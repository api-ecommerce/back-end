package com.ecommerce.services.user;

import com.ecommerce.dtos.TokenResponseDTO;
import com.ecommerce.dtos.user.CadastrarUsuarioRequestDTO;
import com.ecommerce.dtos.user.CadastrarUsuarioResponseDTO;
import com.ecommerce.dtos.user.LoginRequestDTO;
import com.ecommerce.dtos.user.UserResponseDTO;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.exceptions.EventBadRequestException;
import com.ecommerce.exceptions.EventInternalServerErrorException;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.infra.security.TokenService;
import com.ecommerce.repositories.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    IUserRepository repository;

    TokenService tokenService;

    public UserService(TokenService tokenService, IUserRepository repository){
        this.tokenService = tokenService;
        this.repository = repository;
    }

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserModel validarEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado."));
    }

    public ResponseEntity cadastrarUsuario(CadastrarUsuarioRequestDTO request) {
        try {
            // Verifica se o usuário já existe pelo e-mail
            if (repository.findByEmail(request.email()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new CadastrarUsuarioResponseDTO(HttpStatus.CONFLICT, "Usuário com este e-mail já existe."));
            }

            // Verifica se o usuário já existe pelo CPF
            if (repository.findByCpf(request.cpf()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new CadastrarUsuarioResponseDTO(HttpStatus.CONFLICT, "Usuário com este CPF já existe."));
            }

            UserModel novoUsuario = new UserModel();
            novoUsuario.setNome(request.nome());
            novoUsuario.setCpf(request.cpf());
            novoUsuario.setEmail(request.email());

            String encryptedPassword = new BCryptPasswordEncoder().encode(request.senha());
            novoUsuario.setSenha(encryptedPassword);

            repository.save(novoUsuario);

            return new ResponseEntity<>(new CadastrarUsuarioResponseDTO(HttpStatus.CREATED, "Usuário criado com sucesso."), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new EventInternalServerErrorException("Erro interno ao cadastrar usuário: " + ex.getMessage());
        }
    }


    public ResponseEntity login(LoginRequestDTO data){
        try{

            Optional<UserModel> userOptional = repository.findByEmail(data.email());

            if(userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserModel userData = userOptional.get();

            if(userData.getEmail().equals(data.email()) && passwordEncoder.matches(data.senha(), userData.getPassword()))
                return new ResponseEntity<>(new TokenResponseDTO(userData.getEmail(), tokenService.generateToken(userData), true), HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException ex){
            throw new EventBadRequestException(ex.getMessage());
        }
    }

    public Optional<UserModel> findByEmail(String email){return repository.findByEmail(email);}

    public ResponseEntity<UserResponseDTO> acharPeloEmail(String email){
        try{

            UserModel usuario = validarEmail(email);

            return new ResponseEntity<>(new UserResponseDTO(usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getRole()), HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }
}

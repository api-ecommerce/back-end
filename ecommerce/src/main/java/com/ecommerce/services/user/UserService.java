package com.ecommerce.services.user;

import com.ecommerce.dtos.TokenResponseDTO;
import com.ecommerce.dtos.user.*;
import com.ecommerce.entities.user.UserModel;
import com.ecommerce.entities.user.UserRole;
import com.ecommerce.exceptions.EventBadRequestException;
import com.ecommerce.exceptions.EventInternalServerErrorException;
import com.ecommerce.exceptions.EventNotFoundException;
import com.ecommerce.infra.email.EmailSenderDTO;
import com.ecommerce.infra.email.EmailService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    IUserRepository repository;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserModel) authentication.getPrincipal();
    }

    private UserModel validateEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado."));
    }


    public ResponseEntity registerUser(RegisterUserRequestDTO request) {
        try {

            // Verifica se o usuário já existe pelo CPF
            if (repository.findByCpf(request.cpf()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new RegisterUserResponseDTO(HttpStatus.CONFLICT, "Usuário com este CPF já existe."));
            }

            // Verifica se o usuário já existe pelo e-mail
            if (repository.findByEmail(request.email()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new RegisterUserResponseDTO(HttpStatus.CONFLICT, "Usuário com este e-mail já existe."));
            }

            UserModel newUser = new UserModel();
            newUser.setNome(request.nome());
            newUser.setCpf(request.cpf());
            newUser.setEmail(request.email());

            String encryptedPassword = new BCryptPasswordEncoder().encode(request.senha());
            newUser.setSenha(encryptedPassword);

            repository.save(newUser);

            return new ResponseEntity<>(new RegisterUserResponseDTO(HttpStatus.CREATED, "Usuário criado com sucesso."), HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new EventInternalServerErrorException("Erro interno ao cadastrar usuário: " + ex.getMessage());
        }
    }


    public ResponseEntity login(LoginRequestDTO data){
        try{

            Optional<UserModel> userOptional = repository.findByEmail(data.email());

            if(userOptional.isEmpty()) return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);

            UserModel user = userOptional.get();

            if(user.getEmail().equals(data.email()) && passwordEncoder.matches(data.senha(), user.getPassword()))
                return new ResponseEntity<>(new TokenResponseDTO(user.getEmail(), tokenService.generateToken(user), true), HttpStatus.OK);

            return new ResponseEntity<>("Email ou senha incorreto.", HttpStatus.NOT_FOUND);
        }catch (RuntimeException ex){
            throw new EventBadRequestException(ex.getMessage());
        }
    }

    public ResponseEntity<UserResponseDTO> findByEmail(String email){
        try{
            UserModel user = validateEmail(email);

            return new ResponseEntity<>(new UserResponseDTO(user.getNome(), user.getEmail(), user.getCpf(), user.getRole()), HttpStatus.OK);

        }catch(JpaSystemException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        try{
            List<UserModel> allUser = repository.findAll();
            List<UserResponseDTO> response = new ArrayList<>();

            if(allUser.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            for(UserModel user : allUser)
                response.add(new UserResponseDTO(user.getNome(), user.getCpf(), user.getEmail(), user.getRole()));

            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (RuntimeException ex){
            throw new EventInternalServerErrorException();
        }
    }

    public ResponseEntity forgotPassword(ForgotPasswordRequestDTO request) {
        try {
            Optional<UserModel> user = repository.findByEmail(request.email());

            if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UUID passwordGenerator = UUID.randomUUID();
            String[] password = passwordGenerator.toString().split("-");

            emailService.sendEmail(new EmailSenderDTO(request.email(), "Nova senha!", "Sua senha foi alterada para: " + password[0]));

            String encryptedPassword = passwordEncoder.encode(password[0]);

            user.get().setSenha(encryptedPassword);

            repository.save(user.get());

            return new ResponseEntity<>(new ForgotPasswordResponseDTO("Senha enviada com sucesso.",true), HttpStatus.OK);

        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());
        }
    }

    public ResponseEntity editPassword(EditPasswordRequestDTO data, String email){
        try{
            UserModel user = validateEmail(email);

            if(!(passwordEncoder.matches(data.senhaAtual(), user.getSenha()) && email.equals(user.getEmail())))
                return new ResponseEntity<>("Senha atual inválida", HttpStatus.BAD_REQUEST);

            user.setSenha(passwordEncoder.encode(data.senha()));

            repository.save(user);

            return new ResponseEntity<>("Senha editada com sucesso!",HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }catch (RuntimeException ex){
            throw new EventBadRequestException();
        }
    }

    public ResponseEntity editAdminPermission(String email, UserEditTypeRequestDTO request){
        try {
            Optional<UserModel> userOptional = repository.findByEmail(email);
            if(userOptional.isEmpty()){ return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

            UserModel user = userOptional.get();

            user.setRole(request.admin() ? UserRole.ADMIN : UserRole.USER);

            repository.save(user);

            return new ResponseEntity<>("Tipo do usuário alterado com sucesso", HttpStatus.OK);
        }catch (JpaSystemException ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }catch(RuntimeException ex){
            throw new EventBadRequestException(ex.getMessage());
        }
    }

}

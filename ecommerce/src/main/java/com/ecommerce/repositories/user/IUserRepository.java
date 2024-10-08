package com.ecommerce.repositories.user;

import com.ecommerce.entities.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, String> {
    List<UserModel> findByActiveTrue();
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByCpf(String cpf);
}

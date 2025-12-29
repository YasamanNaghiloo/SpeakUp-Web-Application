package com.example.speak_up.data_layer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.speak_up.business_logic.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u_id FROM users WHERE email = :email AND pwd = SHA2(:pwd_to_check, 256)", nativeQuery = true)
    Optional<Long> checkCredentials(@Param("email") String email, @Param("pwd_to_check") String pwdToCheck);

    @Modifying
    @Query(value = "INSERT INTO users (email, u_name, f_name, l_name, pwd, phone_num) VALUES (:email, :u_name, :f_name, :l_name, SHA2(:pwd, 256), :phone_num)", nativeQuery = true)
    void createUser(
        @Param("email") String email,
        @Param("u_name") String username,
        @Param("f_name") String fName,
        @Param("l_name") String lName,
        @Param("pwd") String pwd,
        @Param("phone_num") String phoneNum
    );
}
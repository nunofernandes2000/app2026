package com.example.spring_es.spring.repository;

import com.example.spring_es.spring.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    // ✅ Vai buscar as roles pela tabela user_roles + role
    @Query(value = """
        SELECT r.name
        FROM user_roles ur
        JOIN role r ON r.id = ur.role_id
        JOIN user u ON u.id = ur.user_id
        WHERE u.email = :email
    """, nativeQuery = true)
    List<String> findRoleNamesByEmail(@Param("email") String email);
}

package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
    User findByLogin(String login);

    @Query("SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
    User userLogin(@Param("login") String login, @Param("password") String password);
}

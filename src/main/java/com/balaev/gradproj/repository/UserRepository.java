package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
    User findByLogin(String login);
}

package com.balaev.gradproj.repository;

import com.balaev.gradproj.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created in 2017 year.
 *
 * @autor Arif Balaev
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
}

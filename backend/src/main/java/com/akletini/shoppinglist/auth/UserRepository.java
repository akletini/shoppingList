package com.akletini.shoppinglist.auth;

import com.akletini.shoppinglist.auth.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM USER WHERE USERNAME = ?1", nativeQuery = true)
    User findByUsername(String username);
}

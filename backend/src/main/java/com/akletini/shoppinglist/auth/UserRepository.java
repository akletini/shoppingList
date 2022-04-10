package com.akletini.shoppinglist.auth;

import com.akletini.shoppinglist.auth.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

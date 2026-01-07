package com.newstagram.logging.domain.repository;

import com.newstagram.logging.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

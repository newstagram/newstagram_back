package com.ssafy.newstagram.api.logging.domain.repository;

import com.ssafy.newstagram.api.logging.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyUserRepository extends JpaRepository<User, Long> {

}

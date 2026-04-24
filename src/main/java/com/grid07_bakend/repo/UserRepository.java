package com.grid07_bakend.repo;

import com.grid07_bakend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

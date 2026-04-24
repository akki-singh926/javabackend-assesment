package com.grid07_bakend.repo;

import com.grid07_bakend.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}

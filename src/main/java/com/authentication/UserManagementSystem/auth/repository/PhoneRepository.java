package com.authentication.UserManagementSystem.auth.repository;

import com.authentication.UserManagementSystem.auth.model.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}

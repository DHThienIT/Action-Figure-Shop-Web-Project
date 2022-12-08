package com.baitapnhom.renpository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.User;

@Repository
public interface HomeRepository extends JpaRepository<User, Long> {
}

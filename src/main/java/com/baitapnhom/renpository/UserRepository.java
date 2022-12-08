package com.baitapnhom.renpository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{


   Optional<User> findByUsername(String username);
   @Query(value = "SELECT * FROM User t WHERE t.username LIKE %?1%", nativeQuery = true)
   User findbyuser(String username);

}

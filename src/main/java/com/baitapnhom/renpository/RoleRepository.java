package com.baitapnhom.renpository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT t.role_id FROM user_role t WHERE t.user_id LIKE %?1%", nativeQuery = true)
    List<String> getRoleNames(Long id);
}

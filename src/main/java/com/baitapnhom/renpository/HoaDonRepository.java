package com.baitapnhom.renpository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.entity.User;
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    Optional<HoaDon> findById(Long id);
    List<HoaDon> findByStatus(Boolean status);
    List<HoaDon> findByUser(User user );
}

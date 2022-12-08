package com.baitapnhom.renpository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.KhuyenMai;

@Repository
public interface KhuyenmaiRepository extends JpaRepository<KhuyenMai, Long> {
 
    
}

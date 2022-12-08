package com.baitapnhom.renpository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.SanPham;

@Repository
public interface SearchRepository  extends JpaRepository<SanPham, Long>{
    @Query("SELECT p FROM SanPham p WHERE p.name LIKE %?1%")
    public List<SanPham> search(String keyword);
    List<SanPham> findByName(String name);

}

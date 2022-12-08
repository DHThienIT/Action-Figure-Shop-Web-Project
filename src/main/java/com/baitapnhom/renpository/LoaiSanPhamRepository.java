package com.baitapnhom.renpository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.LoaiSanPham;

@Repository
public interface LoaiSanPhamRepository extends  JpaRepository<LoaiSanPham, Integer> {
    LoaiSanPham findByName(String name);
    Page<LoaiSanPham> findAll(Pageable pageable);

 
}

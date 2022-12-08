package com.baitapnhom.renpository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.SanPham;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    SanPham findByName(String name);
    Page<SanPham> findAll(Pageable pageable);
    List<SanPham> findAllByOrderByGiaAsc();
    List<SanPham> findAllByOrderByGiaDesc();
    List<SanPham> findAllByOrderByNgaysanxuatDesc();
}

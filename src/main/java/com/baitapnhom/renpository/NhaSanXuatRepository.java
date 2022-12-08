package com.baitapnhom.renpository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baitapnhom.entity.NhaSanXuat;

@Repository
public interface NhaSanXuatRepository  extends JpaRepository<NhaSanXuat, Long>{
   NhaSanXuat findByName(String name);
   
}

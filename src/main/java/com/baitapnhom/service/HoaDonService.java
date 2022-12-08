package com.baitapnhom.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.renpository.HoaDonRepository;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    
    public Optional<HoaDon> findById(Long id) {
        return hoaDonRepository.findById(id);
    }
}

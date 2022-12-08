package com.baitapnhom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.SanPham;
import com.baitapnhom.renpository.SearchRepository;

@Service 
public class SearchService {
    @Autowired
    private SearchRepository searchRepository;
     
    public List<SanPham> listAll(String keyword) {
        if (keyword != null) {
            return searchRepository.search(keyword);
        }
        return searchRepository.findByName(keyword);
    }
}

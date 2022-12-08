package com.baitapnhom.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;

@Entity
public class LoaiSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="nameLoaiSanPham")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loaiSanPham", orphanRemoval = true)
    @JsonIgnore
    private List<SanPham> sanphams = new ArrayList<>();
    public LoaiSanPham() {
        super();
        // TODO Auto-generated constructor stub
    }
    public LoaiSanPham( int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public List<SanPham> getSanphams() {
        return sanphams;
    }
    public void setSanphams(List<SanPham> sanphams) {
        this.sanphams = sanphams;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    

    
}

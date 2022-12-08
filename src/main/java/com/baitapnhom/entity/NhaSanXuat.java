package com.baitapnhom.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class NhaSanXuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String motacty;
    private String email;
    private String phone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nhaSanXuat", orphanRemoval = true)
    @JsonIgnore
    private List<SanPham> sanphams = new ArrayList<>();
    public NhaSanXuat() {
        super();
    }
    
    public NhaSanXuat(String name, String location, String motacty, String email, String phone) {
        super();
        this.name = name;
        this.location = location;
        this.motacty = motacty;
        this.email = email;
        this.phone = phone;
 
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getMotacty() {
        return motacty;
    }
    public void setMotacty(String motacty) {
        this.motacty = motacty;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public List<SanPham> getSanphams() {
        return sanphams;
    }
    public void setSanphams(List<SanPham> sanphams) {
        this.sanphams = sanphams;
    }

    
}

package com.baitapnhom.entity;

import java.sql.Date;
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
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int phantramKM;
    private Date ngayBD;
    private Date ngayKT;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "khuyenmai", orphanRemoval = true)
    @JsonIgnore
    private List<SanPham> sanphams = new ArrayList<>();
    public KhuyenMai(String name, int phantramKM, Date ngayBD, Date ngayKT, List<SanPham> sanphams) {
        super();
        this.name = name;
        this.phantramKM = phantramKM;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.sanphams = sanphams;
    }
    public KhuyenMai() {
        super();
        // TODO Auto-generated constructor stub
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
    public int getPhantramKM() {
        return phantramKM;
    }
    public void setPhantramKM(int phantramKM) {
        this.phantramKM = phantramKM;
    }
    public Date getNgayBD() {
        return ngayBD;
    }
    public void setNgayBD(Date ngayBD) {
        this.ngayBD = ngayBD;
    }
    public Date getNgayKT() {
        return ngayKT;
    }
    public void setNgayKT(Date ngayKT) {
        this.ngayKT = ngayKT;
    }
    public List<SanPham> getSanphams() {
        return sanphams;
    }
    public void setSanphams(List<SanPham> sanphams) {
        this.sanphams = sanphams;
    }
}

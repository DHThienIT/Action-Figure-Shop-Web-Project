package com.baitapnhom.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateTime;
    private String tensp;
    private double price;
    private String paymentMethod;
    private boolean wasPay;
    private Boolean status;
    
    private double gia;
    private int soluong;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sanpham_id")
    private SanPham sanpham_id;
    public double getGia() {
        return gia;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public SanPham getSanpham_id() {
        return sanpham_id;
    }

    public void setSanpham_id(SanPham sanpham_id) {
        this.sanpham_id = sanpham_id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public HoaDon() {
    }

    public HoaDon(String dateTime, double price, String paymentMethod, boolean wasPay) {
        super();
        this.dateTime = dateTime;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.wasPay = wasPay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateCreate() {
        return dateTime;
    }

    public void setDateCreate(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public boolean isWasPay() {
        return wasPay;
    }

    public void setWasPay(boolean wasPay) {
        this.wasPay = wasPay;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

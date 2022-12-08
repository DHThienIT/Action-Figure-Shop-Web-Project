package com.baitapnhom.entity;

import java.sql.Date;
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




@Entity
public class SanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String name;
    private String mota;
    private String image;
    private String mamau;
    private Date ngaysanxuat;
    private double gia;
    private double giaSauKhuyenMai;
    private int soluong;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loaisanpham_id")
    private LoaiSanPham loaiSanPham;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nhasanxuat_id")
    private NhaSanXuat nhaSanXuat;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="khuyenmai_id")
    private KhuyenMai khuyenmai;
    

    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sanpham_id", orphanRemoval = true)
    private List<HoaDon> hoaDons = new ArrayList<>();

    public Long getId() {
        return id;
    }
    

    public String getMamau() {
        return mamau;
    }


    public void setMamau(String mamau) {
        this.mamau = mamau;
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

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getNgaysanxuat() {
        return ngaysanxuat;
    }

    public void setNgaysanxuat(Date ngaysanxuat) {
        this.ngaysanxuat = ngaysanxuat;
    }

 
 
 
    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public List<HoaDon> getHoaDons() {
        return hoaDons;
    }

    public void setHoaDons(List<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public NhaSanXuat getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(NhaSanXuat nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public KhuyenMai getKhuyenmai() {
        return khuyenmai;
    }

    public void setKhuyenmai(KhuyenMai khuyenmai) {
        this.khuyenmai = khuyenmai;
    }

    public SanPham() {
        super();
        // TODO Auto-generated constructor stub
    }

  

    public SanPham(String name, String mota, String image, Date ngaysanxuat, double gia, int soluong,
            LoaiSanPham loaiSanPham, NhaSanXuat nhaSanXuat, KhuyenMai khuyenmai) {
        super();
        this.name = name;
        this.mota = mota;
        this.image = image;
        this.ngaysanxuat = ngaysanxuat;
        this.gia = gia;
        this.soluong = soluong;
        this.loaiSanPham = loaiSanPham;
        this.nhaSanXuat = nhaSanXuat;
        this.khuyenmai = khuyenmai;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
    
    public double getGiaSauKhuyenMai() {
        return giaSauKhuyenMai;
    }

    public void setGiaSauKhuyenMai(double giaSauKhuyenMai) {
        this.giaSauKhuyenMai = giaSauKhuyenMai;
    }
    
    
    
}

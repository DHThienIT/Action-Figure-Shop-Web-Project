package com.baitapnhom.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class TinTuc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String noidung;
	private String image;
	private String loaitin;
	
	public TinTuc() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TinTuc(String name, String noidung, String image, String loaitin) {
		super();
		this.name = name;
		this.noidung = noidung;
		this.image = image;
		this.loaitin = loaitin;
	}
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNoidung() {
		return noidung;
	}
	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLoaitin() {
		return loaitin;
	}
	public void setLoaitin(String loaitin) {
		this.loaitin = loaitin;
	}

	
	
	

}

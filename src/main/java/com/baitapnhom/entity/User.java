package com.baitapnhom.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
    private String username;
	private String fullname;
    private String password;
    private String address;
    private String email;
    private String phone;
    private String image;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> role;
    
    
    private Boolean status;
    
	public Boolean getStatus() {
        return status;
    }


    public void setStatus(Boolean status) {
        this.status = status;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User(String username, String fullname, String password, String address, String email,
			String phone, String image) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.password = password;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.image = image;
	
	}
	

	public List<Role> getRole() {
        return role;
    }


    public void setRole(List<Role> role) {
        this.role = role;
    }


    public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	


	public String getFullname() {
        return fullname;
    }


    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", address=" + address
				+ ", email=" + email + ", phone=" + phone + ", image=" + image +  "]";
	}


	
    
    
    
    

	
}

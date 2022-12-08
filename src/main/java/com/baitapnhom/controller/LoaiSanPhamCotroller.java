package com.baitapnhom.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baitapnhom.entity.LoaiSanPham;

import com.baitapnhom.renpository.LoaiSanPhamRepository;


@Controller
@RequestMapping("/loaisanpham")
public class LoaiSanPhamCotroller {
    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;
    
    int t=0;
    int ok=0;
    @Autowired
    private HttpSession session;
    private int id_return ;
    
    @RequestMapping(value = "/site/images/{imageBrowes}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("imageBrowes") String image) {

        if (!image.equals("") || image != null) {
            try {
                Path path = Paths.get("src/main/resources/static/site/images/", image);
                byte[] buffer = Files.readAllBytes(path);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);

                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/danhsach")
    public String them(Model model, HttpSession session,@PageableDefault(size = 5,sort="id",direction =Direction.ASC) Pageable pageable,@ModelAttribute("direction") String directions,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
        List<LoaiSanPham> listLoaiSanPham=loaiSanPhamRepository.findAll();
        Page<LoaiSanPham> pages=loaiSanPhamRepository.findAll(pageable);
        
        
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        List<Sort.Order> orders=pages.getSort().stream().collect(Collectors.toList());
        
        if (orders.size()>0) {
            Sort.Order order=orders.get(0);
            model.addAttribute("sort",order.getProperty() );
            model.addAttribute("page",pageable.getPageNumber() );
           
            model.addAttribute("direction",directions);
        }
        else
            model.addAttribute("sort","id" );
        
                model.addAttribute("listloaisanpham",pages);       
                
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        LoaiSanPham loaiSanPham = new LoaiSanPham();
        model.addAttribute("loaisanpham",loaiSanPham);
        if(t==1) {
            model.addAttribute("themkhongthanhcong", "Thêm không thành công!");
            t=0;
        }
        if(ok==1) {
            model.addAttribute("them", new String("Thêm Loại Sản Phẩm Thành Công!"));
            ok=0;
        }
      
        return "LoaiSanPham/danhsach";
    }
    
    
    @PostMapping("/them")
    public String thems(Model model,LoaiSanPham loaisanpham, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {

       
        LoaiSanPham lsp = loaiSanPhamRepository.findByName(loaisanpham.getName());
        if (lsp != null) {           
            List<LoaiSanPham> listloaisanpham = loaiSanPhamRepository.findAll();
            System.out.println("noooooooo");
            model.addAttribute("listloaisanpham", listloaisanpham);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            LoaiSanPham loaiSanPham = new LoaiSanPham();
            model.addAttribute("loaisanpham",loaiSanPham);
            t=1;
           return "redirect:/loaisanpham/danhsach";
        }
        else {
            loaiSanPhamRepository.save(loaisanpham);
             ok =1; 
            List<LoaiSanPham> listloaisanpham = loaiSanPhamRepository.findAll();
            model.addAttribute("listloaisanpham", listloaisanpham);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            LoaiSanPham loaiSanPham = new LoaiSanPham();
            model.addAttribute("loaisanpham",loaiSanPham);
            return "redirect:/loaisanpham/danhsach";
        }
    }
//    
    @GetMapping({"sua/{id}"})
    public String sualsp(@PathVariable Integer id) {
        id_return = id;
        return "redirect:/loaisanpham/edit";
    }
    
    @GetMapping("edit")
    public String suaString(Model model, HttpSession session,@CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
       LoaiSanPham lsp=loaiSanPhamRepository.findById(id_return).orElseThrow();
        model.addAttribute("loaisanphamid",lsp.getId());
        model.addAttribute("loaisanphamname",lsp.getName());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        LoaiSanPham loaiSanPham = new LoaiSanPham();
        model.addAttribute("loaisanpham",loaiSanPham);
        return "LoaiSanPham/edit";
    }
    
    @PostMapping("/SaveEdit")
    public String sualsps(@RequestParam(value ="id",required = false) Integer id,@RequestParam(value ="name",required = false) String name,Model model,LoaiSanPham loaiSanPham, HttpSession session,@CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        
        LoaiSanPham lspp = loaiSanPhamRepository.findByName(loaiSanPham.getName());
        if (lspp != null) {
            model.addAttribute("khongthem", new String("Tên loại sản phẩm: "+name+" đã tồn tại. Thêm Loại Sản Phẩm Không Thành Công!"));
            List<LoaiSanPham> listloaisanpham = loaiSanPhamRepository.findAll();
            model.addAttribute("listloaisanpham", listloaisanpham);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            LoaiSanPham loaiSanPhmam = new LoaiSanPham();
            model.addAttribute("loaisanpham",loaiSanPhmam);
            return "LoaiSanPham/danhsach";
        }
        LoaiSanPham lsp=loaiSanPhamRepository.findById(id).orElseThrow();
        lsp.setId(lsp.getId());
        lsp.setName(name);
        loaiSanPhamRepository.save(lsp);
        System.out.println("id" + lsp.getId());
        System.out.println("name" + name);

        List<LoaiSanPham> listloaisanpham = loaiSanPhamRepository.findAll();
        model.addAttribute("listloaisanpham", listloaisanpham);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        LoaiSanPham loaiSanPhamm = new LoaiSanPham();
        model.addAttribute("loaisanpham",loaiSanPhamm);
        model.addAttribute("them", new String("Chỉnh Sửa Loại Sản Phẩm Thành Công!"));
        return "LoaiSanPham/danhsach";
    }
    @GetMapping("xoa/{id}")
    public String xoalsp(@PathVariable Integer id) {
        LoaiSanPham lsp=loaiSanPhamRepository.findById(id).orElseThrow();
        loaiSanPhamRepository.delete(lsp);
        
        return "redirect:/loaisanpham/danhsach";
    }
    


}

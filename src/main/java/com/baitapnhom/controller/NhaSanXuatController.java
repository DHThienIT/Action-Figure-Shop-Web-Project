package com.baitapnhom.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baitapnhom.entity.NhaSanXuat;

import com.baitapnhom.renpository.NhaSanXuatRepository;



@Controller
@RequestMapping("nhasanxuat")
public class NhaSanXuatController {
    @Autowired
    HttpSession session;
    @Autowired
    private NhaSanXuatRepository nhaSanXuatRepository;
    private Long id_return ;
    private String nameTempString;
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
    public String them(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
        model.addAttribute("listnhasanxuat", listnhaSanXuats);
        model.addAttribute("isNameCookie", cookieValue);
//        model.addAttribute("khongthem", null);
//        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "NhaSanXuat/danhsach";
    }

    @PostMapping("/them")
    public String thems(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "location") String location,
            @RequestParam(value = "motacty") String motacty) {

        NhaSanXuat nsx = nhaSanXuatRepository.findByName(name);
        if (nsx != null) {
            model.addAttribute("khongthem", new String("Tên nhà sản xuất: "+name+" đã tồn tại. Thêm Nhà Sản Xuất Không, Thành Công!"));
            List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
            model.addAttribute("listnhasanxuat", listnhaSanXuats);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            return "NhaSanXuat/danhsach";
        }
        else {
            NhaSanXuat nhaSanXuat = new NhaSanXuat(name, location, motacty, email, phone);
            nhaSanXuatRepository.save(nhaSanXuat);
            model.addAttribute("them", new String("Thêm Nhà Sản Xuất Thành Công!"));
            List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
            model.addAttribute("listnhasanxuat", listnhaSanXuats);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            return "NhaSanXuat/danhsach";
        }
    }

    @GetMapping({"sua/{id}"})
    public String sualsp(@PathVariable Long id) {
        id_return = id;
        return "redirect:/nhasanxuat/edit";
    }
    
    @GetMapping("edit")
    public String suaString(Model model, HttpSession session,@CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        NhaSanXuat nsx=nhaSanXuatRepository.findById(id_return).orElseThrow();
        nameTempString = nsx.getName();
        model.addAttribute("nhasanxuat", nsx);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "NhaSanXuat/edit";
    }

    
    @PostMapping("/SaveEdit")
    public String sualsps(@RequestParam(value ="id",required = false) Long id,@RequestParam(value ="name",required = false) String name,
            @RequestParam(value ="location",required = false) String location,@RequestParam(value ="email",required = false) String email,
            @RequestParam(value ="phone",required = false) String phone,@RequestParam(value ="motacty",required = false) String motacty, 
            Model model,NhaSanXuat nhaSanXuat, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        
        NhaSanXuat nsx = nhaSanXuatRepository.findByName(nhaSanXuat.getName());
        if (nsx != null) {
            if(nameTempString.equals(name)) {
                NhaSanXuat nsxx =nhaSanXuatRepository.findById(id).orElseThrow();
                nsxx.setId(nsxx.getId());
                nsxx.setName(name);
                nsxx.setLocation(location);
                nsxx.setMotacty(motacty);
                nsxx.setEmail(email);
                nsxx.setPhone(phone);
                nhaSanXuatRepository.save(nsxx);
                model.addAttribute("them", new String("Chỉnh Sửa Nhà Sản Xuất Thành Công!"));
            }
         
            else
                model.addAttribute("khongthem", new String("Tên nhà sản xuất: "+name+" đã tồn tại. Chỉnh Sửa Nhà Sản Xuất Không Thành Công!"));
            System.out.println(model.getAttribute("khongthem"));
            List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
            model.addAttribute("listnhasanxuat", listnhaSanXuats);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("userimage",session.getAttribute("userimage").toString());
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            return "NhaSanXuat/danhsach";
        }
        NhaSanXuat nsxx =nhaSanXuatRepository.findById(id).orElseThrow();
        nsxx.setId(nsxx.getId());
        nsxx.setName(name);
        nsxx.setLocation(location);
        nsxx.setMotacty(motacty);
        nsxx.setEmail(email);
        nsxx.setPhone(phone);
        nhaSanXuatRepository.save(nsxx);
        model.addAttribute("them", new String("Chỉnh Sửa Nhà Sản Xuất Thành Công!"));
        List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
        model.addAttribute("listnhasanxuat", listnhaSanXuats);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        NhaSanXuat nhasanxuat = new NhaSanXuat();
        model.addAttribute("nhasanxuat",nhasanxuat);
        return "NhaSanXuat/danhsach";
    }
    @GetMapping("xoa/{id}")
    public String xoalsp(@PathVariable("id") Long id,Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        NhaSanXuat nhaSanXuat = nhaSanXuatRepository.findById(id).orElseThrow();
        nhaSanXuatRepository.delete(nhaSanXuat);  
        List<NhaSanXuat> listnhaSanXuats = nhaSanXuatRepository.findAll();
        model.addAttribute("listnhasanxuat", listnhaSanXuats);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "redirect:/nhasanxuat/danhsach";
    }
}

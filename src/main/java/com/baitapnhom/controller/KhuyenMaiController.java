package com.baitapnhom.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.baitapnhom.entity.KhuyenMai;
import com.baitapnhom.entity.LoaiSanPham;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.KhuyenmaiRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailService;

@Controller
@RequestMapping("/khuyenmai")
public class KhuyenMaiController extends Thread {
    Long id_return;
    Thread t;
    Long idkm;
    @Autowired 
    KhuyenmaiRepository khuyenmaiRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession session;
    private  List<User> listuser;
    @GetMapping("/danhsach")
    public String them(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("listkhuyenmai", khuyenmaiRepository.findAll());
        model.addAttribute("khongthem", null);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));  
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
//        ThreadController T1 = new ThreadController("Thread-1-HR-Database");
//        T1.start();
//        List<User> list=userRepository.findAll();
//        SendMailController T2 = new SendMailController("3", list);
//        T2.start();
//
//        System.out.println("==> Main thread stopped!!! ");
        return "khuyenmai/danhsach";
    }
    @PostMapping("them")
    public String them(KhuyenMai khuyenMai) {
        khuyenmaiRepository.save(khuyenMai);
        idkm=khuyenMai.getId();
        listuser=userRepository.findAll();
       
            this.start();
            
            
     
        return "redirect:/khuyenmai/danhsach";
    }
    
    
    public void start() {
      
     
           t = new Thread(this);
           t.start();
    
   
}
    @Override
    public void run() {
        for(User user : listuser) {
            System.out.println("luong ddax chay");
        emailService.sendSimpleEmail(user.getEmail(),
                
                "Xác thực email",
                "Xin chào quý khách rất vui được phu mụ quý khách . Mã xác nhận : 9h19");
        }
        t.stop();
        t=null;
        
    }
  
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
    
    @GetMapping("edit/{id}")
    public String editid(@PathVariable Long id) {
        id_return=id;
        KhuyenMai khuyenMai=khuyenmaiRepository.findById(1L).orElseThrow();
      
        return "redirect:/khuyenmai/edit";
    }
    @GetMapping("edit")
    public String edit(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        KhuyenMai khuyenMai=khuyenmaiRepository.findById(id_return).orElseThrow();
        model.addAttribute("khuyenmai",khuyenMai);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        return "khuyenmai/edit";
    }
    @PostMapping("edit")
    public String editsave(KhuyenMai khuyenMai) {
    
        KhuyenMai khuyenMais=khuyenmaiRepository.findById(khuyenMai.getId()).orElseThrow();
        khuyenMais.setName(khuyenMai.getName());
        khuyenMais.setPhantramKM(khuyenMai.getPhantramKM());
        khuyenMais.setNgayBD(khuyenMai.getNgayBD());
        khuyenMais.setNgayKT(khuyenMai.getNgayKT());
        khuyenmaiRepository.save(khuyenMais);
        return "redirect:/khuyenmai/danhsach";
    }
    @GetMapping("/xoa/{id}")
    public String xoa(Model model,@PathVariable Long id) {
        KhuyenMai khuyenMai=khuyenmaiRepository.findById(id).orElseThrow();
        khuyenmaiRepository.delete(khuyenMai);
        return "redirect:/khuyenmai/danhsach";
    }
    
}

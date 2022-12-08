package com.baitapnhom.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baitapnhom.config.MailBuilder;
import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.entity.SanPham;
import com.baitapnhom.entity.Mail;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.HoaDonRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailService;
import com.baitapnhom.service.EmailServices;

@Controller
@RequestMapping("/hoadon")
public class HoaDonController  extends Thread {
    Thread t;
    User usermail;
    double tongtien;
    HoaDon hoaDon;
    List<HoaDon> listhd=new ArrayList<>();
    @Autowired
    private EmailServices emailServices;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("lichsu")
    public String lichsu(Model model,HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user =userRepository.findByUsername(username).orElseThrow();
        List<HoaDon> listhoaDon=hoaDonRepository.findByUser(user);
        List<HoaDon> list=new ArrayList<>();
        for (HoaDon hoadon :listhoaDon) {
            if (hoadon.isWasPay()) {
                list.add(hoadon);
            }
        }
        System.out.println(listhoaDon.size());      
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("listHoaDon", list);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "hoadon/lichsu";
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
    @GetMapping("/danhsach")
    public String them(Model model, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
        List<HoaDon> listHoaDon = hoaDonRepository.findAll();
        List<HoaDon> listnew=new  ArrayList<>();
        Long a=0L;
        String b="";
        int sl[];
        for(HoaDon hoadon : listHoaDon) {
           
            if(a!=hoadon.getUser().getId()) {
            a=hoadon.getUser().getId();
            b=hoadon.getDateCreate();
          
            listnew.add(hoadon);
            }
            else if (!b.equalsIgnoreCase(hoadon.getDateCreate())) {
               
                b=hoadon.getDateCreate();
               
                listnew.add(hoadon);                  
                
            }                 
            
        }    
        
        
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("listHoaDon", listnew);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "hoadon/danhsach";
    }
    
    @GetMapping("xoa/{id}")
    public String xoa(@PathVariable Long id) {
        HoaDon hoaDon=hoaDonRepository.findById(id).orElseThrow();
        hoaDonRepository.delete(hoaDon);
        
        return "redirect:/hoadon/danhsach";
    }
    
    @GetMapping("/danhsachdonhang")
    public String donhangcanduyet(Model model, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
        List<HoaDon> listHoaDon = hoaDonRepository.findByStatus(false);
        
      
        List<HoaDon> listnew=new  ArrayList<>();
        Long a=0L;
        String b="";
        int sl[];
        for(HoaDon hoadon : listHoaDon) {
           
            if(a!=hoadon.getUser().getId()) {
            a=hoadon.getUser().getId();
            b=hoadon.getDateCreate();
            listnew.add(hoadon);
            }
            else if (!b.equalsIgnoreCase(hoadon.getDateCreate())) {
               
                b=hoadon.getDateCreate();
                
                listnew.add(hoadon);                  
                
            }                 
            
        }    
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("listHoaDon", listnew);
        model.addAttribute("tt",  session.getAttribute("tt"));
        session.setAttribute("tt", null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "donhang/show";
    }
    
    @GetMapping("/duyet/{id}")
    public String duyetdon(@PathVariable Long id,RedirectAttributes redirectAttributes,Model model, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
         hoaDon=hoaDonRepository.findById(id).orElseThrow();
         List<HoaDon> listHoaDon = hoaDonRepository.findByStatus(false);
         String ngay=hoaDon.getDateCreate();
         for(HoaDon hoadon : listHoaDon) {
            
             
             if (hoadon.getUser().getId()==hoaDon.getUser().getId() && hoadon.getDateCreate().equalsIgnoreCase(ngay))
             {
               tongtien+=hoadon.getGia()*hoadon.getSoluong();
                 
                 hoadon.setStatus(true);
//                
               hoadon.setTensp(hoadon.getSanpham_id().getName());
                 listhd.add(hoadon);
//                 hoadon.setDateTime(hoaDon.getDateTime()); 
                 hoaDonRepository.save(hoadon);
                
          }
         }
      
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user=userRepository.findById(hoaDon.getUser().getId()).orElseThrow();
        System.out.println(user.getEmail());
        usermail=user;
        this.start();
        session.setAttribute("tt", 1);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "redirect:/hoadon/danhsachdonhang";
    }
    @GetMapping("/khongduyet/{id}")
    public String khoduyetdon(RedirectAttributes redirectAttributes,@PathVariable Long id,Model model, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
        HoaDon hoaDon=hoaDonRepository.findById(id).orElseThrow();
      
        hoaDonRepository.delete(hoaDon);
        session.setAttribute("tt", 2);
        return "redirect:/hoadon/danhsachdonhang";
    }
    @Override
    public void run() {

       System.out.println("luong ddax chay");
      
       String table="<table style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"
               + "<tr style='border: 1px solid black;  border-collapse: collapse;'>"
                   + "<th style='border: 1px solid black;  border-collapse: collapse;'>Ten Pham</th> "
                   + "<th style='width:100%;border: 1px solid black;  border-collapse: collapse;'>Gia</th>   "
                   + " <th style='width:100%;border: 1px solid black;  border-collapse: collapse;'>SL</th>     "
                   + " <th style='width:100%;border: 1px solid black;  border-collapse: collapse;'>Thanh Tien</th>     "
               + "</tr> ";
       for(HoaDon hoadon: listhd) {
           double tong=hoadon.getGia()*hoadon.getSoluong();
           
               table+= "<tr>"
                   + "<td style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"+hoadon.getTensp()+"</td>"
                   + "<td style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"+hoadon.getGia()+"</td>"
                   + "<td style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"+hoadon.getSoluong()+"</td> "
                   + "<td style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"+tong+"$</td> </tr> ";
             
       }
       table+="<tr><td style='text-align: center;'><strong>Tong tien</strong></td>   <td  colspan='3' style='    text-align: right;'> <strong>"+tongtien+"</strong></td></tr></table>";
        final Mail mail = new MailBuilder()
                .From("letamxi87@gmail.com") // For gmail, this field is ignored.
                .To("thanhb1910139@student.ctu.edu.vn")
                .To(usermail.getEmail())//usermail.getEmail()
                .Template("email/duyet-template.html")
                .AddContext("subject", "<h3 style='color:red;text-align: center;'><strong>Your order information</strong></h3>")
                .AddContext("content", "Hello, <span style='color:blue'><strong>"+usermail.getFullname()+"</strong></span><br><span>Consignee information :<br>-Day oder :<strong>"+hoaDon.getDateCreate()+"</strong><br>-Phone number :<strong>"+usermail.getPhone()+"</strong><br>-Delivery address :<strong>"+usermail.getAddress()+"</strong><br>-Order account :<strong>"+usermail.getUsername()+" </strong></span> ")
                .AddContext("table", table)
                .Subject("CHI TIẾT ĐƠN ĐẶT HÀNG")
                .createMail();
        try {
            emailServices.sendHTMLEmail(mail);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        t.stop();
        t = null;

    } public void start() {

        t = new Thread(this);
        t.start();

    }

}

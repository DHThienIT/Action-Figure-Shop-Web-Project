package com.baitapnhom.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.entity.KhuyenMai;
import com.baitapnhom.entity.LoaiSanPham;
import com.baitapnhom.entity.NhaSanXuat;
import com.baitapnhom.entity.SanPham;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.HoaDonRepository;
import com.baitapnhom.renpository.UserRepository;

@Controller
@RequestMapping({ "/nguoidung", })
public class UserController {
    @Autowired 
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private UserRepository userRepository;
    private static String UPLOADED_FOLDER = "D:\\TN412\\DuAnBaiTapNhomJava_V1\\11THANG8\\Bai_tap_nhom_TN218_04\\src\\main\\resources\\static\\site\\images\\";
 
    @GetMapping("bieudo")
    public String bieudo(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        Map<String, Integer> surveyMap = new LinkedHashMap<>();
        Map<String, Integer> soluongs = new LinkedHashMap<>();
        List<HoaDon> listHoaDon=hoaDonRepository.findAll();
        int[] doanhthu = new int[13];
        for (int i = 1; i < 13; i++) {
            doanhthu[i] = 0;
        }
        int[] soluong = new int[13];
        for (int i = 1; i < 13; i++) {
            soluong[i] = 0;
        }
         for (HoaDon hoadon :listHoaDon) {            
             int day=Integer.parseInt(hoadon.getDateTime().substring(5,7));
             doanhthu[day]+=hoadon.getGia()*hoadon.getSoluong();
             soluong[day]+=hoadon.getSoluong();
             System.out.println(day);
         }
         for ( int i = 1; i < 13; i++) {
             
             surveyMap.put("Tháng "+i,doanhthu[i]);
             soluongs.put("Tháng "+i, soluong[i]);
         }
//        surveyMap.put("Java", 40);
//        surveyMap.put("Dev oops", 25);
//        surveyMap.put("Python", 20);
//        surveyMap.put(".Net", 15);
        model.addAttribute("surveyMap", surveyMap);
        model.addAttribute("soluongs", soluongs);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage")!=null?session.getAttribute("userimage").toString():null);
     
        return "admin/bieudo";
    }
    @GetMapping("/danhsach")
    public String them(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        List<User> listUser = userRepository.findAll();
        model.addAttribute("listUser", listUser);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("error", session.getAttribute("loiuser"));
        session.setAttribute("loiuser",null);
        return "nguoidung/danhsach";
    }

    @GetMapping("xoa/{id}")
    public String xoalsp(@PathVariable("id") Long id,HttpSession session) {
        User User = userRepository.findById(id).orElseThrow();
        try {
        userRepository.delete(User);}
        catch (Exception e) {
            session.setAttribute("loiuser","Tồn tại khóa ngoại");
            return "redirect:/nguoidung/danhsach";
        }
        return "redirect:/nguoidung/danhsach";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        String Usernew = "";
        if (cookieValue.equals("defaultCookieValue"))
            Usernew = (String) session.getAttribute("isNameSession");
        else
            Usernew = cookieValue;
        User user = userRepository.findByUsername(Usernew).orElseThrow();

        model.addAttribute("username", user);
      
        model.addAttribute("capnhatthanhcong", null);
        return "/nguoidung/profile";
    }
    @RequestMapping(value = "/site/images/{imageBrowes}", method= RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("imageBrowes") String image) {
        
        if (!image.equals("") || image!=null) {
            try {
                Path path = Paths.get("src/main/resources/static/site/images/", image);
                byte[] buffer = Files.readAllBytes(path);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
              
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            }catch (Exception e) {
                // TODO: handle exception
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/profile")
    public String capnhat(Model model, @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("fullname") String fullname,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!file.getOriginalFilename().isEmpty()) {

            try {
                String userDirectory = Paths.get("")
                        .toAbsolutePath()
                        .toString();

                // Get the file and save it somewhefile.getOriginalFilename()re
                byte[] bytes = file.getBytes();

                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                user.setImage(file.getOriginalFilename().toString());
                Files.write(path, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            session.setAttribute("userimage",file.getOriginalFilename());
        }
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        userRepository.save(user);
        model.addAttribute("username", user);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("capnhatthanhcong", "user");
        return "/nguoidung/profile";
    }
}

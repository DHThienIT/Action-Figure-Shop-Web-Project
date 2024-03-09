package com.baitapnhom.controller;


import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;


import com.baitapnhom.entity.TinTuc;
import com.baitapnhom.renpository.TinTucRenpository;

@Controller
@RequestMapping("/tintuc")
public class TinTucController {
    @Autowired
    private TinTucRenpository tinTucRenpository;
    Long id_return;
    private static String UPLOADED_FOLDER = "D:\\TN412\\DuAnBaiTapNhomJava_V1\\BaiTapNhom11_11\\Bai_tap_nhom_TN218_04\\src\\main\\resources\\static\\site\\images\\";
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
        TinTuc tintuc = new TinTuc();
        List<TinTuc> listtintuc = tinTucRenpository.findAll();
        model.addAttribute("tintuc", tintuc);
        model.addAttribute("listtintuc", listtintuc);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "tintuc/danhsach";
    }
    
    @PostMapping("/them")
    public String themsp(TinTuc tintuc, @RequestParam("file") MultipartFile file) {

        try {
            String userDirectory = Paths.get("")
                    .toAbsolutePath()
                    .toString();

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            tintuc.setImage(file.getOriginalFilename().toString());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        tinTucRenpository.save(tintuc);

        return "redirect:/tintuc/danhsach";
    }
    
    @GetMapping({ "sua/{id}" })
    public String sualsp(@PathVariable Long id) {
        id_return = id;
        return "redirect:/tintuc/edit";
    }

    @GetMapping("/edit")
    public String suaString(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("tintuc", tinTucRenpository.findById(id_return).orElseThrow());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "tintuc/edit";
    }

    @PostMapping("/edit")
    public String suasps(TinTuc tinTuc, @RequestParam("file") MultipartFile file,
            Model model) {

        TinTuc tintuc = tinTucRenpository.findById(id_return).orElseThrow();
        if (tintuc != null) {
            if(!file.getOriginalFilename().isEmpty()) {
            try {
                String userDirectory = Paths.get("")
                        .toAbsolutePath()
                        .toString();

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());        
                    tintuc.setImage(file.getOriginalFilename().toString());
                Files.write(path, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
            }

            tintuc.setName(tinTuc.getName());
            tintuc.setNoidung(tinTuc.getNoidung());
            tintuc.setLoaitin(tinTuc.getLoaitin());
            tinTucRenpository.save(tintuc);

        }
        return "redirect:/tintuc/danhsach";
    }


    @GetMapping("/user")
    public String showTinTuc(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them",null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));  
        model.addAttribute("listintuc", tinTucRenpository.findAll());
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "tintuc/show";
    }
}

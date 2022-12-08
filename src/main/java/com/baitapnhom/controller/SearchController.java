package com.baitapnhom.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import com.baitapnhom.entity.SanPham;
import com.baitapnhom.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller

public class SearchController {
    @Autowired
    private SearchService searchService;
   @Autowired 
   private HttpSession session;
   
   
   
   
    @RequestMapping("/search")
    public @ResponseBody String SearchProduct(HttpServletRequest request,HttpSession session) {
        String keyword = request.getParameter("name");
        System.out.println(keyword);
        List<SanPham>  listsanphams =new ArrayList<>();
        if (keyword!="") {
         listsanphams = searchService.listAll(keyword);
      
        }
        List<String> lstsap=new ArrayList<>();
     String listSP="";
      

        for(SanPham sanpham : listsanphams)
            listSP+=(sanpham.getName()+"/");
        
        ObjectMapper mapper = new ObjectMapper();
        String ajaxResponse = "";
        try {
            ajaxResponse = mapper.writerWithDefaultPrettyPrinter() .writeValueAsString(listSP);
            System.out.println(ajaxResponse);
        } catch (JsonProcessingException e) {
           System.out.println("ngu nhu heo");;
        }
        
        return ajaxResponse;
    }
    @GetMapping("/search/{keyword}")
    public  String SearchProducta(Model model,@PathVariable String keyword, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
       
        System.out.println(keyword);
        List<SanPham>  listsanphams =new ArrayList<>();
        if (keyword!="") {
         listsanphams = searchService.listAll(keyword);
         System.out.println("hello");
        }
        model.addAttribute("listsanpham", listsanphams);
     
        model.addAttribute("listsanpham", listsanphams);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        return "sanpham/search";
    }
    @PostMapping("/searchs")
    public  String SearchProduct(Model model,@RequestParam(name = "keyword") String keyword, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
       
        System.out.println(keyword);
        List<SanPham>  listsanphams =new ArrayList<>();
        if (keyword!="") {
         listsanphams = searchService.listAll(keyword);
         System.out.println("hello");
        }
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("listsanpham", listsanphams);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "sanpham/search";
    }

}

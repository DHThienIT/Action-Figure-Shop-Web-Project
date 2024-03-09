package com.baitapnhom.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.baitapnhom.config.MailBuilder;
import com.baitapnhom.entity.Card;
import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.entity.KhuyenMai;
import com.baitapnhom.entity.LoaiSanPham;
import com.baitapnhom.entity.Mail;
import com.baitapnhom.entity.NhaSanXuat;
import com.baitapnhom.entity.SanPham;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.KhuyenmaiRepository;
import com.baitapnhom.renpository.LoaiSanPhamRepository;
import com.baitapnhom.renpository.NhaSanXuatRepository;
import com.baitapnhom.renpository.SanPhamRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailService;
import com.baitapnhom.service.EmailServices;
import com.baitapnhom.service.SanPhamService;
import com.baitapnhom.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Slf4j;
import groovyjarjarantlr4.runtime.misc.IntArray;
import net.bytebuddy.asm.Advice.Return;

@Controller
@RequestMapping({ "/sanpham", "" })

public class ProductController extends Thread {
    Thread t;
    User usermail;
    Long chitietid;
    List<SanPham> lsp=new ArrayList<>();
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  EmailServices emailServices;
    @Autowired
    private HttpSession session;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private NhaSanXuatRepository nhaSanXuatRepository;
    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private KhuyenmaiRepository khuyenmaiRepository;
    Long id_return;
    private List<String> lstUser=new ArrayList<>() ;
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

 
    @GetMapping("/sanphamshow")
    public String card_show(Model model,@PageableDefault(size = 8,sort="id",direction =Direction.ASC) Pageable pageable,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        
        
        Page<SanPham> pages=sanPhamRepository.findAll(pageable);
        
        
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        List<Sort.Order> orders=pages.getSort().stream().collect(Collectors.toList());
        
        if (orders.size()>0) {
            Sort.Order order=orders.get(0);
            model.addAttribute("sort",order.getProperty() );
            model.addAttribute("page",pageable.getPageNumber() );
           System.out.println(pageable.getPageNumber()+"fdffdfd" );
           
        }
        
        List<SanPham> listsanpham = sanPhamRepository.findAll();
        model.addAttribute("listsanpham", pages);
        return "/sanpham/sanpham-show";
    }
    @GetMapping("/chitiet/{id}")
    public String chitiet(Model model,@PathVariable Long id,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {

        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        chitietid=id;
        return "redirect:/sanpham/chitiet";
    }
    @GetMapping("/chitiet")
    public String chitietsp(Model model,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        SanPham sanpham = sanPhamRepository.findById(chitietid).orElseThrow();
        int flag=1;
        List<SanPham> listSanpham = new ArrayList<>();
        listSanpham = session.getAttribute("spdaxem")!=null?(List<SanPham>) session.getAttribute("spdaxem"):new ArrayList<>();
        System.out.println(listSanpham.size());
        if(listSanpham.size()>0) {
            for (SanPham sanphams:listSanpham) {
                if(sanphams.getId()==sanpham.getId())
                {
                    flag=0;
                    break;
                }
            }
        }
        if(listSanpham.size()==0 || flag==1)
           listSanpham.add(sanpham);
        System.out.println(listSanpham.size());
        session.setAttribute("spdaxem",listSanpham);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("sanpham", sanpham);
        return "/sanpham/chitiet";
    }
    @GetMapping("/sanphamdaxem")
    public String sanphamdaxem(Model model,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
       
        List<SanPham> listSanpham = new ArrayList<>();
        listSanpham = session.getAttribute("spdaxem")!=null?(List<SanPham>) session.getAttribute("spdaxem"):new ArrayList<>();
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
      System.out.println(listSanpham.size());
        model.addAttribute("listSanpham", listSanpham);
        return "/sanpham/sanphamdaxem";
    }

    @GetMapping("/add-card")
    public @ResponseBody String addCard(HttpServletRequest request, Principal principal) {
        ObjectMapper mapper = new ObjectMapper();
        double tong = 0;
        String ajaxResponse = "";
        Long id = Long.parseLong(request.getParameter("id"));
        List<SanPham> listSanpham = new ArrayList<>();
        System.out.println("moi vo");
        if (principal != null) {
            System.out.println("fdfdfdfd"+session.getAttribute("tong")+"fdfdf");
         
            tong = session.getAttribute("tong") != null ? (double) session.getAttribute("tong") : 0;
            SanPham sanpham = sanPhamRepository.findById(id).orElseThrow();
            int flag = 0;
            if (session.getAttribute("card") != null) {
                // lay sesion

                listSanpham = (List<SanPham>) session.getAttribute("card");

                for (SanPham sp : listSanpham)
                    if (sp.getId() == sanpham.getId()) {
                        SanPham samphamnew = sanPhamRepository.findById(sp.getId()).orElseThrow();
                        
                        System.out.println(samphamnew.getSoluong()+" "+sp.getSoluong());
                        if (samphamnew.getSoluong()<=sp.getSoluong()||samphamnew.getSoluong()==0)
                            return "HetHangkho";
                        sp.setSoluong(sp.getSoluong() + 1);
                        tong += sp.getGiaSauKhuyenMai();
                        flag = 1;
                    }
            }
            if (listSanpham.isEmpty() || flag == 0) {

                
                
                if (sanpham.getSoluong() == 0)
                    return "Khohethangroi";
                Card card = new Card(sanpham.getId(), 1);
                sanpham.setSoluong(1);
                tong += sanpham.getGiaSauKhuyenMai();
                listSanpham.add(sanpham);
            }
            
          String tmep = tong+"";                     
           double tongg = Double.parseDouble(String.format("%,.2f",  Double.parseDouble(tmep))) ;
            
            session.setAttribute("card", listSanpham);
            session.setAttribute("tong", tongg);
            listSanpham = (List<SanPham>) session.getAttribute("card");
            System.out.println(listSanpham.size());
            session.setAttribute("soluong", listSanpham.size());
            for (SanPham sp : listSanpham)
                System.out.println(sp.getName() + " next ");

            try {
                ajaxResponse = mapper.writeValueAsString(listSanpham.size());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return listSanpham.size() + "";
        } else {
            System.out.println("moi vo 111");
            try {
                ajaxResponse = mapper.writeValueAsString("ChuaDangnhap");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(ajaxResponse);
            return ajaxResponse;
        }
    }

    @GetMapping("/danhsach")
    public String them(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        SanPham sanPham = new SanPham();
        List<SanPham> listsanpham = sanPhamRepository.findAll();
        List<NhaSanXuat> nhaSanXuat = nhaSanXuatRepository.findAll();
        List<LoaiSanPham> loaiSanPhams = loaiSanPhamRepository.findAll();
        List<KhuyenMai> khuyenMais = khuyenmaiRepository.findAll();
        model.addAttribute("khuyenmai", khuyenMais);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("listsanpham", listsanpham);
        model.addAttribute("nhaSanXuat", nhaSanXuat);
        model.addAttribute("loaisanpham", loaiSanPhams);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "sanpham/danhsach";
    }

    @PostMapping("/sanpham/them")
    public String themsp(SanPham sanPham, @RequestParam("file") MultipartFile file,@RequestParam("mauNen") String mauNen) {
        String mamau="";
        try {
            String userDirectory = Paths.get("")
                    .toAbsolutePath()
                    .toString();

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            sanPham.setImage(file.getOriginalFilename().toString());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mauNen.equals("1")) {
            mamau="(184,45,68)";
        }
        if(mauNen.equals("2")) {
            mamau="(29,29,29)";
        }
        if(mauNen.equals("3")) {
            mamau="(21,58,130)";
        }
        if(mauNen.equals("4")) {
            mamau="(63,74,105)";
        }
        System.out.println(sanPham.toString());
        float f = sanPham.getKhuyenmai().getPhantramKM();
        double t = (f / 100.0) * sanPham.getGia();
        sanPham.setGiaSauKhuyenMai(sanPham.getGia() - t);
        sanPham.setMamau(mamau);
        sanPhamRepository.save(sanPham);

        return "redirect:/sanpham/danhsach";
    }

    @GetMapping({ "sua/{id}" })
    public String sualsp(@PathVariable Long id) {
        id_return = id;
        System.out.println(id + "'''''''''ahdddd");
        return "redirect:/sanpham/edit";
    }

    @GetMapping("edit")
    public String suaString(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {

        model.addAttribute("loaisanpham", loaiSanPhamRepository.findAll());
        model.addAttribute("nhasanxuat", nhaSanXuatRepository.findAll());
        model.addAttribute("khuyenmai", khuyenmaiRepository.findAll());
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("sanpham", sanPhamRepository.findById(id_return).orElseThrow());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "sanpham/edit";
    }

    @PostMapping("/edit")
    public String suasps(SanPham sanpham,@RequestParam("file") MultipartFile file,
            Model model) {

        SanPham sp = sanPhamRepository.findById(sanpham.getId()).orElseThrow();
        
        if (sp != null) {
            if(!file.getOriginalFilename().isEmpty()) {
            try {
                String userDirectory = Paths.get("")
                        .toAbsolutePath()
                        .toString();

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                sp.setImage(file.getOriginalFilename().toString());
                Files.write(path, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
            }
            sp.setName(sanpham.getName());
            sp.setMota(sanpham.getMota());
            sp.setNgaysanxuat(sanpham.getNgaysanxuat());
            sp.setGia(sanpham.getGia());
            sp.setKhuyenmai(sanpham.getKhuyenmai());
            sp.setLoaiSanPham(sanpham.getLoaiSanPham());
            sp.setNhaSanXuat(sanpham.getNhaSanXuat());
            
            float f = sanpham.getKhuyenmai().getPhantramKM();
            double t = (f / 100.0) * sanpham.getGia();
            sp.setGiaSauKhuyenMai(sanpham.getGia() - t);

            sanPhamRepository.save(sp);

        }
        return "redirect:/sanpham/danhsach";
    }

    @GetMapping("/giohang")
    public String card_giohang(Model model,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("card", session.getAttribute("card"));
        model.addAttribute("tong", session.getAttribute("tong"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "/card/card_giohang";
    }

    @GetMapping("/changesl")
    public @ResponseBody String changesl(HttpServletRequest request, Model model) {
        double tong=0;
        Long id = Long.parseLong(request.getParameter("id"));
        int soluong = Integer.parseInt(request.getParameter("soluong"));
     
        List<SanPham> listSanpham = new ArrayList<>();
        listSanpham = (List<SanPham>) session.getAttribute("card");

        for (SanPham sp : listSanpham) {
            if (sp.getId() == id) {
                SanPham samphamnew = sanPhamRepository.findById(sp.getId()).orElseThrow();
                System.out.println(samphamnew.getSoluong() +"heeh "+soluong);
                if (samphamnew.getSoluong() < soluong) {
                    System.out.println(samphamnew.getSoluong() +"heeh "+soluong);
                    tong+=(sp.getSoluong()-soluong)*sp.getGiaSauKhuyenMai();
                    String tmep = tong+"";                     
                    double tongg = Double.parseDouble(String.format("%,.2f",  Double.parseDouble(tmep))) ;
                     
                    session.setAttribute("tong", tongg);
                    return "HetHangTrongKho"+samphamnew.getSoluong();}
                tong+=(soluong   )*sp.getGiaSauKhuyenMai();
                sp.setSoluong(soluong);
                
               
            }
            else {
                tong+=(sp.getSoluong()   )*sp.getGiaSauKhuyenMai();
            }
            }
        String tmep = tong+"";                     
        double tongg = Double.parseDouble(String.format("%,.2f",  Double.parseDouble(tmep))) ;
         
        model.addAttribute("card", session.getAttribute("card"));
        session.setAttribute("tong", tongg);
        System.out.println("hêhe");
        ObjectMapper mapper = new ObjectMapper();
        String ajaxResponse = "";
        try {
            ajaxResponse = mapper.writeValueAsString(tongg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ajaxResponse;
    }
    
    
    
    @GetMapping("/xoacard/{id}")
    public  String xoacard(@PathVariable Long id, Model model) {
        double tong=session.getAttribute("tong")!=null?(double)session.getAttribute("tong"):0;
       
        System.out.println("helloooooo");
        List<SanPham> listSanpham = new ArrayList<>();
        listSanpham = (List<SanPham>) session.getAttribute("card");
        List<SanPham> listSanphamnew = (List<SanPham>) session.getAttribute("card");
        int i=0;
        
        for (SanPham sp : listSanpham) {
            System.out.println(sp.getId());
            if (sp.getId() == id) {
                tong-=(sp.getSoluong())*sp.getGiaSauKhuyenMai();
                listSanphamnew.remove(i);
                break;
               
            }
            i++;}
        
        session.setAttribute("card", listSanphamnew);
        model.addAttribute("card", listSanphamnew);
        for (SanPham sp : listSanphamnew) {
            System.out.println(sp.getId());
            }
        
        String tmep = tong+"";                     
         double tongg = Double.parseDouble(String.format("%,.2f",  Double.parseDouble(tmep))) ;
        session.setAttribute("tong", tongg);
        session.setAttribute("soluong", listSanphamnew.size());
        model.addAttribute("tong", tongg);

        return "redirect:/sanpham/giohang";
    }

    @PostMapping("/card_show")
    public String card_show(
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue, Model model) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("card", session.getAttribute("card"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        LocalDate date = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        String timeString = calendar.getTime().toString();
        String[] words = timeString.split("\\s");
        String dateTime = date.toString() + " " + words[3];
        model.addAttribute("ngaylap", dateTime);
        return "/card/card-show";
    }

    @GetMapping("/card_xacnhan")
    public String card_xacnhan() {
        return "/card/card_xacnhan";
    }

    @GetMapping("xoa/{id}")
    public String xoalsp(@PathVariable("id") Long id) {
        SanPham sanPham = sanPhamRepository.findById(id).orElseThrow();
        sanPhamRepository.delete(sanPham);
        return "redirect:/sanpham/danhsach";
    }
////gui all qc
    @GetMapping("sendall")
    public String sendall() {
        this.start();
        return "redirect:/sanpham/quangcao";
    }
    
    
    public void start() {

        t = new Thread(this);
        t.start();

    }

    @Override
    public void run() {

       System.out.println("luong ddax chay");
      
       String text="<p style='color:#98A5BF;text-align:center'>Welcome to our website!</p>"
               + " <p style='color:#FFC000;text-align:center'> Promotions with attractive offers sent to you. </p>"
               +"<p style='color:#FF0000;text-align:center'>Promotion of new products:</p>";
       String texts="<table style='width:100%;border: 1px solid black;  border-collapse: collapse;'>"
               + "<tr style='border: 1px solid black;  border-collapse: collapse;'>"
               + "<th style='width:20px;border: 1px solid black;  border-collapse: collapse;'>Ten Pham</th> "
               + "<th style='width:20px;border: 1px solid black;  border-collapse: collapse;'>Ten Khuyen mai</th>   "
               + "<th style='width:20px;border: 1px solid black;  border-collapse: collapse;'>Phan Tram</th>   "
               + " <th style='width:40px;border: 1px solid black;  border-collapse: collapse;'>Ngay Bd</th>     "
               + " <th style='width:40px;border: 1px solid black;  border-collapse: collapse;'>Ngay KT</th>     "
           + "</tr> ";
       for (SanPham sanphams : lsp) {
           System.out.println(sanphams.getName());
           texts+="<tr style='text-align:center;'>"
                   + "<td style='width:20px;border: 1px solid black;  border-collapse: collapse;'>"+sanphams.getName()+"</td>"
                   + "<td style='width:20px;border: 1px solid black;  border-collapse: collapse;'>"+sanphams.getImage()+"</td>"
                   + "<td style='width:20px;border: 1px solid black;  border-collapse: collapse;'>"+sanphams.getSoluong()+"%</td>"
                   + "<td style='width:40px;border: 1px solid black;text-align:center;  border-collapse: collapse;'>"+sanphams.getMamau()+"</td> "
                   + "<td style='width:40px;border: 1px solid black;  border-collapse: collapse;'>"+sanphams.getMota()+"</td> </tr> ";
       }
      
              texts+="</table>";

       for(String user :lstUser) {
      
        final Mail mail = new MailBuilder()
                .From("letamxi87@gmail.com") // For gmail, this field is ignored.
                .To(user)
                .Template("/email/mail-khuyenmai.html")
                .AddContext("subject", text)
                .AddContext("content",texts)
                .Subject("Hello")
                .createMail();
        try {
            emailServices.sendHTMLEmail(mail);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       }
        t.stop();
        t = null;

    }
    
    @GetMapping("/sapxep/{id}")
    public String sapxep(@PathVariable String id, Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "redirect:/sanpham/"+id;
    }
    @GetMapping("/{id}")
    public String tangdan(@PathVariable String id, Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("isNameCookie", cookieValue);
        String khoanggia="";
        if (id.equals("tangdan"))
                model.addAttribute("listsanpham",sanPhamRepository.findAllByOrderByGiaAsc());
        if (id.equals("giamdan"))
            model.addAttribute("listsanpham",sanPhamRepository.findAllByOrderByGiaDesc());
        if(id.equals("sanxuat"))
            model.addAttribute("listsanpham",sanPhamRepository.findAllByOrderByNgaysanxuatDesc());
        float a=0,b=0;
       if (id.equals("0")) {a=0;b=1;khoanggia= "Sản phẩm có giá dưới 1$";}
       if (id.equals("1")) {a=1;b=3; khoanggia= "Sản phẩm có giá từ 1$ đến 3$";}
       if (id.equals("3")) {a=3;b=5;khoanggia= "Sản phẩm có giá từ 3$ đến 5$";}
       if (id.equals("5")) {a=5;b=500;khoanggia= "Sản phẩm có giá trên 5$";}
       List<SanPham> listNew=new ArrayList<>();
       if (model.getAttribute("listsanpham")==null) {
           List<SanPham> listSamPham=sanPhamRepository.findAllByOrderByGiaAsc();
           for (SanPham sanpham :listSamPham) {
                   if(sanpham.getGiaSauKhuyenMai()>=a && sanpham.getGiaSauKhuyenMai()<=b)
                        listNew.add(sanpham);}
                     
       
           model.addAttribute("listsanpham",listNew);
           if(listNew.size()==0){
               model.addAttribute("khoanggiasapxep", "Không có ản phẩm có giá từ "+a+"$" +b+"$");}
            else {
           model.addAttribute("khoanggiasapxep",khoanggia);
           }
           
       }
       model.addAttribute("userimage",session.getAttribute("userimage").toString());
       
        return "sanpham/sanpham-show";
    }
    

    // gửi quảng cáo
    @GetMapping("/quangcao")
    public String quangcao(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        SanPham sanPham = new SanPham();
        LocalDate date = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        String timeString = calendar.getTime().toString();
          lsp=new ArrayList<>();
        List<SanPham> listsanpham = sanPhamRepository.findAll();
       List<User>listuser=userRepository.findAll();
       for (User user : listuser) {
           lstUser.add(user.getEmail());
           System.out.println(user.getEmail());
           }
        List<SanPham> listsanphamnew = new ArrayList<>();
        for (SanPham sanpham : listsanpham) {
            if (sanpham.getKhuyenmai() != null) {
                KhuyenMai khuyenmai = sanpham.getKhuyenmai();
                if (LocalDate.parse(khuyenmai.getNgayBD().toString()).compareTo(LocalDate.now()) < 0
                        && LocalDate.parse(khuyenmai.getNgayKT().toString()).compareTo(LocalDate.now()) > 0) {
                   
                    sanpham.setImage(sanpham.getKhuyenmai().getName());
                    sanpham.setMamau(sanpham.getKhuyenmai().getNgayBD().toString());
                    sanpham.setMota(sanpham.getKhuyenmai().getNgayKT().toString());
                    sanpham.setSoluong(sanpham.getKhuyenmai().getPhantramKM());
                    lsp.add(sanpham);
                    System.out.println(sanpham.getName());
                    listsanphamnew.add(sanpham);
                }
            }
        }
        List<NhaSanXuat> nhaSanXuat = nhaSanXuatRepository.findAll();
        List<LoaiSanPham> loaiSanPhams = loaiSanPhamRepository.findAll();
        List<KhuyenMai> khuyenMais = khuyenmaiRepository.findAll();
        model.addAttribute("khuyenmai", khuyenMais);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("listsanpham", listsanphamnew);
        model.addAttribute("nhaSanXuat", nhaSanXuat);
        model.addAttribute("loaisanpham", loaiSanPhams);
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("khongthem", null);
        model.addAttribute("them", null);
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "sanpham/guiquangcao";
    }
}

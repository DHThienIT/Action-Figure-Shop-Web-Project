package com.baitapnhom.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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


import com.baitapnhom.config.MailBuilder;
import com.baitapnhom.config.WebUtils;
import com.baitapnhom.entity.KhuyenMai;
import com.baitapnhom.entity.Mail;
import com.baitapnhom.entity.Role;
import com.baitapnhom.entity.SanPham;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.RoleRepository;
import com.baitapnhom.renpository.SanPhamRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailService;
import com.baitapnhom.service.EmailServices;
import com.baitapnhom.service.EmailService;
import com.baitapnhom.service.UserService;

@Controller
@RequestMapping("")
public class HomeController extends Thread {
    Thread t;
    User usermail;
    int code;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private  EmailServices emailServices;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    int h=0,phantramm;
    @GetMapping({ "/home", "" })
    public String listStudents(Model model,HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        System.out.println(session.getAttribute("role"));
        model.addAttribute("ok",session.getAttribute("ok"));
        session.setAttribute("ok", null);
            model.addAttribute("listsanpham", sanPhamRepository.findAll());
            if(h==1) {
                h=0;
                model.addAttribute("nhanduockhuyenmai", "Đã nhận khuyến mãi thành công");
            }
            //Random giảm giá
            int min = 7;  
            int max = 15;
            int a = (int) (Math.random()*(max-min+1)+min);
            int b = (int) (Math.random()*(max-min+1)+min);
            int c = (int) (Math.random()*(max-min+1)+min);
            
            model.addAttribute("random1", a);
            model.addAttribute("random2", b);
            model.addAttribute("random3", c);
            
            if(session.getAttribute("voucherGiam")==null) {
                model.addAttribute("y", 1);
            } else {
                model.addAttribute("y", 0);
                model.addAttribute("random", session.getAttribute("voucherGiam"));
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if (username!="anonymousUser") {
                User user=userRepository.findByUsername(username).orElseThrow();
                session.setAttribute("userimage",user.getImage());}
            else
            {
                session.setAttribute("userimage","user.png");
            }
        
            String image=session.getAttribute("userimage").toString();
        if ((session.getAttribute("isNameSession") != null) && (cookieValue.equals("defaultCookieValue"))) {
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("dadangnhap", "okieValue");
            model.addAttribute("isNameCookie", cookieValue);
            
           model.addAttribute("userimage",session.getAttribute("userimage").toString());
            return "/home/home";
        }
        if (!(cookieValue.equals("defaultCookieValue") && (session.getAttribute("isNameSession") == null))) {
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("dadangnhap", "okieValue");
            model.addAttribute("userimage",session.getAttribute("userimage").toString());
            return "/home/home";
        }
        if ((cookieValue.equals("defaultCookieValue") && (session.getAttribute("isNameSession") == null))) {
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
         
           model.addAttribute("userimage",session.getAttribute("userimage").toString());
            return "/home/home";
        }
        model.addAttribute("userimage",session.getAttribute("userimage").toString());

        return "/Login-Register/login";
    }
    
    @GetMapping("/voucher/{phantram}")
    public String voucher(Model model, @PathVariable int phantram) {
        System.out.println("123456789987654321");
        session.setAttribute("voucherGiam", phantram);
         h=1;
         phantramm = phantram;
        System.out.println(phantram);
        return "redirect:/home";
    }
    
    @GetMapping("/login")
    public String login(Model model,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("taikhoankhongco", null);
        model.addAttribute("matkhaukhongdung", null);
        if ((session.getAttribute("isNameSession") != null) && (cookieValue.equals("defaultCookieValue"))) {
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            
            model.addAttribute("isNameCookie", cookieValue);
            return "forward:/home";
        }
        if (!(cookieValue.equals("defaultCookieValue") && (session.getAttribute("isNameSession") == null))) {
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));

            model.addAttribute("isNameCookie", cookieValue);
            return "forward:/home";
        }
        if (session.getAttribute("isNameSession") == null) {
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("isNameCookie", cookieValue);

            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("isNameCookie", cookieValue);
            return "/Login-Register/login";
        }

        if ((cookieValue.equals("defaultCookieValue"))) {
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));

            model.addAttribute("dadangnhap", "okieValue");
            model.addAttribute("isNameCookie", cookieValue);
            return "/Login-Register/login";
        }
        return "";

    }

    @GetMapping("/loginn")
    public String loginn(Model model,@CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("isNameCookie", cookieValue);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
      
        User user = userRepository.findByUsername(name).orElseThrow();
        List<Role> list=user.getRole();
        System.out.println(list.get(0).getId());
        session.setAttribute("role",list.get(0).getId() );
        return "redirect:/home";
    }

    @PostMapping("/loginn")
    public String login(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        // sai tài khoản
        if (!userService.ktraTaiKhoan(username)) {
            model.addAttribute("taikhoankhongco", "Bạn đã đăng nhập thất bại");
            model.addAttribute("matkhaukhongdung", null);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("khongthem", null);
            model.addAttribute("them", null);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));

            return "/Login-Register/login";
        }
        // sai mật khẩu
        if (!userService.checkLogin(username, password)) {
            model.addAttribute("taikhoankhongco", null);
            model.addAttribute("matkhaukhongdung", "Mật khẩu không đúng");
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("khongthem", null);
            model.addAttribute("them", null);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));

        }
        if (userService.checkLogin(username, password)) {
            model.addAttribute("ok", "Bạn đã đăng nhập thành công");
            code = code();
            usermail = userRepository.findByUsername(username).orElseThrow();
            session.setAttribute("code", code);
            session.setAttribute("email", usermail.getEmail());
            model.addAttribute("gmail",usermail.getEmail());
            model.addAttribute("usernamee",username);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            this.start();
            session.setAttribute("tlxn", false );
            return "Login-Register/xacnhandn";
        } else {
            model.addAttribute("khongthanhcong", "Bạn đã đăng nhập thất bại");
            model.addAttribute("matkhaukhongdung", "Mật khẩu không đúng");
            System.out.println("Bạn đã đăng nhập thất bại taikhoankhongco");
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("khongthem", null);
            model.addAttribute("them", null);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));

            return "/Login-Register/login";
        }
    }

    public void addCookie(String name, String valuee) {
        Cookie newCookie = new Cookie(name, valuee);
        newCookie.setMaxAge(24 * 24 * 24);
        newCookie.setPath("/");
        response.addCookie(newCookie);
    }

    public void removeCookie(String name, String valuee) {
        Cookie newCookie = new Cookie(name, valuee);
        newCookie.setMaxAge(0);
        newCookie.setPath("/");
        response.addCookie(newCookie);
    }

    @GetMapping("/register")
    public String dangky(Model model,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue){
        model.addAttribute("user", new User());
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("isNameCookie", cookieValue);
        return "Login-Register/register";
    }

    @PostMapping("/dangky")
    public String saveUser(@RequestParam(name = "password2") String password2,
            @ModelAttribute("user") User user, Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        boolean isFix = false;

        if (!password2.equals(user.getPassword())) {
            model.addAttribute("khongtrungmatkhau", new String("Lỗi nhập không trùng mật khẩu!"));
            isFix = true;
            model.addAttribute("isfix", isFix);
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            return "Login-Register/register";
        }
        try {
        User usernew=userRepository.findByUsername(user.getUsername()).orElseThrow();
        if(usernew!=null) {
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("error", "Tài khoản đã tồn tại");
            model.addAttribute("user", user );
            return "Login-Register/register";
        }}
        catch (Exception e) {
            // TODO: handle exception
        }

        List<Role> lstrole=new ArrayList<>();
        Role role=roleRepository.findById(1L).orElseThrow();
        lstrole.add(role);
       
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole(lstrole);
        user.setImage("user.png");
      
        user.setStatus(false);
        userService.save(user);
        code = code();
        usermail = user;
        session.setAttribute("tlxn", true );
        this.start();
        session.setAttribute("code", code);
     
        session.setAttribute("email",  user.getEmail());
        model.addAttribute("gmail", user.getEmail());
       user.setImage("user.png");
        session.setAttribute("userimage","user.png ");
        session.setAttribute("user", user);
        model.addAttribute("gmail", session.getAttribute("email").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "Login-Register/xacnhan";
    }

    public void start() {

        t = new Thread(this);
        t.start();

    }

    @Override
    public void run() {

       System.out.println("luong ddax chay");
        emailService.sendSimpleEmail(usermail.getEmail(),

                "Xác thực email",
                "Xin chào quý khách rất vui được phu mụ quý khách . Mã xác nhận :" + code);

//        final Mail mail = new MailBuilder()
//                .From("letamxi87@gmail.com") // For gmail, this field is ignored.
//                .To(usermail.getEmail())
//                .Template("mail-template.html")
//                .AddContext("subject", "Test Email")
//                .AddContext("content", "Hello World!")
//                .Subject("Hello")
//                .createMail();
//        try {
//            emailServices.sendHTMLEmail(mail);
//        } catch (MessagingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        t.stop();
        t = null;

    }
    @GetMapping("/logins")
    public String loginsucces() {
        session.setAttribute("tlxn", false );
        return "redirect:/xacnhan";
        
    }
    @GetMapping("/loginfail" )
    public String loginfail(Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
       model.addAttribute("error", "Username or password incorrect");
      
       model.addAttribute("isNameCookie", cookieValue);
       model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
       return "Login-Register/login";
        
    }
    public int code() {
        int code=0;
        while (code<999|| code>9999) {
            code = (int) Math.floor(((Math.random() * 10000)));
        }
        System.out.println("Code ddd "+ code);
        return code;
    }
    @GetMapping("/changepassword" )
    public String changepassword(Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
       
      model.addAttribute("user", new User());
       model.addAttribute("isNameCookie", cookieValue);
     
       model.addAttribute("userimage",session.getAttribute("userimage").toString());
       model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
       return "Login-Register/changepassword";
        
    }
    @PostMapping("/changepassword" )
    public String changepasswordz(@RequestParam("rpasswordnew") String rpasswordnew ,@RequestParam("passwordnew") String passwordnew,User user ,Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User usernew = userRepository.findByUsername(name).orElseThrow();
        if (!bcryptEncoder.matches(user.getPassword(),usernew.getPassword())) {
            model.addAttribute("loi","Mật khẩu cũ không đúng ");
        }
        else {
        if (!rpasswordnew.equals(passwordnew)) {
            model.addAttribute("loi","Mật khẩu mới nhập lại không trùng ");
        }
        else {
            String text="";
            if (!passwordnew.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}")) {
                model.addAttribute("loi", "Mật khểu phải gồm : ít nhất 1 số,1 kí tự hoa,1 kí tự thường và 1 kí tự đặt biệt");
            }
            else {
               usernew.setPassword(bcryptEncoder.encode(passwordnew));
               userRepository.save(usernew);
               session.setAttribute("ok","Thay đổi password thành công");
               return "redirect:/home";
            }
            
        }
        }
        System.out.println("hehe"+passwordnew.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}"));
       model.addAttribute("isNameCookie", cookieValue);
       model.addAttribute("password", user.getPassword());
       model.addAttribute("rpasswordnew", rpasswordnew);
       model.addAttribute("passwordnew", passwordnew);
       model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
       model.addAttribute("userimage",session.getAttribute("userimage").toString());
       return "Login-Register/changepassword";
        
    }
    @GetMapping("/xacnhan")
    public String guilai(Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        User user=new User();
        try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        code =code();
        user = userRepository.findByUsername(name).orElseThrow();
        usermail = user;}
        catch (Exception e) {
            usermail=(User)session.getAttribute("user");
        }
        this.start();
        session.setAttribute("code", code);
        session.setAttribute("email", usermail.getEmail());
        System.out.println(session.getAttribute("email").toString());
        model.addAttribute("gmail", session.getAttribute("email").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        
        if ((Boolean)session.getAttribute("tlxn")==true)
        return "Login-Register/xacnhan";
        else
            return "Login-Register/xacnhandn";
            
    }

    @PostMapping("/xacnhan")
    public String xacnhan(Model model, @RequestParam(name = "code") int code, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
       
        if (code == session.getAttribute("code").hashCode()) {
            User user=(User)session.getAttribute("user");
           
           user.setImage("image.png");
            user.setStatus(true);
            userService.save(user); 
            return "redirect:/login";
         
        }

        if (session.getAttribute("code").hashCode()==0)
            model.addAttribute("error", "Quá thời gian nhập mã OTP");
        else
         model.addAttribute("error", "Mã OTP không chính xác");
        session.setAttribute("email", session.getAttribute("email").toString());
        model.addAttribute("gmail", session.getAttribute("email").toString());
        model.addAttribute("gmail", session.getAttribute("email").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "Login-Register/xacnhan";
    }
    
    
    
    @PostMapping("/xacnhandn")
    public String xacnhandn(Model model, @RequestParam(name = "code") int code ,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue ) {
       
        System.out.println(code);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
  
       
       
        if (code == session.getAttribute("code").hashCode()) {
                System.out.println(username+"21111111111111111111111111111111111111111111111111111");
            var luumatkhau = request.getParameter("luuMatKhauu");
            if (luumatkhau != null && luumatkhau.equals("luuMatkhau")) {
                System.out.println("cookies login post" + session.getAttribute("isNameCookie"));
                addCookie("isNameCookie", username);
                model.addAttribute("isNameCookie", username);
                session.removeAttribute("isNameSession");
                User userr = userRepository.findByUsername(username).orElseThrow();
                model.addAttribute("quyentruycap", userr.getRole());
                System.out.println("Quyen " + userr.getRole());
                model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
                model.addAttribute("dangnhapthanhcong", "bandadangnhapthanhcong");
               
                User user = userRepository.findByUsername(username).orElseThrow();
                List<Role> list=user.getRole();
                System.out.println(list.get(0).getId());
                session.setAttribute("role",list.get(0).getId() );
                return "redirect:/home";
            } else {
                session.setAttribute("isNameSession", username);
                System.out.println("session login post" + session.getAttribute("isName"));
                removeCookie("isNameCookie", "n");
                model.addAttribute("isName", session.getAttribute("isName"));
                model.addAttribute("dangnhapthanhcong", "bandangnhapthanhcong");
                model.addAttribute("dangnhapthanhcong", "bandangnhapthanhcong");
                User userr = userRepository.findByUsername(username).orElseThrow();
                model.addAttribute("quyentruycap", userr.getRole());
                System.out.println("Quyen " + userr.getRole());
                model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
                User user = userRepository.findByUsername(username).orElseThrow();
                List<Role> list=user.getRole();
                System.out.println(list.get(0).getId());
                session.setAttribute("role",list.get(0).getId() );
                model.addAttribute("isNameCookie", cookieValue);
                return "redirect:/home";

            }                                         
        }

        if (session.getAttribute("code").hashCode()==0)
            model.addAttribute("error", "Quá thời gian nhập mã OTP");
        else
            model.addAttribute("error", "Mã OTP không chính xác");
        session.setAttribute("email", session.getAttribute("email").toString());
        model.addAttribute("gmail", session.getAttribute("email").toString());
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        return "Login-Register/xacnhandn";
    }
    
    
    @GetMapping("/timeout")
    public @ResponseBody int timeout(HttpServletRequest request) {
       code=0;
       session.setAttribute("code", code);
        return code;
    }
    


    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {

        if (principal != null) {
            org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage",session.getAttribute("userimage").toString());
        return "/home/home";
    }


    @GetMapping("/logouts")
    public String logout(Model model, HttpSession session,
            @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        session.removeAttribute("isNameSession");
        removeCookie("isNameCookie", "n");
        model.addAttribute("isNameCookie", "defaultCookieValue");
        model.addAttribute("isNameSession", null);
        session.removeAttribute("isNameSession");
        return "Login-Register/login";
    }

}

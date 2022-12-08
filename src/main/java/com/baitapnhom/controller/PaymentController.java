package com.baitapnhom.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baitapnhom.config.MailBuilder;
import com.baitapnhom.config.PaypalPaymentIntent;
import com.baitapnhom.config.PaypalPaymentMethod;
import com.baitapnhom.config.VnpayConfig;
import com.baitapnhom.entity.HoaDon;
import com.baitapnhom.entity.Mail;
import com.baitapnhom.entity.PaymentMethod;
import com.baitapnhom.entity.SanPham;
import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.HoaDonRepository;
import com.baitapnhom.renpository.SanPhamRepository;
import com.baitapnhom.renpository.UserRepository;
import com.baitapnhom.service.EmailServices;
import com.baitapnhom.service.HoaDonService;
import com.baitapnhom.service.PaypalService;
import com.baitapnhom.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController extends Thread {
    public static final String URL_PAYPAL_SUCCESS = "card/success";
    public static final String URL_PAYPAL_CANCEL = "card/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());
    private Long Id;
    private double VND, USD;
    String Mang[] = new String[100];
    int doDai;
    
    @Autowired
    private  EmailServices emailServices;
    Thread t;
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private HttpSession session;
    

    @GetMapping("/phuongthucthanhtoan")
    public String phuongthucthanhtoan(Model model,HttpSession  session) {
        model.addAttribute("payment", new PaymentMethod());
        System.out.println("fdffdfd"+session.getAttribute("userimage").toString());
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
       
        return "/card/card_phuongthucthanhtoan";
    }
    
    @PostMapping("/card_phuongthucthanhtoan")
    public String card_phuongthucthanhtoan(Model model, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) {
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        model.addAttribute("payment", new PaymentMethod());
        
        float dola = 24867;     //Số liệu 11/11/2022: 1 USD = 24867 VND
        
        int phiVanChuyen=0;
        float tong = Float.parseFloat(session.getAttribute("tong").toString());
        int tong2 = (int) (tong*dola);
        DecimalFormat f = new DecimalFormat("0.000");
        DecimalFormat f2 = new DecimalFormat("0.00");
        
        if(tong2<=10000) phiVanChuyen=2;
        else if(tong2>10000 && tong <500000) phiVanChuyen=1;
        else if(tong2>=500000) phiVanChuyen=0;
        int phiVanChuyen2 = (int) (phiVanChuyen*dola);
        
        model.addAttribute("tong", tong2);
        model.addAttribute("tong2", f.format(tong));
        model.addAttribute("phiVanChuyen2", f.format(phiVanChuyen));
        model.addAttribute("phiVanChuyen", phiVanChuyen2);
        
        int voucher=0;
        if(session.getAttribute("voucherGiam")==null)
            model.addAttribute("voucher", 0);
        else {
            model.addAttribute("voucher", session.getAttribute("voucherGiam"));
            voucher = (int) session.getAttribute("voucherGiam");
        }
        
        float h = (tong + phiVanChuyen)-(float) (voucher/100.0)*(tong + phiVanChuyen);    
        float k = h*dola;
        
        VND = (double) k;
        USD = h;
        
        model.addAttribute("total", (int) VND);
        model.addAttribute("totalUSD", f2.format(h));
        model.addAttribute("payment", new PaymentMethod());
        return "/card/card_phuongthucthanhtoan";
    }
    
    private void ThaoTacHoaDon(int check) {
        for(int i=0;i<doDai;i++) {
            System.out.println("<><><><><><><><><><><><><><><><><><><><><><> "+ check);
            Id = Long.parseLong(Mang[i]);
            HoaDon hoaDon=hoaDonRepository.findById(Id).orElseThrow();
            if(check == 1) hoaDonRepository.delete(hoaDon);
            if(check == 2) {
                hoaDon.setWasPay(true);
                hoaDonRepository.save(hoaDon);
                
                List<SanPham> litsp=(List<SanPham>)session.getAttribute("card");
                for(SanPham sanpham : litsp) {
                    SanPham sanphamnew=sanPhamRepository.findById(sanpham.getId()).orElseThrow();
                    sanphamnew.setSoluong(sanphamnew.getSoluong()-sanpham.getSoluong());
                    
                    sanPhamRepository.save(sanphamnew);
                }
            }
        }
        
        doDai=0;
        Mang = new String[100];
    }
    
    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue){
        
//        ThaoTacHoaDon(1);
        
        model.addAttribute("isNameCookie", cookieValue);
        model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
        model.addAttribute("userimage", session.getAttribute("userimage").toString());
        return "/card/cancel";
    }
    
    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, Model model, @RequestParam("PayerID") String payerId, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                
                ThaoTacHoaDon(2);
                
//              HttpSession session = request.
                List<SanPham> listnew=new ArrayList<>();
                session.setAttribute("soluong", null);
                session.setAttribute("card", listnew);
                session.removeAttribute("tong");
                model.addAttribute("isNameCookie", cookieValue);
                model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
                model.addAttribute("userimage", session.getAttribute("userimage").toString());
                return "card/successPaypal";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
    
    @PostMapping("/pay")
    public String pay(Model model,@ModelAttribute PaymentMethod paymentMethod, HttpSession session, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue,HttpServletRequest request) throws UnsupportedEncodingException{
//        System.out.println("12345s4ad654s65d = " + paymentMethod.getPaymentMethodEnum().getDisplayValue());
        int i=0;
        List<SanPham> listnew=new ArrayList<>();
     // System.out.println("12345s4ad654s65d = " + paymentMethod.getPaymentMethodEnum().getDisplayValue());
        LocalDate date = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        String timeString = calendar.getTime().toString();
        String[] words=timeString.split("\\s");
        String Username;
      if (!cookieValue.equals("defaultCookieValue"))
          Username=cookieValue;
      else
          Username=session.getAttribute("isNameSession").toString();
      User user=userRepository.findByUsername(Username).orElseThrow();
     
        String dateTime = date.toString() + " " + words[3];
        
        String pttt = paymentMethod.getPaymentMethodEnum().getDisplayValue();
        
        if(paymentMethod.getPaymentMethodEnum().getDisplayValue() == "Paypal") {
            String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
            String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
            
//            System.out.println(cancelUrl + "---000---" + successUrl);
            int o=0;
            List<SanPham> litsp=(List<SanPham>)session.getAttribute("card");
            
            for(SanPham sanpham : litsp) {
                HoaDon hoaDon = new HoaDon(dateTime, USD*24867, pttt, false);
                
                hoaDon.setSoluong(sanpham.getSoluong());
                hoaDon.setGia(sanpham.getGia());
                hoaDon.setStatus(false);

                SanPham sanphamnew=sanPhamRepository.findById(sanpham.getId()).orElseThrow();
                hoaDon.setSanpham_id(sanphamnew);
                
                //gán user
                hoaDon.setUser(user);
                hoaDonRepository.save(hoaDon);
//                System.out.println("========>>>>> "+hoaDon.getId().toString() + " - " + o);
                Mang[o]= hoaDon.getId().toString();
                o++;
//                sanPhamRepository.save(sanphamnew);
            }
            doDai=o;
            this.start();
            
            try {
                Payment payment = paypalService.createPayment(
                        (double) USD,
                        "USD",
                        PaypalPaymentMethod.paypal,
                        PaypalPaymentIntent.sale,
                        "payment description",
                        cancelUrl,
                        successUrl);
                for(Links links : payment.getLinks()){
                    if(links.getRel().equals("approval_url")){
                        return "redirect:" + links.getHref();
                    }
                }
            } catch (PayPalRESTException e) {
                log.error(e.getMessage());
            }
            return "redirect:/";
        }
        else
            if(paymentMethod.getPaymentMethodEnum().getDisplayValue() == "Vnpay") {
                String vnp_Version = "2.1.0";
                String vnp_Command = "pay";
                String vnp_OrderInfo = "Thanh Toan Don Hang";
                String orderType = "topup";
                String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
                String vnp_IpAddr = VnpayConfig.getIpAddress(request);
                String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

                int amount = (int) (VND * 100);
                System.out.println(amount);
                Map vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_Version", vnp_Version);
                vnp_Params.put("vnp_Command", vnp_Command);
                vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                vnp_Params.put("vnp_Amount", String.valueOf(amount));
                vnp_Params.put("vnp_CurrCode", "VND");
                
                String bank_code = "";
                if (bank_code != null && !bank_code.isEmpty()) {
                    vnp_Params.put("vnp_BankCode", bank_code);
                }
                
                vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
                vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
                vnp_Params.put("vnp_OrderType", orderType);

                String locate = "vn";
                vnp_Params.put("vnp_Locale", "vn");
                
                vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
                vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
                Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String vnp_CreateDate = formatter.format(cld.getTime());

                vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
                cld.add(Calendar.MINUTE, 15);
                String vnp_ExpireDate = formatter.format(cld.getTime());
                //Add Params of 2.1.0 Version
                vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
                
                //Build data to hash and querystring
                List fieldNames = new ArrayList(vnp_Params.keySet());
                Collections.sort(fieldNames);
                StringBuilder hashData = new StringBuilder();
                StringBuilder query = new StringBuilder();
                Iterator itr = fieldNames.iterator();
                while (itr.hasNext()) {
                    String fieldName = (String) itr.next();
                    String fieldValue = (String) vnp_Params.get(fieldName);
                    if ((fieldValue != null) && (fieldValue.length() > 0)) {
                        //Build hash data
                        hashData.append(fieldName);
                        hashData.append('=');
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        //Build query
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append('=');
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        if (itr.hasNext()) {
                            query.append('&');
                            hashData.append('&');
                        }
                    }
                }
                String queryUrl = query.toString();
                String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
                queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;    
                String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
                
                System.out.println(paymentUrl);
                
                int o=0;
                List<SanPham> litsp=(List<SanPham>)session.getAttribute("card");
                for(SanPham sanpham : litsp) {
                    HoaDon hoaDon = new HoaDon(dateTime, USD*24867, pttt, false);
                    
                    hoaDon.setSoluong(sanpham.getSoluong());
                    hoaDon.setGia(sanpham.getGia());
                    hoaDon.setStatus(false);
                    
                    SanPham sanphamnew=sanPhamRepository.findById(sanpham.getId()).orElseThrow();
                    hoaDon.setSanpham_id(sanphamnew);
                    
                    //gán user
                    hoaDon.setUser(user);
                    hoaDonRepository.save(hoaDon);
//                    System.out.println("========>>>>> "+hoaDon.getId().toString() + " - " + o);
                    Mang[o]= hoaDon.getId().toString();
                    o++;
//                    sanPhamRepository.save(sanphamnew);
                }
                doDai=o;
                
                return "redirect:" + paymentUrl;
            }
        else {
           // this.start();
            List<SanPham> litsp=(List<SanPham>)session.getAttribute("card");
           
            for(SanPham sanpham : litsp) {
                HoaDon hoaDon = new HoaDon(dateTime, VND, pttt, true);
                SanPham sanphamnew=sanPhamRepository.findById(sanpham.getId()).orElseThrow();
                hoaDon.setSanpham_id(sanphamnew);
                sanphamnew.setSoluong(sanphamnew.getSoluong()-sanpham.getSoluong());
//                System.out.println(sanphamnew.getSoluong()+"   "+sanpham.getSoluong());
                hoaDon.setSoluong(sanpham.getSoluong());
                hoaDon.setGia(sanpham.getGia());
                hoaDon.setStatus(false);
                //gán user
                hoaDon.setUser(user);
                sanPhamRepository.save(sanphamnew);
                hoaDonRepository.save(hoaDon);
            }
            
            session.setAttribute("soluong", null);
            session.setAttribute("card", listnew);
            session.removeAttribute("tong");
            model.addAttribute("isNameCookie", cookieValue);
            model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
            model.addAttribute("userimage", session.getAttribute("userimage").toString());
            return "/card/success";
        }
    }
    
    @GetMapping("/returnPage")
    public String returnPage(HttpServletRequest request, Model model, @CookieValue(value = "isNameCookie", defaultValue = "defaultCookieValue") String cookieValue) throws ServletException, IOException{
        System.out.println("Đã vào returnPage thành công!");
        try
        {
            Map fields = new HashMap();
             for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
    
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) 
            {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) 
            {
                fields.remove("vnp_SecureHash");
            }
            
            // Check checksum
            String signValue = VnpayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) 
            {
                boolean checkOrderId = true; // vnp_TxnRef exists in your database
                boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
                boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)
                
                if(checkOrderId)
                {
                    if(checkAmount)
                    {
                        if (checkOrderStatus)
                        {
                            if ("00".equals(request.getParameter("vnp_ResponseCode")))
                            {
                                //Here Code update PaymnentStatus = 1 into your Database
                                System.out.println("Đã vào 1, giao dịch thành công!");
                                
                                ThaoTacHoaDon(2);
                                
                                
                                
                                List<SanPham> listnew=new ArrayList<>();
                                session.setAttribute("soluong", null);
                                session.setAttribute("card", listnew);
                                session.removeAttribute("tong");
                                model.addAttribute("isNameCookie", cookieValue);
                                model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
                                model.addAttribute("userimage", session.getAttribute("userimage").toString());
                                return "/card/successVnpay";
                            }
                            else
                            {
                                // Here Code update PaymnentStatus = 2 into your Database
                                System.out.println("Đã vào 2, giao dịch thất bại!");
//                                ThaoTacHoaDon(1);
                                
                                model.addAttribute("isNameCookie", cookieValue);
                                model.addAttribute("isNameSession", session.getAttribute("isNameSession"));
                                model.addAttribute("userimage", session.getAttribute("userimage").toString());
                                return "/card/cancel";
                            }
                        }
                        else
                        {
                            
                            System.out.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                        }
                    }
                    else
                    {
                        System.out.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}"); 
                    }
                }
                else
                {
                    System.out.print("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
                }
            } 
            else 
            {
                System.out.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
            }
        }
        catch(Exception e)
        {
            System.out.print("{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}");
        }
        
        return "/card/cancel";
    }
    
    public void start() {

        t = new Thread(this);
        t.start();

    }
    
    @Override
    public void run() {

   System.out.println("Luoongf ddang chay");
        final Mail mail = new MailBuilder()
                .From("letamxi87@gmail.com") // For gmail, this field is ignored.
//                .To("thanhb1910139@student.ctu.edu.vn")
                .To("thienanhhung258@gmail.com")
                .Template("email/mail-hoadon.html")
                .AddContext("subject", "Test Email")
                .AddContext("content","noi dung")
                .Subject("Hello")
                .createMail();
        try {
            emailServices.sendHTMLEmail(mail);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        t.stop();
        t = null;

    }
    
}

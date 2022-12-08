package com.baitapnhom.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorControllers implements ErrorController {

      @RequestMapping("/error")
      public String error() {
          return "/error" ;
      }

}


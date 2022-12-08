package com.baitapnhom.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.baitapnhom.controller.GlobalExceptionHandlers;

@ControllerAdvice
public class GlobalExceptionHandlesr {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlers.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final Throwable throwable){
        logger.error("Exception during execution of SpringSecurity application", throwable);

        ModelAndView modelAndView = new ModelAndView("/error");
        String errorMessage = (throwable != null)? throwable.toString() : "Unknown error";
        modelAndView.addObject("errorMessage", errorMessage);
        return  modelAndView;

    }
}

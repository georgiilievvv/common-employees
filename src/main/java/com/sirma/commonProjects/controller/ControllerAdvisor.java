package com.sirma.commonProjects.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalStateException.class,
            IllegalArgumentException.class,
            NumberFormatException.class,
            DateTimeParseException.class})
    public String handleCustomMessageExceptions(RuntimeException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "index";
    }
}


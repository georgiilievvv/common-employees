package com.sirma.commonProjects.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;

/**
 * Controller class for handling exceptions and passing the error message as a model attribute.
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private static final String INCORRECT_DATA_PROVIDED_ERROR_MESSAGE = "Incorrect data provided. %s";
    private static final String MISSING_DATA_ERROR_MESSAGE = "Missing data for particular record. " +
            "Row data should be as follows (EmployeeId, ProjectId, StartDate, EndDate)";
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalStateException.class,
            IllegalArgumentException.class})
    public String illegalSateAndArgumentsExceptionHandler(RuntimeException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "index";
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ArrayIndexOutOfBoundsException.class})
    public String missingDataExceptionHandler(RuntimeException exception, Model model) {
        model.addAttribute("message", MISSING_DATA_ERROR_MESSAGE);
        return "index";
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NumberFormatException.class,
            DateTimeParseException.class})
    public String inputDataExceptionHandler(RuntimeException exception, Model model) {
        String message = String.format(INCORRECT_DATA_PROVIDED_ERROR_MESSAGE, exception.getMessage());
        model.addAttribute("message", message);
        return "index";
    }
}


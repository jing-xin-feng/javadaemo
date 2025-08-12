package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e, Model model) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("参数验证失败: {}", errors);
        model.addAttribute("errors", errors);

        // 添加表单数据用于回显
        Object target = e.getBindingResult().getTarget();
        if (target != null) {
            model.addAttribute("user", target);
        }

        return "user/form";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException e, Model model) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("URL参数验证失败: {}", errorMsg);
        model.addAttribute("error", errorMsg);
        return "redirect:/users";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("系统异常完整堆栈:", e); // 添加此行打印完整堆栈
        model.addAttribute("errorMsg", "操作失败: " + e.getMessage());
        return "error/500";
    }
}
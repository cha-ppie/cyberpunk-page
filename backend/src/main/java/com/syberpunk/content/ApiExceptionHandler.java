package com.syberpunk.content;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail domainError(ResponseStatusException error) {
        return ProblemDetail.forStatusAndDetail(error.getStatusCode(), error.getReason());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationError(MethodArgumentNotValidException error) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "请检查必填项、字段长度和数值范围");
        detail.setProperty("fields", error.getBindingResult().getFieldErrors().stream().map(field -> field.getField()).distinct().toList());
        return detail;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail malformedRequest() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "请求格式不正确，请检查填写的内容");
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflict() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "该标识已存在，或资料的关联数据不完整");
    }
}

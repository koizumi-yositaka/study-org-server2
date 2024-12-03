package com.example.study_org_server.controller.advice;

import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.openapitools.example.model.ForbiddenError;
import org.openapitools.example.model.ResourceNotFoundError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, MeetingNotFoundException.class})
    public ResponseEntity<Object> handleProductNotFoundException(UserNotFoundException ex){
        var error= new ResourceNotFoundError();
        error.setTitle("ERROR");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex){
        var error= new ForbiddenError();
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    //ConstraintViolationException Errorの処理
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){
        var badRequestError= BadRequestErrorCreater.from(ex);
        return ResponseEntity.badRequest().body(badRequestError);
    }


    @Override
    // SpringBoot内で用意されている例外
    // Spring MVCの @Valid アノテーションを使ったバリデーションにおいて発生するエラー
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var error =BadRequestErrorCreater.from(ex);
        return ResponseEntity.badRequest().body(error);
    }



    //どこにもキャッチされなかった例外
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        return super.handleExceptionInternal(ex, "handleAllException", null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        //ログ出力
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

}

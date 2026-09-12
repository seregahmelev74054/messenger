package com.example.messenger.EXCEPTION;

import com.example.messenger.EXCEPTION.dto.MyErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<MyErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<MyErrorResponse> handleUserLogin(UserLoginException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<MyErrorResponse> handleUserDoesNotExist(UserDoesNotExistException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MyErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<MyErrorResponse> handleConversationNotFound(ConversationNotFoundException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<MyErrorResponse> handleMemberNotFound(MemberNotFoundException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<MyErrorResponse> handleMessageNotFound(MessageNotFoundException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableRequestException.class)
    public ResponseEntity<MyErrorResponse> handleUnprocessableRequest(
            UnprocessableRequestException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MyErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        MyErrorResponse error = new MyErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

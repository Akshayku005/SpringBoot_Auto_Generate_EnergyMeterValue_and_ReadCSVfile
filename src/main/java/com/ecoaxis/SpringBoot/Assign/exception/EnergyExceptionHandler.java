package com.ecoaxis.SpringBoot.Assign.exception;

import com.ecoaxis.SpringBoot.Assign.bean.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class EnergyExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        List<String> errorMessage = errorList.stream().map(objError -> objError.getDefaultMessage()).collect(Collectors.toList());
        ResponseDto responseDTO = new ResponseDto("Exception while processing Rest Request!", errorMessage);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        ResponseDto responseDTO = new ResponseDto("Exception while processing Rest Request!", "Format is incorrect!");
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ResponseDto responseDTO = new ResponseDto("Exception while processing Rest Request!", "Change request POST,PUT,GET,DELETE");
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleAddressBookCustomException(Exception exception) {
        ResponseDto responseDTO = new ResponseDto("Exception while processing Rest Request!!", exception.getMessage());
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}

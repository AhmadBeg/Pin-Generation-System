package com.sample.excercise.pingeneration.exception;


import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;


@ControllerAdvice
public class PinManagerExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(PinManagerExceptionHandler.class);

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> handleDataAccessException(DataAccessException e){
	logger.error("Issue in data access: ", e);
	PINManagerServiceResponse<PinGenerationResponseData> response = new PINManagerServiceResponse<PinGenerationResponseData>();
	PinGenerationResponseData pinResponse = new PinGenerationResponseData(null, null);
	response.setData(pinResponse);;
	response.setStatusCode(Constants.DATA_ACCESS_ISSUE);
	response.setStatusDescription(e.getMessage());
	return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler({ConstraintViolationException.class, 
	WrongInputFormatException.class,
	ValidationException.class,
	IllegalArgumentException.class,
	MethodArgumentNotValidException.class})
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> handleConstraintViolationException(Exception e) {
	logger.error("Wrong Input Format: ", e);
	PINManagerServiceResponse<PinGenerationResponseData> response = new PINManagerServiceResponse<PinGenerationResponseData>();
	PinGenerationResponseData pinResponse = new PinGenerationResponseData(null, null);
	response.setData(pinResponse);
	response.setStatusCode(Constants.WRONG_INPUT_FORMAT);
	response.setStatusDescription("Please check format and encoding of input data.");
	return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.BAD_REQUEST);
    
    }
    
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> handleRunTimeException(RuntimeException e) {
	logger.error("runtime exception: ", e);
	PINManagerServiceResponse<PinGenerationResponseData> response = new PINManagerServiceResponse<PinGenerationResponseData>();
	PinGenerationResponseData pinResponse = new PinGenerationResponseData(null, null);
	response.setData(pinResponse);
	response.setStatusCode(Constants.SERVER_ERROR);
	response.setStatusDescription(e.getMessage());
	return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}
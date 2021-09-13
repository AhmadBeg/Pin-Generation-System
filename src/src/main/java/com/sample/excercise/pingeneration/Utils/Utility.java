package com.sample.excercise.pingeneration.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;
import com.sample.excercise.pingeneration.exception.WrongInputFormatException;


public class Utility {
    private static final List<Integer> digits =
	    new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    
    public static String generatePin() {
	    Collections.shuffle(digits);
	    final StringBuilder sb = new StringBuilder(4);
	    for (Integer digit : digits.subList(0, 4)) {
	        sb.append(digit);
	    }
	    return sb.toString();
	}

    public static ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> returnServiceSuccessResponse(String pin, String msisdn, int responseCode) {
	PINManagerServiceResponse<PinGenerationResponseData> response = new PINManagerServiceResponse<PinGenerationResponseData>();
	PinGenerationResponseData pinResponse = new PinGenerationResponseData(pin, msisdn);
	response.setData(pinResponse);
	response.setStatusCode(responseCode);
	
	switch(responseCode) {
	   case Constants.PIN_GENERATION_SUCCESS:
		response.setStatusDescription("Pin is succesfully generated");
		return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.CREATED);
	   case Constants.PIN_VALIDATION_SUCCESS:
		response.setStatusDescription("Pin is succesfully validated");
		return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.OK);
	   case Constants.MORE_THAN_THREE_FAILED_ATTEMPTS:
	        response.setStatusDescription("More than 3 failed attempts. Please generate new PIN");
		return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.OK);
	   case Constants.NO_PIN_FOR_MSISDN_REISSUE_PIN:
	        response.setStatusDescription("No PIN issued for given MSISDN. Please generate PIN");
	        return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.OK);
	   case Constants.PIN_VALIDATION_FAILURE:
	       response.setStatusDescription("Wrong PIN for given MSISDN");
	       return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.OK); 
	   case Constants.THREE_PIN_ALEADY_EXISTS:
	       response.setStatusDescription("Three PINs already assigned to this msisdn");
	       return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response, HttpStatus.OK); 
	   default:
	       response.setStatusDescription("Success");
	       return new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response,HttpStatus.OK);
	}
    }
    
    public static String decode(String msisdn) {
	try {
	    return new String(Base64.decodeBase64(msisdn), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    throw new WrongInputFormatException("unsupported encoding in msisdn");
	}
    }

    public static ValidatePINModel decode(@Valid ValidatePINModel validatePINModel) {
	ValidatePINModel model = new ValidatePINModel();
	try {
	    String msisdn = new String(Base64.decodeBase64(validatePINModel.getMsisdn()), "UTF-8");
	    model.setMsisdn(msisdn);
	    String pin = new String(Base64.decodeBase64(validatePINModel.getPin()), "UTF-8");
	    model.setPin(pin);
	    
	    return model;
	} catch (UnsupportedEncodingException e) {
	    throw new WrongInputFormatException("unsupported encoding in msisdn");
	}
    
    }

    public static String encode(String savedPin) {
	return Base64.encodeBase64String(savedPin.getBytes());
    }
}

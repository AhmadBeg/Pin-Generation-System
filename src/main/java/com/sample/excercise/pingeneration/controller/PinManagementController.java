package com.sample.excercise.pingeneration.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sample.excercise.pingeneration.Utils.Utility;
import com.sample.excercise.pingeneration.dto.MSISDNModel;
import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;
import com.sample.excercise.pingeneration.service.PinGenerationService;

@Controller
public class PinManagementController {

    private static final Logger logger = LoggerFactory.getLogger(PinManagementController.class);
    
    @Autowired
    PinGenerationService pinGenerationService;
    
    /**
     * This API takes msisdn , generates pin . associates pin with msisdn for later verification and returns pin to the caller of API
     * @param MSISDNModel
     * 
     * All parameters would be in base 64 encoded format
     */
    @PostMapping("/generatePin")
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> generatePin(@RequestBody @Valid MSISDNModel msisdn) {
	logger.info("generatePin service called");
	return  pinGenerationService.savePinWithMSISDN(Utility.decode(msisdn.getMsisdn()));
    }

    /**
     * This API validates PIN for given MSISDN. All input should be in Base64 encoded format
     * @param validatePINModel
     * @return
     */
    @PostMapping("/validatePin")
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> validatePin(@RequestBody @Valid ValidatePINModel validatePINModel) {
	logger.info("validatePin service called");
	return pinGenerationService.checkFailedAttemptsAndValidatePin(Utility.decode(validatePINModel));
    }
}

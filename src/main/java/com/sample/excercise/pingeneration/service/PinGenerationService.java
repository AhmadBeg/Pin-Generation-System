/**
 * 
 */
package com.sample.excercise.pingeneration.service;

import org.springframework.http.ResponseEntity;

import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;

/**
 * @author Ahmad Beg
 * This interface takes care of generating 4 digit random PIN and saves in DB for the given MSISDN
 *
 */
public interface PinGenerationService {
    ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> savePinWithMSISDN(String msisdn);
    ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> checkFailedAttemptsAndValidatePin(ValidatePINModel validatePINModel);
}

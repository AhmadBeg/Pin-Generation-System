/**
 * 
 */
package com.sample.excercise.pingeneration.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sample.excercise.pingeneration.Utils.AsyncOperationsService;
import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.Utils.Utility;
import com.sample.excercise.pingeneration.dao.PinGenerationDao;
import com.sample.excercise.pingeneration.dto.DbRowsModel;
import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;
import com.sample.excercise.pingeneration.service.PinGenerationService;

/**
 * @author Ahmad Beg
 * 
 *   * This interface takes care of generating 4 digit random PIN and saves in DB for the given MSISDN

 *
 */
@Service
public class PinGenerationServiceImpl implements PinGenerationService {
    private static final Logger log = LoggerFactory.getLogger(PinGenerationServiceImpl.class);

    @Autowired
    PinGenerationDao pinGenerationDao;
    
    @Autowired
    AsyncOperationsService asyncOperationsService;
    
    /*
     * This method calls utility to generate PIN and calls DAO to save association of MSISDN with PIN
     */
    @Override
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> savePinWithMSISDN(String msisdn) {
	boolean areMoreThanAwaitingValidation  = checkIfMorethanThreeAwaitingValidation(msisdn);
	if(areMoreThanAwaitingValidation) {
	    return Utility.returnServiceSuccessResponse(null, Utility.encode(msisdn), Constants.THREE_PIN_ALEADY_EXISTS);
	}
	String pin = Utility.generatePin();
	log.info("generated pin {} for msisdn {}", pin, msisdn);
	String savedPin = pinGenerationDao.associateWithMsisdnAndreturnPin(msisdn, pin);
	return Utility.returnServiceSuccessResponse(Utility.encode(savedPin), Utility.encode(msisdn), Constants.PIN_GENERATION_SUCCESS);
    }
    
    private boolean checkIfMorethanThreeAwaitingValidation(String msisdn) {
	 int count = pinGenerationDao.fetchRowsCountForMsisdn(msisdn);
	 log.info("Count of rows for {} is {}", msisdn, count);
	 if(count >= 3) {
	     return true;
	 }else {
	     return false;
	 }
    }

    /*
     * This method takes msisdn and pin given in validate calls and check if ok, also increment failed attempts in async manner
     * 
     * it also takes care of removing DB rows if validation good that too will be in async manner.
     */
    @Override
    public ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> checkFailedAttemptsAndValidatePin(ValidatePINModel validatePINModel) {
	List<DbRowsModel> dbRows = pinGenerationDao.getAllRowsForGivenMsisdnAndPin(validatePINModel.getMsisdn());
	
	if(dbRows.isEmpty()) {
	    log.info("No PIN exists for {}", validatePINModel.getMsisdn());
	    return Utility.returnServiceSuccessResponse(null, Utility.encode(validatePINModel.getMsisdn()), Constants.NO_PIN_FOR_MSISDN_REISSUE_PIN);
	}
	
	for(DbRowsModel aRow: dbRows) {
	    if(aRow.getFailedAttempts() > 3){
		asyncOperationsService.removePINsForMSISDN(validatePINModel.getMsisdn());
		return Utility.returnServiceSuccessResponse(null, Utility.encode(validatePINModel.getMsisdn()), 
			Constants.MORE_THAN_THREE_FAILED_ATTEMPTS);
	    }
	    
	    if(aRow.getPin().equals(validatePINModel.getPin())) {
		asyncOperationsService.removePINsForMSISDN(validatePINModel.getMsisdn());
		return Utility.returnServiceSuccessResponse(Utility.encode(validatePINModel.getPin()), Utility.encode(validatePINModel.getMsisdn()), 
			Constants.PIN_VALIDATION_SUCCESS);
	    }
	}
	
	asyncOperationsService.incrementFailureAttempts(validatePINModel.getMsisdn());
	return Utility.returnServiceSuccessResponse(null, Utility.encode(validatePINModel.getMsisdn()), Constants.PIN_VALIDATION_FAILURE);
    }

}

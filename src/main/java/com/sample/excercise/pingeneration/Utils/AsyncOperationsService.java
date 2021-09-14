/**
 * 
 */
package com.sample.excercise.pingeneration.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.sample.excercise.pingeneration.dao.PinGenerationDao;

/**
 * @author Ahmad
 * 
 * This component takes all Async operations in the app.
 * Since Async Ops cant be called from within a class as it uses a proxy, if called from inside the same class, proxy would be bypassed and method would be called directly.
 *
 */
@Component
@Async
public class AsyncOperationsService {
    private static final Logger log = LoggerFactory.getLogger(AsyncOperationsService.class);
    
    @Autowired
    PinGenerationDao pinGenerationDao;
    
    public void removePINsForMSISDN(String msisdn) {
	log.info("Removing all PINs assigned to {}", msisdn);
	int rowsDeleted = pinGenerationDao.removeALLPINsAssignedToMSISDN(msisdn);
	if(rowsDeleted > 0) {
	    log.info("Succesfully removed {} PINs assigned to MSISDN {}", rowsDeleted, msisdn);
	}else {
	    log.info("No PINs found for {}", msisdn);
	}
	
    }

    public void incrementFailureAttempts(String msisdn) {
	log.info("Incrementing failed attempts for  {}", msisdn);
	pinGenerationDao.incrementFailureCount(msisdn);
	log.info("Successfully incremented count for {}", msisdn);
    }
}

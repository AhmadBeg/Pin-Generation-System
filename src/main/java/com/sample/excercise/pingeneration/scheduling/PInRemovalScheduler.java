/**
 * 
 */
package com.sample.excercise.pingeneration.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sample.excercise.pingeneration.dao.PinGenerationDao;

/**
 * @author Ahmad
 * 
 * This class takes care of removing PINs after an hour from database in the background
 *
 */
@Component
public class PInRemovalScheduler {
    private static final Logger log = LoggerFactory.getLogger(PInRemovalScheduler.class);
    
    @Autowired
    PinGenerationDao pinGenerationDao;
    
    /**
     * This method runs every interval which is mentioned in application.properties in fixed-delay.in.milliseconds
     */
    @Scheduled(fixedDelayString  = "${fixed-delay.in.milliseconds}")
    public void removePINsEachHours() {
	log.info("Starting Scheduler to remove DB rows after an hour");
	int rowsDeleted = pinGenerationDao.removeAnHourOldDBRows();
	log.info("Scheduler ran succesfully for this iteration and deleted {} rows", rowsDeleted);
    }


}

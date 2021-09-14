package com.sample.excercise.pingeneration.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.dao.PinGenerationDao;
import com.sample.excercise.pingeneration.dto.DbRowsModel;
import com.sample.excercise.pingeneration.dto.MSISDNAndPINRowMapper;


@Repository
public class PinGenerationDaoImpl implements PinGenerationDao {

    private static final Logger log = LoggerFactory.getLogger(PinGenerationDaoImpl.class);

    @Autowired
    JdbcTemplate pinJDBCTemplate;
    
    /**
     * This method takes care of saving msisdn with pin in DB so it can be looked into while verification of the pin.
     * This method returns pin and this will be returned to caller of API , not taking pin from service itself as it ensures association is saved in DB 
     * @param msisdn MSISDN given for generation of PIN
     * @param pin PIN generated from Utility
     * @return pin PIN after saving in DB with given msisdn
     */
    @Override
    public String associateWithMsisdnAndreturnPin(String msisdn, String pin) {
	log.info("associating msisdn {} with pin {}", msisdn, pin);
	Timestamp  currentTS=new Timestamp(new Date().getTime());
	this.pinJDBCTemplate.update(Constants.INSERT_PIN_WITH_MSISDN,msisdn, pin, 0, currentTS);
	log.info("associated succesfully msisdn {} with pin {}", msisdn, pin);
	return pin;
    }
    
    /**
     * This method fetches all pins from database assigned to a given msisdn
     * this data is being used to validate PIN and check failed attempts as well
     * @param msisdn MSISDN given while calling for PIN Validation
     * @return returns all DB rows containing MSISDN, PIN and FailedAttempts
     */
    @Override
    public List<DbRowsModel> getAllRowsForGivenMsisdnAndPin(String msisdn) {
	log.info("Validating PIN for MSISDN {}", msisdn);
	List<DbRowsModel> DBrowList = this.pinJDBCTemplate.query(Constants.FETCH_ALL_ROWS_FOR_MSISDN, new Object[]{msisdn}, new MSISDNAndPINRowMapper());
	return DBrowList;
    }
    
    /**
     * This method removes all PINs for a given MSISDN.
     * 
     */
    @Override
    public int removeALLPINsAssignedToMSISDN(String msisdn) {
	return this.pinJDBCTemplate.update(Constants.REMOVE_PINS_FOR_MSISDN, msisdn);
    }
    
    /**
     * This method increments count of failure if PIN is wrong
     */
    @Override
    public void incrementFailureCount(String msisdn) {
	log.info("Incrementing failure count for {}", msisdn);
	this.pinJDBCTemplate.update(Constants.INCREMENT_FAILURE_COUNT, msisdn);
    }

    @Override
    public int fetchRowsCountForMsisdn(String msisdn) {
	log.info("Checking if more than 3 rows exists for {}", msisdn);
	return this.pinJDBCTemplate.queryForObject(Constants.FETCH_ROWS_FOR_MSISDN, new Object[]{msisdn}, Integer.class);
	
    }

    @Override
    public int removeAnHourOldDBRows() {
	return this.pinJDBCTemplate.update(Constants.REMOVE_AN_HOUR_OLD_ROWS);
    }
}
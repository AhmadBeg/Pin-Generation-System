/**
 * 
 */
package com.sample.excercise.pingeneration.Utils;

/**
 * @author Ahmad Beg
 * 
 * This class stores all constants used in the application
 *
 */
public class Constants {
    public static final String INSERT_PIN_WITH_MSISDN = "INSERT INTO PIN_SCHEMA.PIN_MNGR (MSISDN, PIN, FAIL_ATMPT, UPDT_TSTP)"
    	+ "VALUES (?, ?, ?, ?)";
    public static final String FETCH_ALL_ROWS_FOR_MSISDN = "SELECT MSISDN, PIN, FAIL_ATMPT FROM PIN_SCHEMA.PIN_MNGR WHERE MSISDN = ?";
    public static final String REMOVE_PINS_FOR_MSISDN = "DELETE FROM PIN_SCHEMA.PIN_MNGR WHERE MSISDN = ?";
    public static final String INCREMENT_FAILURE_COUNT = "UPDATE PIN_SCHEMA.PIN_MNGR SET FAIL_ATMPT = (@cur_value := FAIL_ATMPT) + 1 "
    	+ "WHERE MSISDN = ?";
    public static final String FETCH_ROWS_FOR_MSISDN = "SELECT COUNT(*) FROM PIN_SCHEMA.PIN_MNGR WHERE MSISDN = ?";
    public static final String REMOVE_AN_HOUR_OLD_ROWS = "DELETE FROM PIN_SCHEMA.PIN_MNGR WHERE UPDT_TSTP < (NOW() - INTERVAL 1 HOUR)";
    
    public static final int MORE_THAN_THREE_FAILED_ATTEMPTS = 2;
    public static final int PIN_VALIDATION_SUCCESS = 1;
    public static final int PIN_VALIDATION_FAILURE = 3;
    public static final int NO_PIN_FOR_MSISDN_REISSUE_PIN = 4;
    public static final int DATA_ACCESS_ISSUE = 5;
    public static final int SERVER_ERROR = 6;
    public static final int PIN_GENERATION_SUCCESS = 7;
    public static final int THREE_PIN_ALEADY_EXISTS = 8;
    public static final int WRONG_INPUT_FORMAT = 9;
    
    
    
}

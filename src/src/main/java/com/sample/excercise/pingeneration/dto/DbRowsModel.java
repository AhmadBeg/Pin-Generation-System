/**
 * 
 */
package com.sample.excercise.pingeneration.dto;

/**
 * @author Ahmad
 * 
 * This class saves all data in DB for a given msisdn while validation of PIN.
 *
 */
public class DbRowsModel {
    String msisdn;
    String pin;
    int failedAttempts;
    
    
    public String getMsisdn() {
        return msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public int getFailedAttempts() {
        return failedAttempts;
    }
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
    
    /*
     * Dont serialize and print objects, its very expensive operation, just implement toString and provide object ref in logs
     */
    @Override
    public String toString() {
	return "DbRowsModel [msisdn=" + msisdn + ", pin=" + pin + ", failedAttempts=" + failedAttempts + "]";
    }
    
    
}

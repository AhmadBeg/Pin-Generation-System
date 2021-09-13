/**
 * 
 */
package com.sample.excercise.pingeneration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Ahmad
 *
 *This model class will retun response of Pin Generation API
 */

@JsonInclude(Include.NON_NULL)
public class PinGenerationResponseData {
    String pin;
    String msisdn;
    
    public PinGenerationResponseData(String pin, String mSISDN) {
	super();
	this.pin = pin;
	this.msisdn = mSISDN;
    }
    
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getMSISDN() {
        return msisdn;
    }
    public void setMSISDN(String mSISDN) {
        msisdn = mSISDN;
    }
}

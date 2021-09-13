/**
 * 
 */
package com.sample.excercise.pingeneration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.excercise.pingeneration.validation.EncodedFormat;

/**
 * @author Ahmad
 * 
 * This is DTO class which is used in Post calls in Controller and tranfers MSISDN and PIN
 *
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidatePINModel {
    
    @EncodedFormat
    String msisdn;
    
    @EncodedFormat
    String pin;
    
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
    
    
}

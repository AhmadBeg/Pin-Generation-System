/**
 * 
 */
package com.sample.excercise.pingeneration.dto;

import com.sample.excercise.pingeneration.validation.EncodedFormat;

/**
 * @author Ahmad
 *
 */
public class MSISDNModel {
    
    @EncodedFormat
    String msisdn;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    
}

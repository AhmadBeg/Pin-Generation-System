/**
 * 
 */
package com.sample.excercise.pingeneration.exception;

/**
 * @author Ahmad
 *
 *This Exception would be thrown if MSISDN format is wrong.
 */
public class WrongInputFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public WrongInputFormatException(String errorMessage) {
        super(errorMessage);
    }
}

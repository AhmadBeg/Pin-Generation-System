/**
 * 
 */
package com.sample.excercise.pingeneration.validation;

import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Ahmad 
 *
 */
public class EncodingAndFormatValidator implements ConstraintValidator<EncodedFormat, String>{
    
    private static final Logger logger = LoggerFactory.getLogger(EncodingAndFormatValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
	String decoded = null;
	if(!Base64.isBase64(value)) {
	    return false;
	}
	try {
	    decoded = new String(Base64.decodeBase64(value), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    logger.error("unsuppeorted encoding", e);
	    return false;
	}
	return decoded.matches("^[0-9]{4}$") || decoded.matches("^\\+[1-9]{1,3}\\-{0,1}[0-9]{3,10}$");
    }

}

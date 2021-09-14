/**
 * 
 */
package com.sample.excercise.pingeneration.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
/**
 * @author aubeg
 *
 */
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.dao.PinGenerationDao;
import com.sample.excercise.pingeneration.dto.MSISDNModel;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;

@SuppressWarnings("rawtypes")
public class PinManagerControllerIntegrationTest extends BaseIntegrationTest{

    @LocalServerPort
    private int port;

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    PinGenerationDao dao;	
    
    HttpHeaders headers = new HttpHeaders();
    
    MSISDNModel model;
    

    ValidatePINModel validatePinModel;
    
    @Before
    public void setUp() {
	model = new MSISDNModel();
	String msisdn = "KzM0OTUxNzg0NDMzMQ==";
	model.setMsisdn(msisdn);
	
	String decodedMsisdn = null;
	try {
	    decodedMsisdn = new String(Base64.decodeBase64(msisdn), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	dao.removeALLPINsAssignedToMSISDN(decodedMsisdn);
	
	validatePinModel = new ValidatePINModel();
	validatePinModel.setMsisdn(msisdn);
    }
    
    /**
     * This test calls generatePin API and test it
     */
    @Test
    public void testGenratePin_Success() {
	
	HttpEntity<MSISDNModel> entity = new HttpEntity<MSISDNModel>(model, headers);
	
	ResponseEntity<PINManagerServiceResponse> responseEntity = testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	assertEquals(Constants.PIN_GENERATION_SUCCESS, responseEntity.getBody().getStatusCode());

    }
    
    /**
     * This test calls generatePin API after 3 PIN already assinged to MSISDN and tests if new PIN is denied
     */
    @Test
    public void testGenratePin_MorethanThreeAwaitingValidation() {
		
	HttpEntity<MSISDNModel> entity = new HttpEntity<MSISDNModel>(model, headers);
	testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	ResponseEntity<PINManagerServiceResponse> responseEntity = testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);

	assertEquals(Constants.THREE_PIN_ALEADY_EXISTS, responseEntity.getBody().getStatusCode());

    }
    
    /**
     * This test geenrates PIN and then calls validatePIN API
     */
    @Test
    public void testValidatePin() {
	HttpEntity<MSISDNModel> entity = new HttpEntity<MSISDNModel>(model, headers);

	ResponseEntity<PINManagerServiceResponse> responseEntity = testRestTemplate
		.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) responseEntity.getBody().getData();
	String generatedPin = data.get("pin");
	
	validatePinModel.setPin(generatedPin);	
	HttpEntity<ValidatePINModel> entity1 = new HttpEntity<ValidatePINModel>(validatePinModel, headers);
	ResponseEntity<PINManagerServiceResponse> responseEntity1 = testRestTemplate
		.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);

	assertEquals(Constants.PIN_VALIDATION_SUCCESS, responseEntity1.getBody().getStatusCode());

    }
    
    /**
     * This test checks if validations failure in case of wrong PIN
     */
    @Test
    public void testValidatePin_FailedAttempt() {
	HttpEntity<MSISDNModel> entity = new HttpEntity<MSISDNModel>(model, headers);
	testRestTemplate
	.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	
	String wrongPin = "MDAwMA==";  // this base64 encoding of "0000" which would never be a PIN
	validatePinModel.setPin(wrongPin);	
	HttpEntity<ValidatePINModel> entity1 = new HttpEntity<ValidatePINModel>(validatePinModel, headers);
	ResponseEntity<PINManagerServiceResponse> responseEntity1 = testRestTemplate
		.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);

	assertEquals(Constants.PIN_VALIDATION_FAILURE, responseEntity1.getBody().getStatusCode());

    }
    
    /**
     * This Test checks if in case of more than 3 attempts PIN is removed and called is asked to generate new PIN
     */
    @Test
    public void testValidatePin_MoreThanThreeFailedAttempt() {
	HttpEntity<MSISDNModel> entity = new HttpEntity<MSISDNModel>(model, headers);
	testRestTemplate
	.exchange(createURLWithPort("/generatePin"), HttpMethod.POST, entity, PINManagerServiceResponse.class);
	
	String wrongPin = "MDAwMA==";
	validatePinModel.setPin(wrongPin);	
	HttpEntity<ValidatePINModel> entity1 = new HttpEntity<ValidatePINModel>(validatePinModel, headers);
	testRestTemplate
	.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);
	testRestTemplate
	.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);
	testRestTemplate
	.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);
	testRestTemplate
	.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);
	// Looks like Junit paraellaly executing above rest calls, so putting wait of 3 sec to consolidate all threads
	try {
	    Thread.sleep(3000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	ResponseEntity<PINManagerServiceResponse> responseEntity1 = testRestTemplate
		.exchange(createURLWithPort("/validatePin"), HttpMethod.POST, entity1, PINManagerServiceResponse.class);

	assertEquals(Constants.MORE_THAN_THREE_FAILED_ATTEMPTS, responseEntity1.getBody().getStatusCode());
    }
    
    private String createURLWithPort(String uri) {
	return "http://localhost:" + port + uri;
    }
}

/**
 * 
 */
package com.sample.excercise.pingeneration.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.sample.excercise.pingeneration.Utils.AsyncOperationsService;
import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.dao.impl.PinGenerationDaoImpl;
import com.sample.excercise.pingeneration.dto.DbRowsModel;
import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;

/**
 * @author Ahmad
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PinGenerationServiceImplTest {
    
    @MockBean
    private PinGenerationDaoImpl pinDao;
    
    @MockBean
    AsyncOperationsService asyncOperationsService;
    
    String msisdn = "+1234456789";
    String pin = "1234";
    
    PinGenerationServiceImpl service;
    
    ValidatePINModel validatePINModel;
    
    @Before
    public void setUp() {
	service = new PinGenerationServiceImpl();
	ReflectionTestUtils.setField(service, "pinGenerationDao", pinDao);
	ReflectionTestUtils.setField(service, "asyncOperationsService", asyncOperationsService);
	validatePINModel = new ValidatePINModel();
	validatePINModel.setMsisdn(msisdn);
	validatePINModel.setPin(pin);
    }
    
    @Test
    public void shouldSavePinWithMSISDN_LessThanThree() {
	Mockito.when(pinDao.fetchRowsCountForMsisdn(msisdn)).thenReturn(0);
	Mockito.when(pinDao.associateWithMsisdnAndreturnPin(Mockito.anyString(), Mockito.anyString())).thenReturn("1234");
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.savePinWithMSISDN(msisdn);
	assertEquals("MTIzNA==", response.getBody().getData().getPin());
    }
    
    @Test
    public void shouldSavePinWithMSISDN_MoreThanThree() {
	Mockito.when(pinDao.fetchRowsCountForMsisdn(msisdn)).thenReturn(3);
	Mockito.when(pinDao.associateWithMsisdnAndreturnPin(Mockito.anyString(), Mockito.anyString())).thenReturn("1234");
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.savePinWithMSISDN(msisdn);
	assertNull(response.getBody().getData().getPin());
	assertEquals(Constants.THREE_PIN_ALEADY_EXISTS, response.getBody().getStatusCode());
    }
    
    @Test
    public void shouldCheckFailedAttemptsAndValidatePin_IsEmpty() {
	List<DbRowsModel> rows = new ArrayList<>();
	Mockito.when(pinDao.getAllRowsForGivenMsisdnAndPin(Mockito.anyString())).thenReturn(rows);
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.checkFailedAttemptsAndValidatePin(validatePINModel);
	assertNull(response.getBody().getData().getPin());
	assertEquals(Constants.NO_PIN_FOR_MSISDN_REISSUE_PIN, response.getBody().getStatusCode());
    }
    
    @Test
    public void shouldCheckFailedAttemptsAndValidatePin_MoreThanThreeFailedAttempts() {
	List<DbRowsModel> rows = new ArrayList<>();
	DbRowsModel rowModel = new DbRowsModel();
	rowModel.setFailedAttempts(4);
	rows.add(rowModel);
	
	Mockito.when(pinDao.getAllRowsForGivenMsisdnAndPin(Mockito.anyString())).thenReturn(rows);
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.checkFailedAttemptsAndValidatePin(validatePINModel);
	assertNull(response.getBody().getData().getPin());
	assertEquals(Constants.MORE_THAN_THREE_FAILED_ATTEMPTS, response.getBody().getStatusCode());
    }
    
    @Test
    public void shouldCheckFailedAttemptsAndValidatePin_PIN_ValidationSuccess() {
	List<DbRowsModel> rows = new ArrayList<>();
	DbRowsModel rowModel = new DbRowsModel();
	rowModel.setFailedAttempts(0);
	rowModel.setPin(pin);
	rowModel.setMsisdn(msisdn);
	rows.add(rowModel);
	
	Mockito.when(pinDao.getAllRowsForGivenMsisdnAndPin(Mockito.anyString())).thenReturn(rows);
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.checkFailedAttemptsAndValidatePin(validatePINModel);
	assertEquals(Constants.PIN_VALIDATION_SUCCESS, response.getBody().getStatusCode());
    }
    
    @Test
    public void shouldCheckFailedAttemptsAndValidatePin_PINValidationFailure() {
	List<DbRowsModel> rows = new ArrayList<>();
	DbRowsModel rowModel = new DbRowsModel();
	rowModel.setFailedAttempts(0);
	rowModel.setPin("5678");
	rowModel.setMsisdn(msisdn);
	rows.add(rowModel);
	
	Mockito.when(pinDao.getAllRowsForGivenMsisdnAndPin(Mockito.anyString())).thenReturn(rows);
	ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>> response = service.checkFailedAttemptsAndValidatePin(validatePINModel);
	assertEquals(Constants.PIN_VALIDATION_FAILURE, response.getBody().getStatusCode());
    }
}

/**
 * 
 */
package com.sample.excercise.pingeneration.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.excercise.pingeneration.dto.MSISDNModel;
import com.sample.excercise.pingeneration.dto.PinGenerationResponseData;
import com.sample.excercise.pingeneration.dto.ValidatePINModel;
import com.sample.excercise.pingeneration.dto.response.PINManagerServiceResponse;
import com.sample.excercise.pingeneration.service.impl.PinGenerationServiceImpl;

/**
 * @author Ahmad
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PinManagementController.class)
public class PinManagementControllerTest  {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PinGenerationServiceImpl pinGenerationService;
    
    ValidatePINModel model;
    PINManagerServiceResponse<PinGenerationResponseData> response;
    
    MSISDNModel msisdnModel;
    
    ObjectMapper objectMapper;
    
    @Before
    public void setup() {
	model = new ValidatePINModel();
	model.setMsisdn("KzM0OTUxNzg0NDMzMQ==");
	model.setPin("MTIzNA==");
	response = new PINManagerServiceResponse<PinGenerationResponseData>();
	PinGenerationResponseData pinResponse = new PinGenerationResponseData(model.getMsisdn(), model.getMsisdn());
	response.setData(pinResponse);
	response.setStatusCode(0);
	objectMapper = new ObjectMapper();
	
	msisdnModel = new MSISDNModel();
    }
    
    
    @Test
    public void shouldValidatePin() throws Exception{
	when(pinGenerationService.checkFailedAttemptsAndValidatePin(model)).thenReturn(
		new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response, HttpStatus.OK)
		);
	
	this.mockMvc.perform(post("/validatePin")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(model)))
		.andExpect(status().is2xxSuccessful());
    }
    
    @Test
    public void shouldGeneratePin() throws Exception{
	when(pinGenerationService.savePinWithMSISDN(model.getMsisdn())).thenReturn(
		new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response, HttpStatus.CREATED)
		);
	msisdnModel.setMsisdn(model.getMsisdn());
	this.mockMvc.perform(post("/generatePin")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(msisdnModel)))
		.andExpect(status().is2xxSuccessful());
    }
    
    @Test
    public void shouldNotGeneratePin_NonEncodedInput() throws Exception{
	when(pinGenerationService.savePinWithMSISDN("+914523232323")).thenReturn(
		new ResponseEntity<PINManagerServiceResponse<PinGenerationResponseData>>(response, HttpStatus.CREATED)
		);
	msisdnModel.setMsisdn("+914523232323");
	this.mockMvc.perform(post("/generatePin")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(msisdnModel)))
		.andExpect(status().is4xxClientError());
    }

}

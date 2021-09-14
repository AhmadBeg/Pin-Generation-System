package com.sample.excercise.pingeneration.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.sample.excercise.pingeneration.Utils.Constants;
import com.sample.excercise.pingeneration.dto.DbRowsModel;
import com.sample.excercise.pingeneration.dto.MSISDNAndPINRowMapper;

@RunWith(SpringJUnit4ClassRunner.class)
public class PinGenerationDaoImplTest {
    
    @Mock
    JdbcTemplate jdbcTemplate;
    
    PinGenerationDaoImpl pinDao;
    
    String pin = "1234";
    String msisdn = "+123456789";
    
    @Before
    public void setup() {
	pinDao = new PinGenerationDaoImpl();
	ReflectionTestUtils.setField(pinDao, "pinJDBCTemplate", jdbcTemplate);
    }
    
    
    @Test
    public void shouldAssociateWithMsisdnAndreturnPin() {
	Mockito.when(jdbcTemplate.update(Constants.INSERT_PIN_WITH_MSISDN)).thenReturn(1);
	assertEquals(pin, pinDao.associateWithMsisdnAndreturnPin(msisdn, pin));
    }
    
    @Test
    public void shouldGetAllRowsForGivenMsisdnAndPin() {
	List<DbRowsModel> listDbRows = new ArrayList<>();
	Mockito.when(jdbcTemplate.query(Constants.FETCH_ALL_ROWS_FOR_MSISDN, new Object[]{msisdn}, new MSISDNAndPINRowMapper()))
	.thenReturn(listDbRows);
	assertEquals(listDbRows, pinDao.getAllRowsForGivenMsisdnAndPin(msisdn));
    }
    
    @Test
    public void shouldRemoveALLPINsAssignedToMSISDN() {
	Mockito.when(jdbcTemplate.update(Constants.REMOVE_PINS_FOR_MSISDN, msisdn)).thenReturn(1);
	assertEquals(1, pinDao.removeALLPINsAssignedToMSISDN(msisdn));
    }
    
    @Test
    public void shouldIncrementFailureCount() {
	Mockito.when(jdbcTemplate.update(Constants.INCREMENT_FAILURE_COUNT, msisdn)).thenReturn(1);
	pinDao.incrementFailureCount(msisdn);
	verify(jdbcTemplate, times(1)).update(Constants.INCREMENT_FAILURE_COUNT, msisdn);
    }
    
    @Test
    public void shouldfetchRowsCountForMsisdn() {
	Mockito.when(jdbcTemplate.queryForObject(Constants.FETCH_ROWS_FOR_MSISDN, new Object[] {msisdn}, Integer.class))
	.thenReturn(1);
	assertEquals(1, pinDao.fetchRowsCountForMsisdn(msisdn));
    }
    
    @Test
    public void shouldRemoveAnHourOldDBRows() {
	Mockito.when(jdbcTemplate.update(Constants.REMOVE_AN_HOUR_OLD_ROWS)).thenReturn(1);
	pinDao.removeAnHourOldDBRows();
	verify(jdbcTemplate, times(1)).update(Constants.REMOVE_AN_HOUR_OLD_ROWS);
    }
    
}

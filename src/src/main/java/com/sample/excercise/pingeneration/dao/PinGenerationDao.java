package com.sample.excercise.pingeneration.dao;

import java.util.List;

import com.sample.excercise.pingeneration.dto.DbRowsModel;

public interface PinGenerationDao {
    String associateWithMsisdnAndreturnPin(String msisdn, String pin);
    List<DbRowsModel> getAllRowsForGivenMsisdnAndPin(String msisdn);
    int removeALLPINsAssignedToMSISDN(String msisdn);
    void incrementFailureCount(String msisdn);
    int fetchRowsCountForMsisdn(String msisdn);
    int removeAnHourOldDBRows();
}

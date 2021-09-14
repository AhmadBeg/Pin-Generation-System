/**
 * 
 */
package com.sample.excercise.pingeneration.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Ahmad 
 * This RowMapper saves all DB rows fetched from DB while validating PIN 
 * @param <DbRowsModel>
 *
 */
public class MSISDNAndPINRowMapper implements RowMapper<DbRowsModel> {

    @Override
    public DbRowsModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
	DbRowsModel dbRowsModel  = new DbRowsModel();
	dbRowsModel.setMsisdn(resultSet.getString("MSISDN"));
	dbRowsModel.setPin(resultSet.getString("PIN"));
	dbRowsModel.setFailedAttempts(resultSet.getInt("FAIL_ATMPT"));
	return dbRowsModel;
    }
}

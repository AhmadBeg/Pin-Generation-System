/**
 * 
 */
package com.sample.excercise.pingeneration.dto.response;

/**
 * @author Ahmad
 *
 *This model class will encapsulate service response
 */
public class PINManagerServiceResponse<T> {
    
    int statusCode;
    String statusDescription;
    T data;
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getStatusDescription() {
        return statusDescription;
    }
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}

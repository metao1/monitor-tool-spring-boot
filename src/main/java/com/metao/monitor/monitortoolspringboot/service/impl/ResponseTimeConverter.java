package com.metao.monitor.monitortoolspringboot.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.ResponseTimeDTO;

import org.springframework.stereotype.Component;

@Component
public class ResponseTimeConverter {

    public ResponseTimeDTO toDto(@NotBlank @Valid ResponseData responseData){    
        return new ResponseTimeDTO(responseData.getUrl(), responseData.getStatus(), responseData.getResponseTime(), responseData.getTimestamp());
    }
}

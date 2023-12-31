package com.metao.monitor.monitortoolspringboot.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.AverageViewModelDTO;
import com.metao.monitor.monitortoolspringboot.service.AverageViewModelMapper;

import org.springframework.stereotype.Component;

@Component
public class AverageViewModelConverter implements AverageViewModelMapper<AverageViewModel, AverageViewModelDTO> {

    @Override
    public AverageViewModelDTO toDto(@NotBlank @Valid AverageViewModel averageViewModel) {    
        return new AverageViewModelDTO(averageViewModel.getWindowSize(), averageViewModel.getMovingAverageResponseTime(), averageViewModel.getTimestamp());
    }
}

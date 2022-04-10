package com.metao.monitor.monitortoolspringboot.service;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.AverageViewModelDTO;

public interface AverageViewModelMapper<T extends AverageViewModel, R extends AverageViewModelDTO> {

    R toDto(T t);
}

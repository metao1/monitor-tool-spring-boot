package com.metao.monitor.monitortoolspringboot.repository;

import java.util.HashMap;
import java.util.Map;

import com.metao.monitor.monitortoolspringboot.model.MovingAverageData;

import org.springframework.stereotype.Service;

@Service
public class MovingAverageRepository {

    private Map<Integer, MovingAverageData> movingAverageMap = new HashMap<>();

    public boolean existsById(Integer windowSize) {
        return movingAverageMap.containsKey(windowSize);
    }

    public void save(int windowSize, MovingAverageData movingAverage) {       
        movingAverageMap.put(windowSize, movingAverage);
    }

    public MovingAverageData findById(Integer windowSize) {
        return movingAverageMap.get(windowSize);
    }
}

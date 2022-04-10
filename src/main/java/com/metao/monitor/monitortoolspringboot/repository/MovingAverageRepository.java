package com.metao.monitor.monitortoolspringboot.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.metao.monitor.monitortoolspringboot.model.MovingAverageData;

import org.springframework.stereotype.Service;

@Service
public class MovingAverageRepository {

    private Map<Integer, MovingAverageData> movingAverageMap = new ConcurrentHashMap<>();

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

package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Station;
import com.balaev.gradproj.repository.StationRepository;
import com.balaev.gradproj.service.api.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("stationService")
@Transactional
public class StationServiceImpl implements StationService {

    @Autowired
    StationRepository stationRepository;

    @Override
    public List<Station> getAllStations() {
        return (List<Station>) stationRepository.findAll();
    }
}

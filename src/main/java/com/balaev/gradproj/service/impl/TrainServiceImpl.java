package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.Train;
import com.balaev.gradproj.repository.TrainRepository;
import com.balaev.gradproj.service.api.TrainService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("trainService")
@Transactional
public class TrainServiceImpl implements TrainService {
    private static final Logger LOG = Logger.getLogger(TrainServiceImpl.class);
    @Autowired
    TrainRepository trainRepository;

    @Override
    public Train createTrain(Train train) {
        Train result = null;
        result = trainRepository.save(train);
        LOG.info("train created {}", result);
        return result;
    }

    @Override
    public List<Train> getAllTrains() {
        LOG.info("get list of all trains");
        return trainRepository.findAll();
    }

    @Override
    public Train read(int id) {
        Train result = null;
        result = trainRepository.findOne(id);
        LOG.info("read train by id {}", result);
        return result;
    }
}

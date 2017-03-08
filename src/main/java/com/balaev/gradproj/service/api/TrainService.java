package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.Train;

import java.util.List;

public interface TrainService {
    Train createTrain(Train train);

    List<Train> getAllTrains();

    Train read(int id);
}

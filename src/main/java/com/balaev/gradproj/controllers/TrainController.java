package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.Train;
import com.balaev.gradproj.service.api.TrainService;
import com.balaev.gradproj.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TrainController {
    @Autowired
    TrainService trainService;

    @RequestMapping(value = "/trains", method = RequestMethod.GET)
    public ModelAndView getAllTrains() {
        ModelAndView modelAndView = new ModelAndView();
        List<Train> trains = trainService.getAllTrains();
        modelAndView.addObject("trains", trains);
        modelAndView.addObject("title", "Trains");
        modelAndView.setViewName("/train/all-trains");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/train/add", method = RequestMethod.GET)
    public ModelAndView addTrainForm() {
        ModelAndView modelAndView = new ModelAndView("/admin/train/add-train");
        modelAndView.addObject("title", "Create train");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/train/add", method = RequestMethod.POST)
    public ModelAndView addTrain(HttpServletRequest request) {
        String seatsStr = request.getParameter("seats");
        if (ValidationUtils.checkNumber(seatsStr) && seatsStr.length() < 10) {
            int seats = Integer.parseInt(seatsStr);
            if (ValidationUtils.checkNumberSeats(seats)) {
                Train train = new Train();
                train.setSeats(seats);
                trainService.createTrain(train);
                return getAllTrains();
            } else {
                request.setAttribute("error", "Number is too big. From 1 to 15 seats");
                return addTrainForm();
            }
        } else {
            request.setAttribute("error", "Incorrect text");
            return addTrainForm();
        }
    }
}

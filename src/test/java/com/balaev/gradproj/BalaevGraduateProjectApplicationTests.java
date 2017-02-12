package com.balaev.gradproj;

import com.balaev.gradproj.domain.Passenger;
import com.balaev.gradproj.domain.User;
import com.balaev.gradproj.repository.PassengerRepository;
import com.balaev.gradproj.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BalaevGraduateProjectApplication.class)
@WebAppConfiguration
public class BalaevGraduateProjectApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void loadUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        assertEquals("Arif Balaev", users.get(0).getFio());
    }

    @Test
    public void getPassengerByFullName() {
        Passenger passenger = passengerRepository.findByLastNameAndFirstName("Filippova", "Daria");
        assertEquals(2, (long) passenger.getIdPassenger());
    }

}

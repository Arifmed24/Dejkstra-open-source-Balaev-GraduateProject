package com.balaev.gradproj.controllers;

import com.balaev.gradproj.domain.User;
import com.balaev.gradproj.service.api.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationController {
    private static final Logger LOG = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPageForm() {
        return new ModelAndView("/login");
    }

//    @RequestMapping(value = {"/logSuccess"}, method = RequestMethod.GET)
//    public String loginSuccess(HttpSession session) {
//        String login = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userService.findUserByLogin(login);
//        session.setAttribute("user", user);
//
//        return "redirect:/findway";
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpSession session) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            User user = userService.login(login, password);
            if (user == null) {
                request.setAttribute("error", "Invalid");
                return loginPageForm();
            } else {
                session.setAttribute("user", user);
                return new ModelAndView("redirect:/findway");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registrationPageForm() {
        return new ModelAndView("/registration");
    }

    @RequestMapping(value = "/out", method = RequestMethod.GET)
    public ModelAndView doLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.setAttribute("logout", "success");
        return loginPageForm();
    }
}

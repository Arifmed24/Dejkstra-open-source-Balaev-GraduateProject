package com.balaev.gradproj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created in 2017 year.
 *
 * @autor Arif Balaev
 */

@Controller
public class IndexController {
    @RequestMapping("/")
    String index(){
        return "index";
    }
}

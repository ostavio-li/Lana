package com.edwardlee.library.controller;

import com.edwardlee.library.service.OntologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author EdwardLee
 * @date 2020/11/2
 */
@Controller
@RequestMapping("/")
public class MainController {

    private OntologyService service;

    @Autowired
    public void setService(OntologyService service) {
        this.service = service;
    }

    @GetMapping(value = "")
    public String start() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/faq")
    public String faq() {
        return "/faq";
    }

    @GetMapping("/read")
    @ResponseBody
    public String read(){
        return service.findBookByAuthor("a");
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("question") String question) {
        ModelAndView modelAndView = new ModelAndView("result");
        String search = service.search(question);
        modelAndView.addObject("que", search);
        return modelAndView;
    }


}

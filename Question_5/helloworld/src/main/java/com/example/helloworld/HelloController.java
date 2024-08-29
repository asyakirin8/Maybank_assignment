package com.example.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/hello")
    public String sayHello(Model model) {
        String message = messageService.getMessage();
        model.addAttribute("message", message);
        return "hello";
    }
}

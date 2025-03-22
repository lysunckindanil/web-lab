package org.example.webapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    @GetMapping("/form")
    public String form() {
        return "content/form";
    }
}


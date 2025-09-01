package com.example.mgdSpringReact.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    // Serve the React index.html for the root path
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    // Serve React for any non-API top-level path (without dots, e.g., /tasks)
    // Use a single-segment regex; avoid combining with /** which is invalid in Spring 6 PathPattern
    @GetMapping("/{path:^(?!api$)[^.]*}")
    public String anySingleLevel() {
        return "forward:/index.html";
    }
}

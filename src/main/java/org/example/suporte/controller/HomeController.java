package org.example.suporte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirecionarParaChamados() {
        return "redirect:/chamados";
    }
}

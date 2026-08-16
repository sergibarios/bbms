package com.doubleb.bbms.controller;

import com.doubleb.bbms.model.Competicion;
import com.doubleb.bbms.service.CompeticionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final CompeticionService competicionService;

    public HomeController(CompeticionService competicionService) {
        this.competicionService = competicionService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/competiciones";
    }

    @GetMapping("/competiciones")
    public String listar(Model model) {
        model.addAttribute("competiciones", competicionService.findAll());
        return "index";
    }

    @PostMapping("/competiciones")
    public String guardar(@ModelAttribute Competicion competicion) {
        competicionService.save(competicion);
        return "redirect:/competiciones";
    }

    @PostMapping("/competiciones/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Competicion competicion) {
        competicion.setId(id);
        competicionService.save(competicion);
        return "redirect:/competiciones";
    }

    @PostMapping("/competiciones/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        competicionService.deleteById(id);
        return "redirect:/competiciones";
    }
}
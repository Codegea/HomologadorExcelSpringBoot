package com.excel.Homologador.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.excel.Homologador.service.IInstitucionEducativaService;

@Controller
public class HomologadorController extends Object {

    @Autowired
    IInstitucionEducativaService homologador;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
            throws IOException {

        if (file == null || file.isEmpty()) {
            attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
            return "redirect:status";
        }

        StringBuilder builder = homologador.uploadFile(file, attributes);

        boolean homologarFichero = homologador.homologarFichero();

        attributes.addFlashAttribute("message", "Archivo cargado en el servidor satisfactoriamente : " + builder.toString());

        return "redirect:/status";
    }

    @GetMapping("/status")
    public String status() {
        return "status";
    }
}

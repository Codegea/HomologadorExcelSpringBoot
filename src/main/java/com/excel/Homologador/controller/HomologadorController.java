package com.excel.Homologador.controller;

import com.excel.Homologador.entity.InstitucionEducativa;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import com.excel.Homologador.service.IHomologadorService;

@Controller
public class HomologadorController extends Object {

    @Autowired
    IHomologadorService homologador;

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
        // ESTE PROCESO DEBERA RETORNAR UNA LISTA DE INSTITUCIONES EDUCATIVAS CON REGISTROS MULTIPLES POR SU NOMBRE
        // SI HAY REGISTROS EN ESTA LISTA SE DEBE MOSTRAR AL USUARIO UNA TABLA CON EL CONTENIDO
        List<InstitucionEducativa> institucionesDuplicadas = homologador.homologarFichero();

        attributes.addFlashAttribute("message", "Archivo cargado en el servidor satisfactoriamente : " + builder.toString());

        return "redirect:/status";
    }

    @GetMapping("/status")
    public String status() {
        return "status";
    }
}

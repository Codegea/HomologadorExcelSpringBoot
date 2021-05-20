package com.excel.Homologador.controller.rest;

import com.excel.Homologador.dao.IInstitucionEducativaDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class InstitucionEducativaControllerRest {

    @Autowired
    IInstitucionEducativaDao institucionEducativaDao;
    
    @GetMapping("/listar")
    public List listarRest(){
        return institucionEducativaDao.findAll();
    }

}

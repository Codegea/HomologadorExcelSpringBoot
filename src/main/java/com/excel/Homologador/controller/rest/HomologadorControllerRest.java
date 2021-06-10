package com.excel.Homologador.controller.rest;

import com.excel.Homologador.dao.IEducacionFormalDao;
import com.excel.Homologador.dao.IInstitucionEducativaDao;
import com.excel.Homologador.dao.IProgramaAcademicoDao;
import com.excel.Homologador.entity.EducacionFormal;
import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class HomologadorControllerRest {

    @Autowired
    IInstitucionEducativaDao institucionEducativaDao;

    @Autowired
    IProgramaAcademicoDao programaAcademicoDao;

    @Autowired
    IEducacionFormalDao educacionFormalDao;

    @GetMapping("/listarInstituciones")
    public Page<InstitucionEducativa> listarRest(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return institucionEducativaDao.findAll(pageable);
    }

    @GetMapping("/listarxNombre")
    public List<InstitucionEducativa> listarXNombre() {
        return institucionEducativaDao.findByNombreInstitucion("FUNDACION UNIVERSIDAD DE AMERICA");
    }

    @GetMapping("/listarProgramasAcade")
    public Page<ProgramaAcademico> listarRestProgramaAcademico(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return programaAcademicoDao.findAll(pageable);
    }

    @GetMapping("/listarEducacionFor")
    public Page<EducacionFormal> listarRestEducacionFormal(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return educacionFormalDao.findAll(pageable);
    }
}
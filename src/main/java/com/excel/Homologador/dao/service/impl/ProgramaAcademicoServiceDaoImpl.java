package com.excel.Homologador.dao.service.impl;

import com.excel.Homologador.dao.IProgramaAcademicoDao;
import com.excel.Homologador.dao.service.ProgramaAcademicoServiceDao;
import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramaAcademicoServiceDaoImpl implements ProgramaAcademicoServiceDao {

    @Autowired
    IProgramaAcademicoDao programaAcademicoDao;

    @Override
    public List<ProgramaAcademico> listaProgramasAcademicos() {
        return programaAcademicoDao.findAll();
    }

    @Override
    public void guardar(ProgramaAcademico programaAcademico) {
        programaAcademicoDao.save(programaAcademico);
    }

    @Override
    public void eliminar(ProgramaAcademico programaAcademico) {
        programaAcademicoDao.delete(programaAcademico);
    }

    /**
     * Metodo que permite actualizar la institucion correcta de un programa
     * academico
     *
     * @param id Codigo del programa academico
     * @param institucionEducativa Entidad institucion educativa correcta
     * @return ProgramaAcademico actualizado
     */
    @Override
    public ProgramaAcademico actualizar(long id, InstitucionEducativa institucionEducativa) {
        Optional<ProgramaAcademico> programa = programaAcademicoDao.findById(id);
        if (programa.isPresent()) {
            ProgramaAcademico tmp = programa.get();
            tmp.setInstitucionEdu(institucionEducativa);
            return programaAcademicoDao.save(tmp);
        }
        return null;
    }

    @Override
    public Optional<ProgramaAcademico> obtenerPorId(Long id) {
        return programaAcademicoDao.findById(id);
    }

}

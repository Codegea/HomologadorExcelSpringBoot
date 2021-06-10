package com.excel.Homologador.dao.service;

import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;
import java.util.List;
import java.util.Optional;

public interface ProgramaAcademicoServiceDao {

    public List<ProgramaAcademico> listaProgramasAcademicos();

    public void guardar(ProgramaAcademico programaAcademico);

    public void eliminar(ProgramaAcademico programaAcademico);

    public ProgramaAcademico actualizar(long id, InstitucionEducativa institucionEducativaNueva);

    public Optional<ProgramaAcademico> obtenerPorId(Long id);

}

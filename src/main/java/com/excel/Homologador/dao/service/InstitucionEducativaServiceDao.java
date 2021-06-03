package com.excel.Homologador.dao.service;

import com.excel.Homologador.entity.InstitucionEducativa;
import java.util.List;

public interface InstitucionEducativaServiceDao {

    public List<InstitucionEducativa> listaInstitucionesEducativas();

    public void guardar(InstitucionEducativa institucionEducativa);

    public void eliminar(InstitucionEducativa institucionEducativa);

    public List<InstitucionEducativa> encontrarPorNombreInstitucion(String nombreInstitucion);

    public InstitucionEducativa buscarInstitucionPorId(Long idInstitucion);
}

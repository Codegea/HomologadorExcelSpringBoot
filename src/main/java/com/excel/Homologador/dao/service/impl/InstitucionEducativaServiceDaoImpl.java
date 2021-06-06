package com.excel.Homologador.dao.service.impl;

import com.excel.Homologador.dao.IInstitucionEducativaDao;
import com.excel.Homologador.dao.service.InstitucionEducativaServiceDao;
import com.excel.Homologador.entity.InstitucionEducativa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitucionEducativaServiceDaoImpl implements InstitucionEducativaServiceDao {

    @Autowired
    IInstitucionEducativaDao institucionEducativaDao;

    @Override
    public List<InstitucionEducativa> listaInstitucionesEducativas() {
        return institucionEducativaDao.findAll();
    }

    @Override
    public void guardar(InstitucionEducativa institucionEducativa) {
        institucionEducativaDao.save(institucionEducativa);
    }

    @Override
    public void eliminar(InstitucionEducativa institucionEducativa) {
        institucionEducativaDao.delete(institucionEducativa);
    }

    @Override
    public List<InstitucionEducativa> encontrarPorNombreInstitucion(String nombreInstitucion) {
        return institucionEducativaDao.findByNombreInstitucion(nombreInstitucion);
    }

    @Override
    public InstitucionEducativa buscarInstitucionPorId(Long idInstitucion) {
        return institucionEducativaDao.findByCodInstitucionEducativa(idInstitucion);
    }

}

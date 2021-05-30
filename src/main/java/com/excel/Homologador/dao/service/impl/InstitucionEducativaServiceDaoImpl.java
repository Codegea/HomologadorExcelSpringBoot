package com.excel.Homologador.dao.service.impl;

import com.excel.Homologador.dao.IInstitucionEducativaDao;
import com.excel.Homologador.dao.service.InstitucionEducativaServiceDao;
import com.excel.Homologador.entity.InstitucionEducativa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstitucionEducativaServiceDaoImpl implements InstitucionEducativaServiceDao {

    @Autowired
    IInstitucionEducativaDao institucionEducativaDao;

    @Override
    @Transactional(readOnly = true)
    public List<InstitucionEducativa> listaInstitucionesEducativas() {
        return institucionEducativaDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(InstitucionEducativa institucionEducativa) {
        institucionEducativaDao.save(institucionEducativa);
    }

    @Override
    @Transactional
    public void eliminar(InstitucionEducativa institucionEducativa) {
        institucionEducativaDao.delete(institucionEducativa);
    }

    @Override
    @Transactional
    public List<InstitucionEducativa> encontrarPorNombreInstitucion(String nombreInstitucion) {
        return institucionEducativaDao.findByNombreInstitucion(nombreInstitucion);
    }

    @Override
    public InstitucionEducativa buscarInstitucionPorId(Long idInstitucion) {
        return institucionEducativaDao.findByCodInstitucionEducativa(idInstitucion);
    }

}

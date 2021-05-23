package com.excel.Homologador.dao;

import com.excel.Homologador.entity.InstitucionEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInstitucionEducativaDao extends JpaRepository<InstitucionEducativa, Long> {

    public InstitucionEducativa findByNombreInstitucion(String nombreInstitucion);


}

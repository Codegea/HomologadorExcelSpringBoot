package com.excel.Homologador.dao;

import com.excel.Homologador.entity.InstitucionEducativa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInstitucionEducativaDao extends JpaRepository<InstitucionEducativa, Long> {

    public List<InstitucionEducativa> findByNombreInstitucion(String nombreInstitucion);

}

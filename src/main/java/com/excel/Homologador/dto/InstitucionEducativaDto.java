package com.excel.Homologador.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class InstitucionEducativaDto {

    private Long codInstitucionEducativa;
    private Long codInstitucionMen;
    private Long codTipoInstitucion;
    private String nombreInstitucion;
    private Date audFechaActualizacion;
    private Long audCodUsuario;
    private Long audCodRol;
    private Long audAccion;
    private Long flgActivo;
    private Long flgInstExtrajera;
    private List<ProgramaAcademicoDto> programasAcademicos;

}
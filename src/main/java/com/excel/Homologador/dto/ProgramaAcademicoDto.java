package com.excel.Homologador.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ProgramaAcademicoDto {

    private Long codTituloAcademico;
    private Long codProgramaAcademico;
    private String nombreProgramaAcademico;
    private String nombreTituloOtorgado;
    private Long flgActivo;
    private Date audFechaActualizacion;
    private Long audCodUsuario;
    private Long audCodRol;
    private Long audAccion;
    private Long institucionEducativa;

}

package com.excel.Homologador.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programa_academico")
public class ProgramaAcademico {

    @Id
    @Column(name = "COD_TITULO_ACADEMICO", nullable = true)
    private Long codTituloAcademico;

    @Column(name = "COD_INSTITUCION")
    private Long codInstitucion;

    @Column(name = "COD_PROGRAMA_ACADEMICO")
    private Long codProgramaAcademico;

    @Column(name = "NOMBRE_PROGRAMA_ACADEMICO")
    private String nombreProgramaAcademico;

    @Column(name = "NOMBRE_TITULO_OTORGADO")
    private String nombreTituloOtorgado;

    @Column(name = "FLG_ACTIVO")
    private Long flgActivo;

    @Column(name = "AUD_FECHA_ACTUALIZACION")
    private Long audFechaActualizacion;

    @Column(name = "AUD_COD_USUARIO")
    private Long audCodUsuario;

    @Column(name = "AUD_COD_ROL")
    private Long audCodRol;

    @Column(name = "AUD_ACCION")
    private Long audAccion;

}

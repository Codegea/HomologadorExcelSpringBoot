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
@Table(name = "institucion_educativa")
public class InstitucionEducativa {

    @Id
    @Column(name = "COD_INSTITUCION_EDUCATIVA")
    private Long codInstitucionEducativa;

    @Column(name = "COD_INSTITUCION_MEN")
    private Long codInstitucionMen;

    @Column(name = "COD_TIPO_INSTITUCION", nullable = true)
    private Long codTipoInstitucion;

    @Column(name = "NOMBRE_INSTITUCION", nullable = true)
    private String nombreInstitucion;

    @Column(name = "AUD_FECHA_ACTUALIZACION", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date audFechaActualizacion;

    @Column(name = "AUD_COD_USUARIO")
    private Long audCodUsuario;

    @Column(name = "AUD_COD_ROL", nullable = true)
    private Long audCodRol;

    @Column(name = "AUD_ACCION", nullable = true)
    private Long audAccion;

    @Column(name = "FLG_ACTIVO", nullable = true)
    private Long flgActivo;

    @Column(name = "FLG_INST_EXTRAJERA", nullable = true)
    private Long flgInstExtrajera;

}

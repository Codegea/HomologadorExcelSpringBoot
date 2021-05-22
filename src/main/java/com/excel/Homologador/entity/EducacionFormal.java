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
@Table(name = "educacion_formal")
public class EducacionFormal {

    @Id
    @Column(name = "COD_EDUCACION_FORMAL", nullable = true)
    private Long codEducacionFormal;

    @Column(name = "COD_PERSONA", nullable = true)
    private Long codPersona;

    @Column(name = "COD_NIVEL_FORMACION")
    private Long codNivelFormacion;

    @Column(name = "COD_NIVEL_EDUCATIVO")
    private Long codNivelEducativo;

    @Column(name = "COD_AREA_CONOCIMIENTO")
    private Long codAreaConocimiento;

    @Column(name = "COD_PAIS", nullable = true)
    private Long codPais;

    @Column(name = "COD_DEPARTAMENTO")
    private Long codDepartamento;

    @Column(name = "COD_MUNICIPIO")
    private Long codMunicipio;

    @Column(name = "COD_PROGRAMA_ACADEMICO")
    private Long codProgramaAcademico;

    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date fechaInicio;

    @Column(name = "FECHA_FINALIZACION")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date fechaFinalizacion;

    @Column(name = "SEMESTRES_APROBADO")
    private Long semestresAprobado;

    @Column(name = "FLG_FINALIZADO")
    private Long flgFinalizado;

    @Column(name = "FLG_EDUCACION_EXTERIOR")
    private Long flgEducacionExterior;

    @Column(name = "FECHA_GRADO")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date fechaGrado;

    @Column(name = "ESTUDIO_CONVALIDADO")
    private Long estudioConvalidado;

    @Column(name = "FECHA_CONVALIDACION_ESTUDIO")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date fechaConvalidacionEstudio;

    @Column(name = "COD_USUARIO_CONVALIDA")
    private Long codUsuarioConvalida;

    @Column(name = "URL_ANEXO")
    private String urlAnexo;

    @Column(name = "FLG_VALIDADO_RRHH", nullable = true)
    private Long flgValidadoRrhh;

    @Column(name = "COD_USUARIO_VERIFICA")
    private Long codUsuarioVerifica;

    @Column(name = "FECHA_VALIDACION_RRHH")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date fechaValidacionRrhh;

    @Column(name = "AUD_FECHA_ACTUALIZACION")
    @Temporal(TemporalType.DATE) // preguntar por esta!!
    private Date audFechaActualizacion;

    @Column(name = "AUD_COD_USUARIO")
    private Long audCodUsuario;

    @Column(name = "AUD_COD_ROL")
    private Long audCodRol;

    @Column(name = "AUD_ACCION")
    private Long audAccion;

    @Column(name = "NUM_TARJETA_PROFESIONAL")
    private String numTarjetaProfesional;

    @Column(name = "ORDEN_NIVEL_FORMACION")
    private Long ordenNivelFormacion;

    @Column(name = "COD_TITULO_OBTENIDO")
    private Long codTituloObtenido;

    @Column(name = "URL_TARJETA_PROFESIONAL")
    private String urlTarjetaProfesional;

    @Column(name = "URL_DIPLOMA")
    private String urlDiploma;

    @Column(name = "FLG_ACTIVO")
    private Long flgActivo;

    @Column(name = "URL_FORMACION_ACADEMICA")
    private String urlFormacionAcademica;

    @Column(name = "FLG_VALIDADO_TARJETA_PROFESIONAL")
    private Long flgValidadoTarjetaProfesional;

    @Column(name = "DT_FECHA_ULTIMA_ACTUALIZACION", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dtFechaUltimaActualizacion;

}

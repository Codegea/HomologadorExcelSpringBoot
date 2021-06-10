package com.excel.Homologador.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class ProgramaAcademico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COD_TITULO_ACADEMICO", nullable = true)
    private Long codTituloAcademico;

    @Column(name = "COD_PROGRAMA_ACADEMICO")
    private Long codProgramaAcademico;

    @Column(name = "NOMBRE_PROGRAMA_ACADEMICO")
    private String nombreProgramaAcademico;

    @Column(name = "NOMBRE_TITULO_OTORGADO")
    private String nombreTituloOtorgado;

    @Column(name = "FLG_ACTIVO")
    private Long flgActivo;

    @Column(name = "AUD_FECHA_ACTUALIZACION")
    @Temporal(TemporalType.DATE)
    private Date audFechaActualizacion;

    @Column(name = "AUD_COD_USUARIO")
    private Long audCodUsuario;

    @Column(name = "AUD_COD_ROL")
    private Long audCodRol;

    @Column(name = "AUD_ACCION")
    private Long audAccion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COD_INSTITUCION", referencedColumnName = "COD_INSTITUCION_EDUCATIVA")
    private InstitucionEducativa institucionEdu;

    @OneToMany(mappedBy = "programa")
    private List<EducacionFormal> listaEduFormal;

}
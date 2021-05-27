package com.excel.Homologador.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "educacion_formal")
public class EducacionFormal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COD_EDUCACION_FORMAL", nullable = true)
    private Long codEducacionFormal;

    @Column(name = "COD_PERSONA", nullable = true)
    private Long codPersona;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COD_PROGRAMA_ACADEMICO", referencedColumnName = "COD_TITULO_ACADEMICO")
    ProgramaAcademico programa;

}

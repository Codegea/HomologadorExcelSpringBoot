package com.excel.Homologador.utils;

import com.excel.Homologador.dto.InstitucionEducativaDto;
import com.excel.Homologador.dto.ProgramaAcademicoDto;
import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;

public class ConvertidorEntityToDto {

    public static InstitucionEducativaDto institucionEducativaEntityToDto(InstitucionEducativa entity) {
        InstitucionEducativaDto institucionEducativaDto = new InstitucionEducativaDto();
        institucionEducativaDto.setAudAccion(entity.getAudAccion());
        institucionEducativaDto.setAudCodRol(entity.getAudCodRol());
        institucionEducativaDto.setAudCodUsuario(entity.getAudCodUsuario());
        institucionEducativaDto.setAudFechaActualizacion(entity.getAudFechaActualizacion());
        institucionEducativaDto.setCodInstitucionEducativa(entity.getCodInstitucionEducativa());
        institucionEducativaDto.setCodInstitucionMen(entity.getCodInstitucionMen());
        institucionEducativaDto.setCodTipoInstitucion(entity.getCodTipoInstitucion());
        institucionEducativaDto.setFlgActivo(entity.getFlgActivo());
        institucionEducativaDto.setFlgInstExtrajera(entity.getFlgInstExtrajera());
        institucionEducativaDto.setNombreInstitucion(entity.getNombreInstitucion());
        return institucionEducativaDto;
    }

    public static ProgramaAcademicoDto programaAcademicoEntityToDto(ProgramaAcademico entity) {
        ProgramaAcademicoDto programaAcademicoDto = new ProgramaAcademicoDto();
        programaAcademicoDto.setAudAccion(entity.getAudAccion());
        programaAcademicoDto.setAudCodRol(entity.getAudCodRol());
        programaAcademicoDto.setAudCodUsuario(entity.getAudCodUsuario());
        programaAcademicoDto.setAudFechaActualizacion(entity.getAudFechaActualizacion());
        programaAcademicoDto.setCodProgramaAcademico(entity.getCodProgramaAcademico());
        programaAcademicoDto.setCodTituloAcademico(entity.getCodTituloAcademico());
        programaAcademicoDto.setFlgActivo(entity.getFlgActivo());
        programaAcademicoDto.setInstitucionEducativa(ConvertidorEntityToDto.institucionEducativaEntityToDto(entity.getInstitucionEdu()));
        programaAcademicoDto.setNombreProgramaAcademico(entity.getNombreProgramaAcademico());
        programaAcademicoDto.setNombreTituloOtorgado(entity.getNombreTituloOtorgado());
        return programaAcademicoDto;
    }

}

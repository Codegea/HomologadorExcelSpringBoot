package com.excel.Homologador.service.impl;

import com.excel.Homologador.dao.service.InstitucionEducativaServiceDao;
import com.excel.Homologador.dto.ProgramaAcademicoDto;
import com.excel.Homologador.dto.RegistrosDto;
import com.excel.Homologador.entity.EducacionFormal;
import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;
import com.excel.Homologador.utils.XLSX2CSV;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.excel.Homologador.service.IInstitucionEducativaService;
import com.excel.Homologador.utils.ConvertidorEntityToDto;
import java.util.ArrayList;

@Service
public class InstitucionEducativaServiceImpl implements IInstitucionEducativaService {

    @Autowired
    InstitucionEducativaServiceDao institucionEducativaServiceDao;

    Logger logger = LoggerFactory.getLogger(InstitucionEducativaServiceImpl.class);

    @Override
    public StringBuilder uploadFile(MultipartFile file, RedirectAttributes attributes) throws IOException {

        StringBuilder builder = new StringBuilder();
        builder.append(System.getProperty("user.home"));
        builder.append(File.separator);
        builder.append("uploads");
        builder.append(File.separator);
        builder.append(file.getOriginalFilename());

        byte[] fileBytes = file.getBytes();
        Path path = Paths.get(builder.toString());
        Files.write(path, fileBytes);

        return builder;
    }

    /**
     * En este metodo se almacenara en la lista que se retorne las intituciones
     * repetidas y que en pantalla se van a mostrar.
     *
     * @return CAMBIAR A RETORNO DE UNA LISTA InstitucionEducativa
     */
    @Override
    public List<InstitucionEducativa> homologarFichero() {
        List<InstitucionEducativa> registrosDuplicados = new ArrayList<>();;
        try {
            List<RegistrosDto> registros = XLSX2CSV.ProcesarExcel();
            if (!registros.isEmpty()) {
                for (RegistrosDto registro : registros) {
                    List<InstitucionEducativa> listaInstitucionesXcorregir = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorActualSIGEPII());
                    if (!listaInstitucionesXcorregir.isEmpty() && listaInstitucionesXcorregir != null && listaInstitucionesXcorregir.size() == 1) {
                        InstitucionEducativa InstitucionCorrecta = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorNuevoSIGEPII()).get(0);
                        for (InstitucionEducativa institucionEducativa : listaInstitucionesXcorregir) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                logger.info("\n\n\n***************************************\n\nREGISTRO EXCEL MARCADO PARA BORRADO FISICO : " + registro.getValorActualSIGEPII());
                                // CONSULTA POR CADA PROGRAMA ACADEMICO ASOCIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
                                if (institucionEducativa.getProgramasAcademicos().size() > 0) {
                                    List<ProgramaAcademicoDto> programaAcademicoDtos = null;
                                    for (ProgramaAcademico programaAcademico : institucionEducativa.getProgramasAcademicos()) {
                                        if (programaAcademico.getListaEduFormal().size() > 0) {
                                            logger.info("\nINSTITUCION EDUCATIVA : " + institucionEducativa.getNombreInstitucion()
                                                    + "\nPROGRAMA ACADEMICO: " + programaAcademico.getNombreProgramaAcademico()
                                                    + "\nTIENE REGISTROS ASOCIADOS EN EDUCACION FORMAL\n");
                                            // SE DEBE CLONAR EL PROGRAMA ACADEMICO EN UN DTO PARA PODER PONERLE COMO FORANEA LA INSTITUCION EDUCATIVA CORRECTA Y LUEGO PERSISTIR
                                            if (programaAcademicoDtos.isEmpty()) {
                                                programaAcademicoDtos = new ArrayList<>();
                                            }
                                            programaAcademicoDtos.add(ConvertidorEntityToDto.programaAcademicoEntityToDto(programaAcademico));
                                            // AQUI SE DEBE CLONAR EL PROGRAMA ACADEMICO PARA PODER ACTUALIZAR LA FORANEA DE INSTITUCION EDUCATIVA Y POSTERIORMENTE HACER EL UPDATE
                                            for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
                                                // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                                                logger.info(educacionFormal.toString());
                                            }
                                        } else {
                                            // SI EL RESULTADO ES VACIO ENTONCES SE DEBE ELIMINAR LA INSTITUCION EDUCATIVA Y TODAS SUS DEPENDENCIAS
                                            logger.info("INSTITUCION EDUCATIVA : " + institucionEducativa.getNombreInstitucion()
                                                    + "\nPROGRAMA ACADEMICO: " + programaAcademico.getNombreProgramaAcademico()
                                                    + "\nNO TIENE REGISTROS ASOCIADOS EN EDUCACION FORMAL");
                                        }
                                    }
                                }

                                // SI ENCONTRO REGISTROS AQUI DEBEMOS 
                                // CONSULTAR EN BASE DE DATOS AL REGISTRO EN INSTITUCION EDUCATIVA = "REQUIERE CORRECCION" 
                                // Y OBTENEMOS EL VALOR DEL ID.
                                // AHORA DEBEMOS ASOCIAR TODOS LOS PROGRAMAS ACADEMICOS Y EDICACION FORMAL A ESTE TIPO
                                // DE INSTITUCION EDUCATIVA "REQUIERE CORRECCION", 
                                // Y POR ULTIMO DEBEMOS ELIMINAR ESE REGISTRO MALO 
                                // EN LA TABLA INSTITUCION EDUCATIVA
                            } else {
                                logger.info("REGISTRO EXCEL NO ESTA MARCADO PARA BORRADO FISICO : " + registro.getValorActualSIGEPII());
//                      InstitucionEducativa registroProcesado = institucionEducativaDao.save(institucionEducativa);
//                      logger.info("Registro procesado : " + registroProcesado.toString());
                            }

//                      logger.info("Registro encontrado : " + institucionEducativa.toString());
                            institucionEducativa.setNombreInstitucion(registro.getValorNuevoSIGEPII());
                            logger.info("InstitucionEducativa VALOR ANTES DEL AJUSTE: " + registro.getValorActualSIGEPII());
                            logger.info("InstitucionEducativa AJUSTADA: " + registro.getValorNuevoSIGEPII());
                            logger.info("Cantidad de programas academicos encontrados para ajustar: " + institucionEducativa.getProgramasAcademicos().size());
                        }
                    } else {
                        for (int i = 0; i < listaInstitucionesXcorregir.size(); i++) {
                            registrosDuplicados.add(listaInstitucionesXcorregir.get(i));
                            logger.info("Instituciones educativas duplicada : " + listaInstitucionesXcorregir.get(i));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(null, ex);

        }
        return registrosDuplicados;
    }
}

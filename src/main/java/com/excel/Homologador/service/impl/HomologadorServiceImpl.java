package com.excel.Homologador.service.impl;

import com.excel.Homologador.dao.service.InstitucionEducativaServiceDao;
import com.excel.Homologador.dao.service.ProgramaAcademicoServiceDao;
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
import java.util.ArrayList;
import com.excel.Homologador.service.IHomologadorService;

@Service
public class HomologadorServiceImpl implements IHomologadorService {

    @Autowired
    InstitucionEducativaServiceDao institucionEducativaServiceDao;

    @Autowired
    ProgramaAcademicoServiceDao programaAcademicoServiceDao;

    Logger logger = LoggerFactory.getLogger(HomologadorServiceImpl.class);

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
        File fileActual = new File(builder.toString());
        if (fileActual.exists()) {
            fileActual.delete();
        }
        Files.write(path, fileBytes);

        return builder;
    }

    /**
     * En este metodo se almacenara en la lista que se retorne las intituciones
     * repetidas y que en pantalla se van a mostrar.
     *
     * @return una lista de registrosDuplicados
     */
    @Override
    public List<InstitucionEducativa> homologarFichero() {
        List<InstitucionEducativa> registrosDuplicados = new ArrayList<>();
        try {
            long inicio = System.currentTimeMillis();
            List<RegistrosDto> registros = XLSX2CSV.ProcesarExcel();
            if (!registros.isEmpty()) {
                for (RegistrosDto registro : registros) {
                    List<InstitucionEducativa> listaInstitucionesXcorregir = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorActualSIGEPII());
                    if (listaInstitucionesXcorregir.size() < 1) {
                        logger.info("\n\nLA INSTITUCION EDUCATIVA POR CORREGIR NO EXISTE EN LA BASE DE DATOS EN ESTOS MOMENTOS : " + registro.getValorActualSIGEPII() + " | NUEVA INSTITUCION: " + registro.getValorNuevoSIGEPII());
                        continue;
                    }
                    List<InstitucionEducativa> listaInstitucionesCorrectas = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorNuevoSIGEPII());
                    if (listaInstitucionesCorrectas.size() < 1) {
                        logger.info("\n\nLA INSTITUCION EDUCATIVA CORRECTA NO EXISTE EN LA BASE DE DATOS EN ESTOS MOMENTOS : " + registro.getValorNuevoSIGEPII());
                        continue;
                    }
                    if (listaInstitucionesCorrectas.size() > 0 && registro.getValorNuevoSIGEPII() == null) {
                        for (InstitucionEducativa institucionEducativaNull : listaInstitucionesCorrectas) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                List<InstitucionEducativa> listaInstitucionRequiereCorreccion = institucionEducativaServiceDao.encontrarPorNombreInstitucion("REQUIERE CORRECCIÓN");
                                if (institucionEducativaNull.getProgramasAcademicos().size() > 0) {
                                    for (ProgramaAcademico programaAcademico : institucionEducativaNull.getProgramasAcademicos()) {
                                        logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getCodTituloAcademico() + " - " + programaAcademico.getNombreProgramaAcademico() + " - CANTIDAD ESTUDIANTES : [" + programaAcademico.getListaEduFormal().size() + "]");
                                        for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
                                            // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                                            logger.info("ESTUDIANTE : " + educacionFormal.getCodPersona());
                                        }
                                        if (programaAcademico.getListaEduFormal().size() > 0) {
                                            programaAcademicoServiceDao.actualizar(programaAcademico.getCodTituloAcademico(), listaInstitucionRequiereCorreccion.get(0));
                                            logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getNombreProgramaAcademico() + " | NUEVA INSTITUCION: " + programaAcademico.getInstitucionEdu().getCodInstitucionEducativa() + " - " + programaAcademico.getInstitucionEdu().getNombreInstitucion());
                                        }
                                        programaAcademicoServiceDao.eliminar(programaAcademico);
                                    }
                                }
                                logger.info("INSTITUCION EDUCATIVA NULL ELIMINADA : " + institucionEducativaNull.getCodInstitucionEducativa() + " - " + institucionEducativaNull.getNombreInstitucion());
                                institucionEducativaServiceDao.eliminar(institucionEducativaNull);
                            }
                        }
                    } else if (!listaInstitucionesCorrectas.isEmpty() && listaInstitucionesCorrectas != null && listaInstitucionesCorrectas.size() == 1) {
                        for (InstitucionEducativa institucionEducativa : listaInstitucionesXcorregir) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                logger.info("\n\n\n***************************************\n\nREGISTRO EXCEL MARCADO PARA BORRADO FISICO : " + registro.getValorActualSIGEPII());
                                // CONSULTA POR CADA PROGRAMA ACADEMICO ASOCIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
                                if (institucionEducativa.getProgramasAcademicos().size() > 0) {
                                    logger.info("INSTITUCIÓN EDUCATIVA CORRECTA: " + registro.getValorNuevoSIGEPII());
                                    for (ProgramaAcademico programaAcademico : institucionEducativa.getProgramasAcademicos()) {
                                        if (programaAcademico.getListaEduFormal().size() > 0) {
                                            logger.info("PROGRAMA ACADEMICO POR ACTUALIZAR: " + programaAcademico.getNombreProgramaAcademico() + " - CANTIDAD ESTUDIANTES: [" + programaAcademico.getListaEduFormal().size() + "]");
                                            programaAcademicoServiceDao.actualizar(programaAcademico.getCodTituloAcademico(), listaInstitucionesCorrectas.get(0));
                                            // AQUI SE DEBE CLONAR EL PROGRAMA ACADEMICO PARA PODER ACTUALIZAR LA FORANEA DE INSTITUCION EDUCATIVA Y POSTERIORMENTE HACER EL UPDATE
                                            for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
//                                                // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                                                logger.info("ESTUDIANTE : " + educacionFormal.getCodPersona());
                                            }
                                        }
                                        logger.info("PROGRAMA ACADEMICO ELIMINADO: " + programaAcademico.getNombreProgramaAcademico());
                                        programaAcademicoServiceDao.eliminar(programaAcademico);
                                    }
                                }
                                // ELIMINAR INTITUCION
                                logger.info("INSTITUCION EDUCATIVA ELIMINADA: " + institucionEducativa.getCodInstitucionEducativa() + " - " + institucionEducativa.getNombreInstitucion());
                                institucionEducativaServiceDao.eliminar(institucionEducativa);
                            } else {
                                logger.info("REGISTRO EXCEL NO ESTA MARCADO PARA BORRADO FISICO : " + registro.getValorActualSIGEPII());
                            }
                        }
                    } else if (!listaInstitucionesCorrectas.isEmpty() && listaInstitucionesCorrectas != null && listaInstitucionesCorrectas.size() > 1) {
                        for (InstitucionEducativa institucionesCorrecta : listaInstitucionesCorrectas) {
                            registrosDuplicados.add(institucionesCorrecta);
                        }
                    }
                }
                if (registrosDuplicados.size() > 0) {
                    logger.info("\nSE ENCONTRO MAS DE UNA INSTITUCION EDUCATIVA CORRECTA, POR FAVOR VALIDAR:\n");
                    for (InstitucionEducativa registrosDuplicado : registrosDuplicados) {
                        logger.info("INSTITUCION EDUCATIVA CORRECTA REPETIDA: " + registrosDuplicado.getNombreInstitucion()
                                + " - CODIGO INSTITUCION EDUCATIVA: " + registrosDuplicado.getCodInstitucionEducativa());
                    }
                }
            }
            logger.info("\n\n************************** ARCHIVO PROCESADO CON EXITO! **************************\n");
            long fin = System.currentTimeMillis();
            Long tiempo = (Long) (((fin - inicio) / 1000) / 60);
            logger.info("TIEMPO DEL PROCESO : " + tiempo + " MINUTOS");
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return registrosDuplicados;
    }
}
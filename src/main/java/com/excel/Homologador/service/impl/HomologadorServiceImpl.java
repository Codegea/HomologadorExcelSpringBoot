package com.excel.Homologador.service.impl;

import com.excel.Homologador.dao.service.InstitucionEducativaServiceDao;
import com.excel.Homologador.dao.service.ProgramaAcademicoServiceDao;
import com.excel.Homologador.dto.RegistrosDto;
import com.excel.Homologador.entity.EducacionFormal;
import com.excel.Homologador.entity.InstitucionEducativa;
import com.excel.Homologador.entity.ProgramaAcademico;
import com.excel.Homologador.properties.ParametrizacionProperties;
import com.excel.Homologador.utils.XLSX2CSV;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.excel.Homologador.service.IHomologadorService;

@Service
public class HomologadorServiceImpl implements IHomologadorService {

    @Autowired
    InstitucionEducativaServiceDao institucionEducativaServiceDao;

    @Autowired
    ProgramaAcademicoServiceDao programaAcademicoServiceDao;

    @Autowired
    ParametrizacionProperties propiedades;

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
            List<RegistrosDto> registros = XLSX2CSV.ProcesarExcel(propiedades.getPathFicheroExcel());
            if (!registros.isEmpty()) {
                long inicio = System.currentTimeMillis();
                Map<Long, String> institucionXeliminar = new HashMap<>();
                for (RegistrosDto registro : registros) {
                    List<InstitucionEducativa> listaInstitucionesXcorregir = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorActualSIGEPII());
                    List<InstitucionEducativa> listaInstitucionesCorrectas = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorNuevoSIGEPII());
                    if (!listaInstitucionesCorrectas.isEmpty() && listaInstitucionesCorrectas != null && listaInstitucionesCorrectas.size() == 1) {
                        for (InstitucionEducativa institucionEducativa : listaInstitucionesXcorregir) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                logger.info("\n\n\n***************************************\n\nREGISTRO EXCEL MARCADO PARA BORRADO FISICO NUEVO VALOR : " + registro.getValorNuevoSIGEPII());
                                logger.info("INSTITUCION EDUCATIVA ITERADA X CORREGIR : " + institucionEducativa.getCodInstitucionEducativa() + " - " + institucionEducativa.getNombreInstitucion());
                                // CONSULTA POR CADA PROGRAMA ACADEMICO ASOCIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
                                logger.info("TOTAL PROGRAMAS ACADEMICOS : [" + institucionEducativa.getProgramasAcademicos().size() + "]");
                                if (institucionEducativa.getProgramasAcademicos().size() > 0) {
                                    for (ProgramaAcademico programaAcademico : institucionEducativa.getProgramasAcademicos()) {
                                        logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getCodTituloAcademico() + " - " + programaAcademico.getNombreProgramaAcademico() + " - ESTUDIANTES : [" + programaAcademico.getListaEduFormal().size() + "]");
                                        for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
//                                                // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                                            logger.info("ESTUDIANTE : " + educacionFormal.getCodPersona());
                                        }
                                        if (programaAcademico.getListaEduFormal().size() > 0) {
                                            ProgramaAcademico programaActualizado = programaAcademicoServiceDao.actualizar(programaAcademico.getCodTituloAcademico(), listaInstitucionesCorrectas.get(0));
                                            logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getNombreProgramaAcademico() + " | NUEVA INSTITUCION: " + programaActualizado.getInstitucionEdu().getCodInstitucionEducativa() + " - " + programaActualizado.getInstitucionEdu().getNombreInstitucion());
                                        }
                                    }
                                }
                                institucionXeliminar.put(institucionEducativa.getCodInstitucionEducativa(), institucionEducativa.getNombreInstitucion());
                            } else {
                                if (registro.getModificar() != null) {
                                    // ESTE REGISTRO NO TIENE MARCA DE ELIMINADO FISICO POR LO TANTO SOLO SE HOMOLOGA SU NOMBRE ACTUAL POR EL NUEVO.
                                    if (!registro.getModificar().equalsIgnoreCase("NO") || !registro.getModificar().equalsIgnoreCase("X")) {
                                        institucionEducativa.setNombreInstitucion(registro.getValorNuevoSIGEPII());
                                        institucionEducativaServiceDao.guardar(institucionEducativa);
                                        logger.info("INSTITUCION EDUCATIVA HOMOLOGADA : " + institucionEducativa.getCodInstitucionEducativa() + " - " + institucionEducativa.getNombreInstitucion());
                                    }
                                }
                            }
                        }
                    } else {
                        if (listaInstitucionesCorrectas.isEmpty() || listaInstitucionesCorrectas.size() == 0) {
                            logger.info("\n\n******* NO SE ENCONTRO EL REGISTRO PARA ESTE NOMBRE DE INSTITUCION EDUCATIVA CORRECTA : " + registro.getValorNuevoSIGEPII() + "\n\n");
                        } else {
                            for (InstitucionEducativa institucionesCorrecta : listaInstitucionesCorrectas) {
                                registrosDuplicados.add(institucionesCorrecta);
                            }
                        }
                    }
                }
                if (institucionXeliminar.size() > 0) {
                    for (Map.Entry<Long, String> entry : institucionXeliminar.entrySet()) {
                        logger.info("SE PROCEDE A ELIMINAR LA INSTITUCION EDUCATIVA: " + entry.getKey() + " - " + entry.getValue());
                        InstitucionEducativa institucionEliminar = institucionEducativaServiceDao.buscarInstitucionPorId(entry.getKey());
                        institucionEducativaServiceDao.eliminar(institucionEliminar);
                    }
                }
                if (registrosDuplicados.size() > 0) {
                    logger.info("\n\n ************* Se encontro mas de una Institucion Educativa Correcta, por favor validar ************* \n\n ");
                    for (InstitucionEducativa registrosDuplicado : registrosDuplicados) {
                        logger.info("INSTITUCION REPETIDA: " + registrosDuplicado.getNombreInstitucion()
                                + " - COD_INSTITUCION_EDUCATIVA: " + registrosDuplicado.getCodInstitucionEducativa());
                    }
                }
                logger.info("\n************************** ARCHIVO PROCESADO CON EXITO! **************************\n");
                long fin = System.currentTimeMillis();
                double tiempo = (double) ((inicio - fin) / 1000);
                logger.info("Tiempo : " + tiempo);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return registrosDuplicados;
    }
}

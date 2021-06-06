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
                    if (listaInstitucionesXcorregir.size() < 1) {
                        logger.info("\n\nLA INSTITUCION EDUCATIVA POR CORREGIR NO EXISTE EN LA BASE DE DATOS EN ESTOS MOMENTOS : " + registro.getValorActualSIGEPII());
                        continue;
                    }
                    List<InstitucionEducativa> listaInstitucionesCorrectas = institucionEducativaServiceDao.encontrarPorNombreInstitucion(registro.getValorNuevoSIGEPII());
                    if (registro.getValorNuevoSIGEPII() == null) {
                        for (InstitucionEducativa institucionEducativaNull : listaInstitucionesCorrectas) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                List<InstitucionEducativa> listaInstitucionRequiereCorreccion = institucionEducativaServiceDao.encontrarPorNombreInstitucion("REQUIERE CORRECCIÓN");
                                procesarRegistroBorradoFisicoNull(institucionEducativaNull, listaInstitucionRequiereCorreccion);
                                institucionXeliminar.put(institucionEducativaNull.getCodInstitucionEducativa(), institucionEducativaNull.getNombreInstitucion());
                            }
                        }
                    } else if (!listaInstitucionesCorrectas.isEmpty() && listaInstitucionesCorrectas != null && listaInstitucionesCorrectas.size() == 1) {
                        for (InstitucionEducativa institucionEducativa : listaInstitucionesXcorregir) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                procesarRegistroBorradoFisico(institucionEducativa, listaInstitucionesCorrectas, registro);
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
                    } else if (registro.getValorNuevoSIGEPII() == null && listaInstitucionesCorrectas.size() > 1) {
                        for (InstitucionEducativa institucionEducativa : listaInstitucionesXcorregir) {
                            procesarRegistroBorradoFisico(institucionEducativa, listaInstitucionesCorrectas, registro);
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
                    logger.info("\n\n ************* SE ENCONTRO MAS DE UNA INSTITUCION EDUCATIVA CORRECTA, POR FAVOR VALIDAR ************* \n\n ");
                    for (InstitucionEducativa registrosDuplicado : registrosDuplicados) {
                        logger.info("INSTITUCION REPETIDA: " + registrosDuplicado.getNombreInstitucion()
                                + " - COD_INSTITUCION_EDUCATIVA: " + registrosDuplicado.getCodInstitucionEducativa());
                    }
                }
                logger.info("\n************************** ARCHIVO PROCESADO CON EXITO! **************************\n");
                long fin = System.currentTimeMillis();
                double tiempo = (double) (((fin - inicio) / 1000) / 60);
                logger.info("TIEMPO DEL PROCESO : " + tiempo + " Minutos");
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return registrosDuplicados;
    }

    private void procesarRegistroBorradoFisico(InstitucionEducativa institucionEducativaXcorregir, List<InstitucionEducativa> listaInstitucionesCorrectas, RegistrosDto registro) {
        ProgramaAcademico programaActualizado = null;
        logger.info("\n\n\n***************************************\n\nREGISTRO EXCEL MARCADO PARA BORRADO FÍSICO, NUEVO VALOR : " + registro.getValorNuevoSIGEPII());
        logger.info("INSTITUCION EDUCATIVA ITERADA POR CORREGIR : " + institucionEducativaXcorregir.getCodInstitucionEducativa() + " - " + institucionEducativaXcorregir.getNombreInstitucion());
        // CONSULTA POR CADA PROGRAMA ACADEMICO ASOCIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
        logger.info("TOTAL PROGRAMAS ACADEMICOS : [" + institucionEducativaXcorregir.getProgramasAcademicos().size() + "]");

        if (institucionEducativaXcorregir.getProgramasAcademicos().size() > 0) {
            for (ProgramaAcademico programaAcademico : institucionEducativaXcorregir.getProgramasAcademicos()) {
                logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getCodTituloAcademico() + " - " + programaAcademico.getNombreProgramaAcademico() + " - ESTUDIANTES : [" + programaAcademico.getListaEduFormal().size() + "]");
                for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
                    // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                    logger.info("ESTUDIANTE : " + educacionFormal.getCodPersona());
                }
                if (programaAcademico.getListaEduFormal().size() > 0) {
                    programaActualizado = programaAcademicoServiceDao.actualizar(programaAcademico.getCodTituloAcademico(), listaInstitucionesCorrectas.get(0));
                    logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getNombreProgramaAcademico() + " | NUEVA INSTITUCION: " + programaActualizado.getInstitucionEdu().getCodInstitucionEducativa() + " - " + programaActualizado.getInstitucionEdu().getNombreInstitucion());
                }
            }
        }
    }

    /**
     * Metodo que procesa la logica para que cuando un registro de institucion
     * educativa correcta sea NULL se deba asociar sus programas academicos a la
     * institucion REQUIERE CORRECCION siempre y cuando tenga estudiantes.
     *
     * @param institucionEducativaNull
     * @param listaInstitucionesRequiereCorreccion
     */
    private void procesarRegistroBorradoFisicoNull(InstitucionEducativa institucionEducativaNull, List<InstitucionEducativa> listaInstitucionesRequiereCorreccion) {
        ProgramaAcademico programaActualizado = null;
        logger.info("\n\n\n***************************************\n\nREGISTRO EXCEL MARCADO PARA BORRADO FÍSICO, NUEVO VALOR REQUIERE CORECCION X REGISTRO NULL\n");
        logger.info("INSTITUCION EDUCATIVA NULL ITERADA POR CORREGIR : " + institucionEducativaNull.getCodInstitucionEducativa() + " - " + institucionEducativaNull.getNombreInstitucion());
        // CONSULTA POR CADA PROGRAMA ACADEMICO ASOCIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
        logger.info("TOTAL PROGRAMAS ACADEMICOS : [" + institucionEducativaNull.getProgramasAcademicos().size() + "]");

        if (institucionEducativaNull.getProgramasAcademicos().size() > 0) {
            for (ProgramaAcademico programaAcademico : institucionEducativaNull.getProgramasAcademicos()) {
                logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getCodTituloAcademico() + " - " + programaAcademico.getNombreProgramaAcademico() + " - ESTUDIANTES : [" + programaAcademico.getListaEduFormal().size() + "]");
                for (EducacionFormal educacionFormal : programaAcademico.getListaEduFormal()) {
                    // *** este logger es informativo para que se evidencie si hay registros en educacion formal
                    logger.info("ESTUDIANTE : " + educacionFormal.getCodPersona());
                }
                if (programaAcademico.getListaEduFormal().size() > 0) {
                    programaActualizado = programaAcademicoServiceDao.actualizar(programaAcademico.getCodTituloAcademico(), listaInstitucionesRequiereCorreccion.get(0));
                    logger.info("PROGRAMA ACADEMICO : " + programaAcademico.getNombreProgramaAcademico() + " | NUEVA INSTITUCION: " + programaActualizado.getInstitucionEdu().getCodInstitucionEducativa() + " - " + programaActualizado.getInstitucionEdu().getNombreInstitucion());
                }
            }
        }
    }
}

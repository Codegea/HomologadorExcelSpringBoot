package com.excel.Homologador.service.impl;

import com.excel.Homologador.dao.IInstitucionEducativaDao;
import com.excel.Homologador.dto.RegistrosDto;
import com.excel.Homologador.entity.InstitucionEducativa;
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

@Service
public class InstitucionEducativaServiceImpl implements IInstitucionEducativaService {

    @Autowired
    IInstitucionEducativaDao institucionEducativaDao;

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
    public boolean homologarFichero() {
        try {
            List<RegistrosDto> registros = XLSX2CSV.ProcesarExcel();
            if (!registros.isEmpty()) {
                for (RegistrosDto registro : registros) {
                    List<InstitucionEducativa> listaInstitucionEducativa = institucionEducativaDao.findByNombreInstitucion(registro.getValorActualSIGEPII());

                    if (!listaInstitucionEducativa.isEmpty() && listaInstitucionEducativa != null && listaInstitucionEducativa.size() < 2) {
                        for (InstitucionEducativa institucionEducativa : listaInstitucionEducativa) {
                            if (registro.getElimBorradoFisico().equals("SI")) {
                                // CONSULTA POR CADA PROGRAMA ACADEMICO ASICIADO SI DENTRO DE EL HAY REGISTROS DE EDUCACION FORMAL
                                // SI EL RESULTADO ES VACIO ENTONCES SE DEBE ELIMINAR LA INSTITUCION EDUCATIVA Y TODAS SUS DEPENDENCIAS

                                // SI ENCONTRO REGISTROS AQUI DEBEMOS 
                                // CONSULTAR EN BASE DE DATOS AL REGISTRO EN INSTITUCION EDUCATIVA = "REQUIERE CORRECCION" 
                                // Y OBTENEMOS EL VALOR DEL ID.
                                // AHORA DEBEMOS ASOCIAR TODOS LOS PROGRAMAS ACADEMICOS Y EDICACION FORMAL A ESTE TIPO
                                // DE INSTITUCION EDUCATIVA "REQUIERE CORRECCION", 
                                // Y POR ULTIMO DEBEMOS ELIMINAR ESE REGISTRO MALO 
                                // EN LA TABLA INSTITUCION EDUCATIVA
                            }

//                      logger.info("Registro encontrado : " + institucionEducativa.toString());
                            institucionEducativa.setNombreInstitucion(registro.getValorNuevoSIGEPII());
                            logger.info("\n\n\n***************************************\n\nInstitucionEducativa OK: " + registro.getValorNuevoSIGEPII());
                            logger.info("InstitucionEducativa CORREGIR: " + registro.getValorActualSIGEPII());
                            logger.info("Cantidad de programas academicos encontrados para ajustar: " + institucionEducativa.getProgramasAcademicos().size());
                            System.out.println("Institucion Educativa encontrada: " + institucionEducativa);
//                      InstitucionEducativa registroProcesado = institucionEducativaDao.save(institucionEducativa);
//                      logger.info("Registro procesado : " + registroProcesado.toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            return false;
        }
        return true;
    }
}

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

    @Override
    public boolean homologarFichero() {
        try {
            List<RegistrosDto> registros = XLSX2CSV.ProcesarExcel();
            if (!registros.isEmpty()) {
                for (RegistrosDto registro : registros) {
                    InstitucionEducativa institucionEducativa = institucionEducativaDao.findByNombreInstitucion(registro.getValorActualSIGEPII());
                    if (institucionEducativa != null) {
                        logger.info("Registro encontrado : " + institucionEducativa.toString());
                        institucionEducativa.setNombreInstitucion(registro.getValorNuevoSIGEPII());
                        InstitucionEducativa registroProcesado = institucionEducativaDao.save(institucionEducativa);
                        logger.info("Registro procesado : " + registroProcesado.toString());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return true;
    }
}

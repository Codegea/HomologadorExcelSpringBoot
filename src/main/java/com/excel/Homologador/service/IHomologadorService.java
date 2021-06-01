package com.excel.Homologador.service;

import com.excel.Homologador.entity.InstitucionEducativa;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface IHomologadorService {

    public StringBuilder uploadFile(MultipartFile file, RedirectAttributes attributes) throws IOException;

    public List<InstitucionEducativa> homologarFichero();

}

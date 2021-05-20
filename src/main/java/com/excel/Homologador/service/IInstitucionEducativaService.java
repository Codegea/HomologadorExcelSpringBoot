package com.excel.Homologador.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface IInstitucionEducativaService {
    
    public StringBuilder uploadFile(MultipartFile file, RedirectAttributes attributes) throws IOException;
    
    public boolean homologarFichero();
    
}

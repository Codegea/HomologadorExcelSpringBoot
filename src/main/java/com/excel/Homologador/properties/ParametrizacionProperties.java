package com.excel.Homologador.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ParametrizacionProperties {

    @Value("${homologador.path.excel.file}")
    private String pathFicheroExcel;
}

package com.github.zulisses.res_with_spring_boot.file.exporter.impl;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.file.exporter.contract.FileExporter;

@Component
public class XlsxExporter implements FileExporter { 

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        return null;
    }
}

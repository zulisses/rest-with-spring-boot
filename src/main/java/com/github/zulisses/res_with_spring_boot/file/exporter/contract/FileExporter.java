package com.github.zulisses.res_with_spring_boot.file.exporter.contract;

import java.util.List;

import org.springframework.core.io.Resource;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}

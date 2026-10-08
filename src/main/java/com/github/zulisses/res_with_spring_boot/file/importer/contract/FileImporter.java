package com.github.zulisses.res_with_spring_boot.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}

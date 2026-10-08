package com.github.zulisses.res_with_spring_boot.file.importer.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.zulisses.res_with_spring_boot.file.importer.contract.FileImporter;
import java.util.List;
import java.io.InputStream;
import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.file.importer.impl.CsvImporter;
import com.github.zulisses.res_with_spring_boot.file.importer.impl.XlsxImporter;
import com.github.zulisses.res_with_spring_boot.exception.BadRequestException;

@Component
public class FileImporterFactory implements FileImporter{

   private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

   @Autowired
   private ApplicationContext context;

   public FileImporter getImporter(String fileName) throws Exception {
    if (fileName.endsWith(".xlsx")) {
        return context.getBean(XlsxImporter.class);
    } else if (fileName.endsWith(".csv")) {
        return context.getBean(CsvImporter.class);
    } else {
        throw new BadRequestException();
    }
   }

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return null;
    }
}

package com.github.zulisses.res_with_spring_boot.file.exporter.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.zulisses.res_with_spring_boot.exception.BadRequestException;
import com.github.zulisses.res_with_spring_boot.file.exporter.contract.FileExporter;
import com.github.zulisses.res_with_spring_boot.file.exporter.impl.CsvExporter;
import com.github.zulisses.res_with_spring_boot.file.exporter.impl.XlsxExporter;
import com.github.zulisses.res_with_spring_boot.file.exporter.MediaTypes;

@Component
public class FileExporterFactory {

   private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

   @Autowired
   private ApplicationContext context;

   public FileExporter getExporter(String acceptHeader) throws Exception {
    if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
        return context.getBean(XlsxExporter.class);
    } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
        return context.getBean(CsvExporter.class);
    } else {
        throw new BadRequestException();
    }
   }
}

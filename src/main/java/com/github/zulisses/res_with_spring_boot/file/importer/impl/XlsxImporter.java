package com.github.zulisses.res_with_spring_boot.file.importer.impl;

import com.github.zulisses.res_with_spring_boot.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;

@Component
public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) { 
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowItaretor = sheet.iterator();

            if(rowItaretor.hasNext()) rowItaretor.next();

            return parseRowsToPersonDTOs(rowItaretor);
        }
    } 

    private List<PersonDTO> parseRowsToPersonDTOs(Iterator<Row> rowItaretor) {
        List<PersonDTO> people = new ArrayList<>();

        rowItaretor.forEachRemaining(row -> {
            if(isRowValid(row)) {
                PersonDTO person = new PersonDTO();
                person.setFirstName(row.getCell(0).getStringCellValue());
                person.setLastName(row.getCell(1).getStringCellValue());
                person.setAddress(row.getCell(2).getStringCellValue());
                person.setGender(row.getCell(3).getStringCellValue());
                person.setEnabled(true);
                people.add(person);
            }
        });

        return people;
    }

    private boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}

package com.github.zulisses.res_with_spring_boot.file.importer.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.zulisses.res_with_spring_boot.data.dto.v1.PersonDTO;
import com.github.zulisses.res_with_spring_boot.file.importer.contract.FileImporter;

@Component
public class CsvImporter implements FileImporter {
    private Logger logger = LoggerFactory.getLogger(CsvImporter.class);

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        logger.info("Importing file!");
        CSVFormat format = CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build();

        Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
        logger.info("Records: {}", records);
        
        return parseRecordsToPersonDTOs(records);
    } 

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();

        records.forEach(record -> {
            logger.info("Record: {}", record);
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);
            people.add(person);
        });

        return people;
    }
}

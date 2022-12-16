package com.sirma.commonProjects.service;

import com.sirma.commonProjects.model.Employee;
import com.sirma.commonProjects.util.DateUtil;
import com.sirma.commonProjects.util.EmployeeValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class for {@link FileObjectMapper} using CSV files
 * and mapping each row to the {@link Employee} class.
 */
@Slf4j
@Service
public class CsvFileEmployeeMapperImpl implements FileObjectMapper<Employee> {

    @Override
    public List<Employee> convertFileContentToModel(MultipartFile file, String dateTimeFormatter) throws IOException {

        log.info("action=convertFileContentToModel fileContentType={} dateTimeFormatter={}",
                file.getContentType(), dateTimeFormatter);

        // parse CSV file to create a list of model objects
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        List<CSVRecord> records = new CSVParser(reader, CSVFormat.DEFAULT.withTrim()).getRecords();

        List<Employee> employees = new ArrayList<>();

        records.forEach(csvRecord -> {
            long employeeId = Long.parseLong(csvRecord.get(0));
            long projectId = Long.parseLong(csvRecord.get(1));
            LocalDate startDate = DateUtil.parseDateFromString(csvRecord.get(2), dateTimeFormatter);
            LocalDate endDate = DateUtil.parseDateFromString(csvRecord.get(3), dateTimeFormatter);

            DateUtil.validateDateRange(startDate, endDate);

            Employee newEmployee = new Employee(
                    employeeId,
                    projectId,
                    startDate,
                    endDate);

            EmployeeValidationUtil.validateEmployeeHasNoOverlappingProjectPeriods(employees, newEmployee);

            employees.add(newEmployee);
        });

        log.info("action=convertFileContentToModel result=success employees={}", employees.size());
        return employees;
    }
}

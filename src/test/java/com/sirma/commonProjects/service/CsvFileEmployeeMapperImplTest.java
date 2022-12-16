package com.sirma.commonProjects.service;

import com.sirma.commonProjects.model.Employee;
import com.sirma.commonProjects.util.MultipartFileGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link CsvFileEmployeeMapperImpl}
 */
@SpringBootTest
class CsvFileEmployeeMapperImplTest {

    private static final String VALID_ISO_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    @Autowired
    private CsvFileEmployeeMapperImpl csvFileEmployeeMapper;

    @Test
    void shouldThrowNumberFormatExceptionWhenEmployeeIdIsNotAnInteger() {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("employeeId,2,2000-01-01,2000-01-01");
        Exception exception = assertThrows(NumberFormatException.class, () ->
                csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN));

        String expectedMessage = "For input string: \"employeeId\"";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void shouldThrowNumberFormatExceptionWhenProjectIdIsNotAnInteger() {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,projectId,2000-01-01,2000-01-01");
        Exception exception = assertThrows(NumberFormatException.class, () ->
                csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN));

        String expectedMessage = "For input string: \"projectId\"";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void shouldThrowDateTimeParseExceptionWhenDateHasDifferentDateFormat() {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000/01/01,2000-01-01");
        Exception exception = assertThrows(DateTimeParseException.class, () ->
                csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN));

        String expectedMessage = "Text '2000/01/01' could not be parsed at index 4";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void shouldThrowIndexOutOfBoundsExceptionWhenMissingDataInRow() {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01");
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN));

        String expectedMessage = "Index 3 out of bounds for length 3";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenRecordsWithTheSameProjectIdAndEmployeeIdOverlap() {
        MockMultipartFile file = MultipartFileGenerator
                .buildMockFile("1,2,2000-01-01,2000-01-04\n1,2,2000-01-03,2000-01-05");

        Exception exception = assertThrows(IllegalStateException.class, () ->
                csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN));

        String expectedMessage = "employee has overlapping project working periods " +
                "firstRecord=Employee(employeeId=1, projectId=2, startDate=2000-01-01, endDate=2000-01-04) " +
                "secondRecord=Employee(employeeId=1, projectId=2, startDate=2000-01-03, endDate=2000-01-05)";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void shouldReturnRecordWhenGivenNullValueAsDate() throws IOException {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01, NULL");
        List<Employee> employees = csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN);

        assertThat(employees, hasSize(1));
    }

    @Test
    void shouldReturnRecordWhenGivenCasInsensitiveNullValueAsDate() throws IOException {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01, Null");
        List<Employee> employees = csvFileEmployeeMapper.convertFileContentToModel(file, VALID_ISO_DATE_FORMAT_PATTERN);

        assertThat(employees, hasSize(1));
    }
}
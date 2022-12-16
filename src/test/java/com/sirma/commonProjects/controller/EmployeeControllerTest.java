package com.sirma.commonProjects.controller;

import com.sirma.commonProjects.model.ColleaguesView;
import com.sirma.commonProjects.util.MultipartFileGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the presentation and business layers.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    private static final String VALID_ISO_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String INVALID_DATE_FORMAT_PATTERN = "abcd-ef-gh";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnIndexViewNameWhenCallingIndexPage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenNoDateFormatPatternIsProvided() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", EMPTY))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("csv file and date format pattern must be provided")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenEmptyFileIsProvided() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile(EMPTY);

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("csv file and date format pattern must be provided")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenGivenInvalidDateFormatPattern() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", INVALID_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Unknown pattern letter: b")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenMissingParameterInFileRow() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Missing data for particular record. " +
                        "Row data should be as follows (EmployeeId, ProjectId, StartDate, EndDate)")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenUsingDifferentDateFormat() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,2000/01/01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Incorrect data provided.")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenEmployeeIdIsNotAnInteger() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("employeeId,2,2000-01-01,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Incorrect data provided.")));
    }

    @Test
    void shouldReturnBadRequestAndIndexPageWhenProjectIdIsNotAnInteger() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,projectId,2000-01-01,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Incorrect data provided.")));
    }

    @Test
    void shouldReturnOkWhenGivenNullValueAsDate() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,NULL");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isOk())
                .andExpect(view().name("datagrid"));
    }

    @Test
    void shouldReturnOkWhenGivenCaseInsensitiveNullValueAsDate() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,NuLl");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isOk())
                .andExpect(view().name("datagrid"));
    }

    @Test
    void shouldReturnOkAndEmptyDataGridPageWhenGivenOneRecordInFile() throws Exception {
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,2,2000-01-01,2000-01-01");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andExpect(status().isOk())
                .andExpect(view().name("datagrid"));
    }

    @Test
    void shouldReturnOkAndOneRecordInDataGridPageWhenGivenTwoOverlappingWorkingProjects() throws Exception {
        ColleaguesView colleaguesView = new ColleaguesView(1,2,10,4);
        MockMultipartFile file = MultipartFileGenerator.buildMockFile("1,10,2000-01-01,2000-01-02\n2,10,2000-01-03,2000-01-04");

        this.mockMvc.perform(multipart("/datagrid")
                        .file(file)
                        .param("dateFormat", VALID_ISO_DATE_FORMAT_PATTERN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("datagrid"))
                .andExpect(model().attribute("colleaguesViews", Collections.singletonList(colleaguesView)))
                .andExpect(model().attribute("colleaguesViews", Matchers.hasSize(1)));
    }


}
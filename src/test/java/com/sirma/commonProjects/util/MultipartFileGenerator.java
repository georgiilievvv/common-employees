package com.sirma.commonProjects.util;

import org.springframework.mock.web.MockMultipartFile;

public final class MultipartFileGenerator {

     public static MockMultipartFile buildMockFile(String content) {
        return new MockMultipartFile(
                "file",
                "hello.csv",
                "text/csv",
                content.getBytes()
        );
    }
}

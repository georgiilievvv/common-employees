package com.sirma.commonProjects.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * Interface for converting file content to model class representations.
 */
public interface FileObjectMapper<T> {

    List<T> convertFileContentToModel(MultipartFile file, String dateFormat) throws IOException;
}

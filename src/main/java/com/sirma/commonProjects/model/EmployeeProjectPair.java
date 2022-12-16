package com.sirma.commonProjects.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Grouping class for employees and projects.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class EmployeeProjectPair {
    private long employeeId;
    private long projectId;
}

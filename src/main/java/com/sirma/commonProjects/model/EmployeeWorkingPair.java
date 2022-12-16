package com.sirma.commonProjects.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * View model used for the datagrid.
 */
@AllArgsConstructor
@Getter
public class EmployeeWorkingPair {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private long projectId;
    private long daysWorkingTogether;
}

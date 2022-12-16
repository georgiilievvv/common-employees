package com.sirma.commonProjects.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * View model used for the datagrid.
 */
@AllArgsConstructor
@Data
public class ColleaguesView {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private long projectId;
    private long daysWorkingTogether;
}

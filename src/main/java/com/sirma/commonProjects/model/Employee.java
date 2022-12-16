package com.sirma.commonProjects.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Model representing data in the provided input file.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Employee {

    private long employeeId;
    private long projectId;
    private LocalDate startDate;
    private LocalDate endDate;

    public long getDaysWorked() {
        return ChronoUnit.DAYS.between(startDate, endDate)+1;
    }
}

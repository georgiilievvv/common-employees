package com.sirma.commonProjects.util;

import com.sirma.commonProjects.model.Employee;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class EmployeeValidationUtil {

    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException("start date is after end date");
        }
    }

    public static void validateEmployeeHasNoOverlappingProjectPeriods(List<Employee> validEmployees, Employee newEmployee) {
        validEmployees.stream()
                .filter(employee ->
                        employee.getEmployeeId() == newEmployee.getEmployeeId() &&
                                employee.getProjectId() == newEmployee.getProjectId())
                .forEach(employee -> {
                    boolean hasOverlappingDateRanges = DateUtil.hasOverlappingDateRanges(
                            employee.getStartDate(), employee.getEndDate(),
                            newEmployee.getStartDate(), newEmployee.getEndDate());

                    if (hasOverlappingDateRanges) {
                        String message = String
                                .format("employee has overlapping project working periods firstRecord=%s secondRecord=%s",
                                employee, newEmployee);

                        log.error("action=validateEmployeeHasNoOverlappingProjectPeriods result=error message={}", message);
                        throw new IllegalStateException(message);
                    }
                });
    }
}

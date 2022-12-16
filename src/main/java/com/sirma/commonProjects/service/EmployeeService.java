package com.sirma.commonProjects.service;

import com.sirma.commonProjects.model.Employee;
import com.sirma.commonProjects.model.EmployeeProjectPair;
import com.sirma.commonProjects.model.EmployeeWorkingPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service class responsible for employee data manipulation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    public List<EmployeeWorkingPair> extractEmployeesWorkingOnSameProject(List<Employee> employees) {

        log.info("action=extractEmployeesWorkingOnSameProject employees={}", employees.size());
        List<EmployeeWorkingPair> employeeWorkingPairs = new ArrayList<>();

        Map<EmployeeProjectPair, Long> groupedEmployeeProjects = employees.stream()
                .collect(Collectors
                        .groupingBy(e -> new EmployeeProjectPair(e.getEmployeeId(), e.getProjectId()),
                                Collectors.summingLong(Employee::getDaysWorked)));

        List<EmployeeProjectPair> employeeProjectPairs = new ArrayList<>(groupedEmployeeProjects.keySet());

        for (int i = 0; i < employeeProjectPairs.size() - 1; i++) {
            for (int j = i + 1; j < employeeProjectPairs.size(); j++) {
                EmployeeProjectPair employee = employeeProjectPairs.get(i);
                EmployeeProjectPair colleague = employeeProjectPairs.get(j);

                if (employee.getProjectId() == colleague.getProjectId()) {
                    long daysWorkingTogether = groupedEmployeeProjects.get(employee) + groupedEmployeeProjects.get(colleague);

                    EmployeeWorkingPair employeeWorkingPair = new EmployeeWorkingPair(
                            employee.getEmployeeId(),
                            colleague.getEmployeeId(),
                            employee.getProjectId(),
                            daysWorkingTogether);

                    employeeWorkingPairs.add(employeeWorkingPair);
                }
            }
        }
        log.info("action=extractEmployeesWorkingOnSameProject result=success employeeWorkingPairs={}", employeeWorkingPairs.size());
        return employeeWorkingPairs;
    }
}

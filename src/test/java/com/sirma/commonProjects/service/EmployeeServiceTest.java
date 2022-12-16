package com.sirma.commonProjects.service;

import com.sirma.commonProjects.model.ColleaguesView;
import com.sirma.commonProjects.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link EmployeeService}
 */
@SpringBootTest
class EmployeeServiceTest {
    @Mock
    private Employee firstEmployee;
    @Mock
    private Employee secondEmployee;
    @Mock
    private Employee thirdEmployee;

    private List<Employee> inputEmployees;

    private static final long FIRST_EMPLOYEE_ID = 1;
    private static final long SECOND_EMPLOYEE_ID = 2;
    private static final long THIRD_EMPLOYEE_ID = 3;

    private static final long FIRST_PROJECT_ID = 11;
    private static final long SECOND_PROJECT_ID = 12;
    private static final long THIRD_PROJECT_ID = 13;

    private static final long FIRST_EMPLOYEE_DAYS_WORKED_ON_PROJECT = 100;
    private static final long SECOND_EMPLOYEE_DAYS_WORKED_ON_PROJECT = 200;
    private static final long THIRD_EMPLOYEE_DAYS_WORKED_ON_PROJECT = 300;

    @BeforeEach
    void setUp() {
        when(firstEmployee.getEmployeeId()).thenReturn(FIRST_EMPLOYEE_ID);
        when(secondEmployee.getEmployeeId()).thenReturn(SECOND_EMPLOYEE_ID);
        when(thirdEmployee.getEmployeeId()).thenReturn(THIRD_EMPLOYEE_ID);

        when(firstEmployee.getProjectId()).thenReturn(FIRST_PROJECT_ID);
        when(secondEmployee.getProjectId()).thenReturn(SECOND_PROJECT_ID);

        when(firstEmployee.getDaysWorked()).thenReturn(FIRST_EMPLOYEE_DAYS_WORKED_ON_PROJECT);

        when(secondEmployee.getDaysWorked()).thenReturn(SECOND_EMPLOYEE_DAYS_WORKED_ON_PROJECT);

        when(thirdEmployee.getDaysWorked()).thenReturn(THIRD_EMPLOYEE_DAYS_WORKED_ON_PROJECT);

        inputEmployees = List.of(firstEmployee, secondEmployee, thirdEmployee);
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesAreProvided() {
        List<Employee> emptyList = Collections.emptyList();

        List<ColleaguesView> colleaguesViews = EmployeeService.extractEmployeesWorkingOnSameProject(emptyList);

        assertThat(colleaguesViews).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesWorkOnTheSameProject() {
        when(thirdEmployee.getProjectId()).thenReturn(THIRD_PROJECT_ID);

        List<ColleaguesView> colleaguesViews = EmployeeService.extractEmployeesWorkingOnSameProject(inputEmployees);

        assertThat(colleaguesViews).isEmpty();
    }

    @Test
    void shouldReturnOnePairWhenTwoEmployeesWorkOnSameProject() {
        when(thirdEmployee.getProjectId()).thenReturn(FIRST_PROJECT_ID);

        List<ColleaguesView> colleaguesViews = EmployeeService.extractEmployeesWorkingOnSameProject(inputEmployees);

        assertThat(colleaguesViews).hasSize(1);
        assertThat(colleaguesViews.get(0).getFirstEmployeeId()).isEqualTo(FIRST_EMPLOYEE_ID);
        assertThat(colleaguesViews.get(0).getSecondEmployeeId()).isEqualTo(THIRD_EMPLOYEE_ID);
        assertThat(colleaguesViews.get(0).getProjectId()).isEqualTo(FIRST_PROJECT_ID);
        assertThat(colleaguesViews.get(0).getDaysWorkingTogether())
                .isEqualTo(FIRST_EMPLOYEE_DAYS_WORKED_ON_PROJECT + THIRD_EMPLOYEE_DAYS_WORKED_ON_PROJECT);
    }

    @Test
    void shouldReturnThreePairsWhenThreeEmployeesWorkOnTheSameProject() {
        when(secondEmployee.getProjectId()).thenReturn(FIRST_PROJECT_ID);
        when(secondEmployee.getProjectId()).thenReturn(FIRST_PROJECT_ID);
        when(thirdEmployee.getProjectId()).thenReturn(FIRST_PROJECT_ID);

        List<ColleaguesView> colleaguesViews = EmployeeService.extractEmployeesWorkingOnSameProject(inputEmployees);

        assertThat(colleaguesViews).hasSize(3);

        ColleaguesView firstWorkingPair = colleaguesViews.get(0);
        ColleaguesView secondWorkingPair = colleaguesViews.get(1);
        ColleaguesView thirdWorkingPair = colleaguesViews.get(2);

        assertThat(firstWorkingPair.getFirstEmployeeId()).isEqualTo(FIRST_EMPLOYEE_ID);
        assertThat(firstWorkingPair.getSecondEmployeeId()).isEqualTo(SECOND_EMPLOYEE_ID);
        assertThat(firstWorkingPair.getProjectId()).isEqualTo(FIRST_PROJECT_ID);
        assertThat(firstWorkingPair.getDaysWorkingTogether())
                .isEqualTo(FIRST_EMPLOYEE_DAYS_WORKED_ON_PROJECT + SECOND_EMPLOYEE_DAYS_WORKED_ON_PROJECT);

        assertThat(secondWorkingPair.getFirstEmployeeId()).isEqualTo(FIRST_EMPLOYEE_ID);
        assertThat(secondWorkingPair.getSecondEmployeeId()).isEqualTo(THIRD_EMPLOYEE_ID);
        assertThat(secondWorkingPair.getProjectId()).isEqualTo(FIRST_PROJECT_ID);
        assertThat(secondWorkingPair.getDaysWorkingTogether())
                .isEqualTo(FIRST_EMPLOYEE_DAYS_WORKED_ON_PROJECT + THIRD_EMPLOYEE_DAYS_WORKED_ON_PROJECT);

        assertThat(thirdWorkingPair.getFirstEmployeeId()).isEqualTo(SECOND_EMPLOYEE_ID);
        assertThat(thirdWorkingPair.getSecondEmployeeId()).isEqualTo(THIRD_EMPLOYEE_ID);
        assertThat(thirdWorkingPair.getProjectId()).isEqualTo(FIRST_PROJECT_ID);
        assertThat(thirdWorkingPair.getDaysWorkingTogether())
                .isEqualTo(SECOND_EMPLOYEE_DAYS_WORKED_ON_PROJECT + THIRD_EMPLOYEE_DAYS_WORKED_ON_PROJECT);
    }
}
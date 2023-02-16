package com.task.assignment.service;

import com.task.assignment.TestUtils;
import com.task.assignment.domain.User;
import com.task.assignment.exception.AssignmentException;
import com.task.assignment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * The User Service Test class.
 *
 * @author mukhtiar.ahmed
 */
@Slf4j
@RunWith(SpringRunner.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Sql({"classpath:import.sql"})
@SpringBootTest
public class UserServiceTest {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonService personService;

    private User person;

    @Before
    public void setup() throws AssignmentException {
        MockitoAnnotations.initMocks(this);
        User person = TestUtils.createPerson();
        this.person = userRepository.save(person);
    }

    @Test
    public void testUpdatingAPerson() throws  Exception {
        LocalDate localDate = LocalDate.now();
        person.setDateOfBirth(localDate);
        person.setFirstName("Muhammad");
        person.setLastName("Saleem");
        User persist = personService.update(person.getId(), person);
        assertThat(persist, is(notNullValue()));
        assertThat(persist.getId(), is(notNullValue()));
        assertThat(persist.getFirstName(), is(person.getFirstName()));
        assertThat(persist.getLastName(), is(person.getLastName()));
        assertThat(persist.getDateOfBirth(), is(localDate));
        assertThat(persist.getLastModifiedOn(), is(notNullValue()));
        assertThat(persist.getCreatedOn(), is(notNullValue()));
        assertThat(persist.getLastModifiedBy(), is(notNullValue()));
        assertThat(persist.getCreatedBy(), is(notNullValue()));
    }

}

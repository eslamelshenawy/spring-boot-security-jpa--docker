package com.task.assignment.repository;

import com.task.assignment.config.TestAuditingConfiguration;
import com.task.assignment.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * The User Repository Test class.
 *
 * @author mukhtiar.ahmed
 */
@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestAuditingConfiguration.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Sql({"classpath:import.sql"})
public class UserRepositoryTest {

    @Autowired
    private AuditorAware<String> auditorAware;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final LocalDate dateOfBirth = LocalDate.of(2000, Month.JANUARY, 1);


    @Before
    public void setup() {

    }

    @Test
    public void testCurrentAuditor() {
        String currentAuditor = auditorAware.getCurrentAuditor().get();
        assertEquals("Test  Auditor", currentAuditor);
    }

    @Test
    public void testCreatingAPerson() throws Exception {

        final User person =  createPerson();
        final User persist = userRepository.save(person);
        assertThat(persist, is(notNullValue()));
        assertThat(persist.getId(), is(notNullValue()));
        assertThat(persist.getFirstName(), is(person.getFirstName()));
        assertThat(persist.getLastName(), is(person.getLastName()));
        assertThat(persist.getDateOfBirth(), is(dateOfBirth));
        assertThat(persist.getLastModifiedOn(), is(notNullValue()));
        assertThat(persist.getCreatedOn(), is(notNullValue()));
        assertThat(persist.getLastModifiedBy(), is(notNullValue()));
        assertThat(persist.getCreatedBy(), is(notNullValue()));

        assertThat(persist.getIsActive(), is(Boolean.TRUE));
        assertThat(userRepository.count(), is(1L));
        final Optional<User> optional = userRepository.findById(persist.getId());
        assertThat(optional.isPresent(), is(Boolean.TRUE));
        assertThat(optional, is(Optional.of(person)));
        final User found = optional.get();
        assertThat(found, is(notNullValue()));
        assertThat(found.getId(), is(notNullValue()));
        assertThat(found.getFirstName(), is(person.getFirstName()));
        assertThat(found.getLastName(), is(person.getLastName()));
        assertThat(found.getIsActive(), is(Boolean.TRUE));
        assertThat(found.getDateOfBirth(), is(dateOfBirth));
        assertThat(found.getCreatedOn(), is(persist.getCreatedOn()));
        assertThat(found.getLastModifiedOn(), is(persist.getLastModifiedOn()));
        assertThat(found.getCreatedBy(),  is(persist.getCreatedBy()));
        assertThat(found.getLastModifiedBy(),  is(persist.getLastModifiedBy()));

    }

    @Test
    public void testUpdatingAPerson() throws Exception {
        final User person =  createPerson();
        final User persist  = entityManager.persist(person);
        final User old = entityManager.find(User.class, persist.getId());

        final String firstName = "Muhammad";
        final String lastName = "Ali";
        final LocalDate dateOfBirth = LocalDate.of(2011, Month.DECEMBER, 12);
        old.setFirstName(firstName);
        old.setLastName(lastName);
        old.setIsActive(Boolean.FALSE);
        old.setDateOfBirth(dateOfBirth);


        final User updatePerson = userRepository.save(old);
        assertThat(updatePerson, is(notNullValue()));
        assertThat(updatePerson.getId(), is(old.getId()));
        assertThat(updatePerson.getFirstName(), is(firstName));
        assertThat(updatePerson.getLastName(), is(lastName));
        assertThat(updatePerson.getIsActive(), is(Boolean.FALSE));
        assertThat(updatePerson.getDateOfBirth(), is(dateOfBirth));
        assertThat(updatePerson.getCreatedBy(),  is(old.getCreatedBy()));
        assertThat(updatePerson.getLastModifiedBy(),  is(old.getLastModifiedBy()));
        assertThat(updatePerson.getCreatedOn(), is(old.getCreatedOn()));

    }

    @Test
    public void testFindAll() throws Exception {
        final User person1 = createPerson("Mukhtiar", "Ahmed");
        final User person2 = createPerson("Syed", "Ali");
        final User person3 = createPerson("Muhammad", "Saleem");
        entityManager.persist(person1);
        entityManager.persist(person2);
        entityManager.persist(person3);
        List<User> list =  userRepository.findAll();
        assertThat( list, hasItem(hasProperty("firstName", is("Syed"))));
        assertThat( list, hasItem(hasProperty("firstName", is("Mukhtiar"))));
        assertThat( list, hasItem(hasProperty("firstName", is("Muhammad"))));
        assertThat(list.size(), is(3));

    }

    @Test
    public void testFindById() throws Exception {
        final User person =  createPerson();
        final User persist  = entityManager.persist(person);
        final Optional<User> optional = userRepository.findById(persist.getId());
        assertThat(optional.isPresent(), is(Boolean.TRUE));
        assertThat(optional, is(Optional.of(person)));
        final User found = optional.get();
        assertThat(found, is(notNullValue()));
        assertThat(found.getId(), is(notNullValue()));
        assertThat(found.getFirstName(), is(persist.getFirstName()));
        assertThat(found.getLastName(), is(persist.getLastName()));
        assertThat(found.getIsActive(), is(Boolean.TRUE));
        assertThat(found.getDateOfBirth(), is(dateOfBirth));
        assertThat(found.getCreatedOn(), is(persist.getCreatedOn()));
        assertThat(found.getLastModifiedOn(), is(persist.getLastModifiedOn()));
        assertThat(found.getCreatedBy(),  is(persist.getCreatedBy()));
        assertThat(found.getLastModifiedBy(),  is(persist.getLastModifiedBy()));
    }

    private User createPerson() {
        final String firstName = "Ahmed";
        final String lastName = "Mukhtiar";
        return createPerson(firstName, lastName);
    }

    private User createPerson(String firstName, String lastName) {

        final User person = new User();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setDateOfBirth(dateOfBirth);
        return person;
    }


}

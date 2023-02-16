package com.task.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.assignment.domain.User;
import com.task.assignment.dto.PersonDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
    public static final String FIRST_NAME = "Ahmed";
    public static  final String LAST_NAME = "Mukhtiar";

    public static String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static User createPerson() {
               return createPerson(FIRST_NAME, LAST_NAME);
    }

    private static User createPerson(String firstName, String lastName) {

        final User person = new User();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setDateOfBirth( LocalDate.of(2000, Month.JANUARY, 1));
        return person;
    }

    public  static PersonDTO createPersonDTO() {
        return createPersonDTO(FIRST_NAME, LAST_NAME);
    }

    private static PersonDTO createPersonDTO(String firstName, String lastName) {

        final PersonDTO person = new PersonDTO();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAge(45);
        return person;
    }

}

package com.task.assignment.controller;


import com.task.assignment.AssignmentApplication;
import com.task.assignment.TestUtils;
import com.task.assignment.domain.User;
import com.task.assignment.dto.PersonDTO;
import com.task.assignment.exception.AssignmentException;
import com.task.assignment.mapper.AssignmentMapper;
import com.task.assignment.repository.UserRepository;
import com.task.assignment.util.AssignmentHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The User Controller Test class.
 *
 * @author mukhtiar.ahmed
 */
@Slf4j
@RunWith(SpringRunner.class)
@Sql({"classpath:import.sql"})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootTest(classes = { AssignmentApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;


    private User person;

    private MockMvc mockMvc;

    @Before
    public void setup() throws AssignmentException {
        MockitoAnnotations.initMocks(this);
        User person = TestUtils.createPerson();
        this.person = userRepository.save(person);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/1.0/person";
    }


    @WithMockUser
    @Test
    public void paginationTest() throws Exception {

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "0");
        requestParams.add("pageSize", "10");
        ResultActions result  = mockMvc.perform(get(getRootUrl() +"/pagination").params(requestParams));
        result.andExpect(status().isOk()).andExpect(jsonPath("$.status").value(AssignmentHelper.SUCCESS)).andDo(print());

    }


    @Test
    @WithMockUser(username="ahmed",roles={"ADMIN"})
    public void paginationAdminTest() throws Exception {

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "0");
        requestParams.add("pageSize", "10");
        ResultActions result  = mockMvc.perform(get(getRootUrl() + "/admin/pagination").params(requestParams));
        result.andExpect(status().isOk()).andExpect(jsonPath("$.status").value(AssignmentHelper.SUCCESS)).andDo(print());

    }


    @Test
    @WithMockUser(username="ahmed",roles={"ADMIN"})
    public void createPersonTest() throws Exception {

        PersonDTO dto = TestUtils.createPersonDTO();
        String personJson = TestUtils.convertObjectToJson(dto);
        ResultActions result  = mockMvc.perform(post(getRootUrl()).with(csrf()).with(user("ahmed").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personJson));
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(AssignmentHelper.SUCCESS))
                .andExpect(jsonPath("$.data.firstName").value(dto.getFirstName())).andDo(print());

    }

    @Test
    @WithMockUser(username="ahmed",roles={"ADMIN"})
    public void updatePersonTest() throws Exception {

        PersonDTO personDTO = AssignmentMapper.toPersonDTO(person);
        personDTO.setDateOfBirth(null);
        personDTO.setAge(25);
        personDTO.setFirstName("Muhammad");
        personDTO.setLastName("Saleem");

        String personJson = TestUtils.convertObjectToJson(personDTO);
        ResultActions result  = mockMvc.perform(put(getRootUrl() + "/" + person.getId())
                .with(csrf()).with(user("ahmed").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AssignmentHelper.SUCCESS))
                .andExpect(jsonPath("$.data.firstName").value(personDTO.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(personDTO.getLastName()))
                .andExpect(jsonPath("$.data.age").value(personDTO.getAge()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getPersonByIdTest() throws Exception {


        PersonDTO personDTO = AssignmentMapper.toPersonDTO(person);
        ResultActions result  = mockMvc.perform(get(getRootUrl() + "/" + personDTO.getId()));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AssignmentHelper.SUCCESS))
                .andExpect(jsonPath("$.data.firstName").value(personDTO.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(personDTO.getLastName()))
                .andExpect(jsonPath("$.data.age").value(personDTO.getAge()))
                .andDo(print());

    }



}

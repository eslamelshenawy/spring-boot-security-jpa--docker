package com.task.assignment;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;


/**
 * The Assignment Application.
 *
 * @author mukhtiar.ahmed
 * @version 1.0
 */
@SpringBootApplication
public class AssignmentApplication {

    private static final String LOOKUP_DATA = "classpath:lookup-data.sql";


    @Autowired
    private DataSource datasource;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }





}

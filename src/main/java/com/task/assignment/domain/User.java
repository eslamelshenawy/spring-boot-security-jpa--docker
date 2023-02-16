package com.task.assignment.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * The persistent class for the User database table.
 *
 * @author mukhtiar.ahmed
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity(name = "person")
public class User extends BaseEntity {

    private static final long serialVersionUID = 9052908123617887381L;


    @NotNull
    @Column(name = "first_name", length = 25, nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", length = 25, nullable = false)
    private String lastName;


    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;


  }

package com.task.assignment.mapper;


import com.task.assignment.domain.User;
import com.task.assignment.dto.PersonDTO;
import com.task.assignment.dto.SimpleUserDTO;
import com.task.assignment.util.AssignmentHelper;

/**
 * The Mapper class
 *
 * @author mukhtiar.ahmed
 * @version 1.0
 */
public class AssignmentMapper {

    private AssignmentMapper() {

    }

    public static SimpleUserDTO toSimplePersonDTO(User entity) {
        if (entity == null) return null;

        SimpleUserDTO dto = new SimpleUserDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(AssignmentHelper.calculateAge(entity.getDateOfBirth()));
        return dto;

    }

    public static PersonDTO toPersonDTO(User entity) {
        if (entity == null) return null;
        PersonDTO dto = new PersonDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(AssignmentHelper.calculateAge(entity.getDateOfBirth()));
        dto.setDateOfBirth(entity.getDateOfBirth());
        return dto;

    }

}

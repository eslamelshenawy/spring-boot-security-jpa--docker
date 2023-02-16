package com.task.assignment.service;


import java.util.Arrays;
import java.util.List;

import com.task.assignment.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.assignment.annotations.LogMethod;
import com.task.assignment.dto.ListResponseDTO;
import com.task.assignment.dto.SearchCriteria;
import com.task.assignment.exception.AssignmentException;
import com.task.assignment.repository.UserRepository;
import com.task.assignment.util.AssignmentHelper;

/**
 * The User service
 *
 * @author mukhtiar.ahmed
 * @version 1.0
 */

@Service
@Transactional
public class PersonServiceImpl extends BaseListableService<User, String> implements PersonService {

    private final List<String> columnCompMaster = Arrays.asList(
            "firstName", "lastName", "age");

    @Value("${page.size:10}")
    private int pageSize;


    @Override
    @LogMethod
    public User update(String id, User entity) throws AssignmentException {
        AssignmentHelper.checkNull(id, "id");
        AssignmentHelper.checkNull(entity, "entity");
        User oldEntity = get(id);
        oldEntity.setLastName(entity.getLastName());
        oldEntity.setFirstName(entity.getFirstName());
        oldEntity.setDateOfBirth(entity.getDateOfBirth());
        return repository.save(oldEntity);
    }


    @Override
    @LogMethod
    public ListResponseDTO<User> list(SearchCriteria searchCriteria) {

        Pageable pageable;
        if (columnCompMaster.contains(searchCriteria.getSortColumn())) {
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.fromString(searchCriteria.getSortOrder()),
                    searchCriteria.getSortColumn()));
            pageable = PageRequest.of(searchCriteria.getPage(), searchCriteria.getPageSize() > 0 ?
                    searchCriteria.getPageSize() : pageSize, sort);
        } else {
            pageable = PageRequest.of(searchCriteria.getPage(), searchCriteria.getPageSize());
        }

        Page<User> page;
        if (!StringUtils.isEmpty(searchCriteria.getSearchString())) {
            String searchString = searchCriteria.getSearchString().toLowerCase();
            if (searchCriteria.isActiveOnly()) {
                page = ((UserRepository) repository).searchActivePersons(searchString, pageable);
            } else {
                page = ((UserRepository) repository).searchPersons(searchString, pageable);
            }
        } else  if (searchCriteria.isActiveOnly())  {
            page = ((UserRepository) repository).findByIsActiveTrue(pageable);
        } else {
            page = repository.findAll(pageable);
        }

        ListResponseDTO<User> responseDTO = new ListResponseDTO<>();

        responseDTO.setData(page.getContent());
        responseDTO.setTotalElement(page.getTotalElements());
        return responseDTO;

    }
}

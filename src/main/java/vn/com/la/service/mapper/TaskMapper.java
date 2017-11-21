package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.TaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, })
public interface TaskMapper extends EntityMapper <TaskDTO, Task> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    TaskDTO toDto(Task task); 

    @Mapping(source = "projectId", target = "project")
    Task toEntity(TaskDTO taskDTO); 
    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}

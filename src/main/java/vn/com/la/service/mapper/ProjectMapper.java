package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface ProjectMapper extends EntityMapper <ProjectDTO, Project> {

    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "manager.login", target = "managerLogin")
    ProjectDTO toDto(Project project); 

    @Mapping(source = "managerId", target = "manager")
    Project toEntity(ProjectDTO projectDTO); 
    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}

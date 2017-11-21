package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.JobDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Job and its DTO JobDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, })
public interface JobMapper extends EntityMapper <JobDTO, Job> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    JobDTO toDto(Job job); 

    @Mapping(source = "projectId", target = "project")
    Job toEntity(JobDTO jobDTO); 
    default Job fromId(Long id) {
        if (id == null) {
            return null;
        }
        Job job = new Job();
        job.setId(id);
        return job;
    }
}

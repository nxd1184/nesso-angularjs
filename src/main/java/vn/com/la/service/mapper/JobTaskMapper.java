package vn.com.la.service.mapper;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity JobTask and its DTO JobTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {JobMapper.class, TaskMapper.class})
public interface JobTaskMapper {
}

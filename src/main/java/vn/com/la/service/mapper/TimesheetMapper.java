package vn.com.la.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.la.domain.Timesheet;
import vn.com.la.service.dto.TimesheetDTO;


@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TimesheetMapper extends EntityMapper<TimesheetDTO, Timesheet>{

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.lastName", target = "name")
    TimesheetDTO toDto(Timesheet timesheet);

    @Mapping(source = "userId", target = "user")
    Timesheet toEntity(TimesheetDTO timesheetDTO);
    default Timesheet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Timesheet timesheet = new Timesheet();
        timesheet.setId(id);
        return timesheet;
    }

}

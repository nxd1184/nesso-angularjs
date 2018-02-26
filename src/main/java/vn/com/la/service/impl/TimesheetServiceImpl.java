package vn.com.la.service.impl;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.domain.Team;
import vn.com.la.domain.Timesheet;
import vn.com.la.repository.TimesheetRepository;
import vn.com.la.service.TimesheetService;
import vn.com.la.service.dto.TeamDTO;
import vn.com.la.service.dto.TimesheetDTO;
import vn.com.la.service.dto.param.SubmitTimesheetParam;
import vn.com.la.service.mapper.TimesheetMapper;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetServiceImpl implements TimesheetService{

    private TimesheetRepository timesheetRepository;
    private TimesheetMapper mapper;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository, TimesheetMapper mapper) {
        this.timesheetRepository = timesheetRepository;
        this.mapper = mapper;
    }

    @Override
    public TimesheetDTO findByDateAndUserId(Date date, Long userId) {
        Timesheet timesheet = timesheetRepository.findByDateAndUserId(date, userId);
        return mapper.toDto(timesheet);
    }


    @Override
    public TimesheetDTO save(TimesheetDTO timesheetDTO) {

        Timesheet timesheet = mapper.toEntity(timesheetDTO);

        timesheet = timesheetRepository.save(timesheet);

        return mapper.toDto(timesheet);
    }

    @Override
    public EmptyResponseVM submitTimesheet(SubmitTimesheetParam param) {
        EmptyResponseVM rs = new EmptyResponseVM();

        TimesheetDTO timesheetDTO = findByDateAndUserId(param.getDate().toDate(), param.getUserId());

        if(timesheetDTO == null) {
            timesheetDTO = new TimesheetDTO();
            timesheetDTO.setCheckInTime(param.getTime().toDate());
            timesheetDTO.setDate(param.getDate().toDate());
            timesheetDTO.setUserId(param.getUserId());
        }else {
            timesheetDTO.setCheckOutTime(param.getTime().toDate());
        }

        save(timesheetDTO);

        return  rs;
    }

    @Override
    public List<TimesheetDTO> getTimesheetReport(Date fromDate, Date toDate) {
        return timesheetRepository.findByDateBetweenAndCheckOutTimeIsNotNull(fromDate, toDate).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}

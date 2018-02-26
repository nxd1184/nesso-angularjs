package vn.com.la.service;

import org.joda.time.DateTime;
import vn.com.la.service.dto.TimesheetDTO;
import vn.com.la.service.dto.param.SubmitTimesheetParam;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.Date;
import java.util.List;

public interface TimesheetService {
    TimesheetDTO findByDateAndUserId(Date date, Long userId);

    TimesheetDTO save(TimesheetDTO timesheetDTO);

    EmptyResponseVM submitTimesheet(SubmitTimesheetParam param);

    List<TimesheetDTO> getTimesheetReport(Date fromDate, Date toDate);
}

package vn.com.la.repository;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.la.domain.Timesheet;
import vn.com.la.domain.User;

import java.util.Date;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long>{

    Timesheet findByDateAndUserId(Date date, Long userId);
}

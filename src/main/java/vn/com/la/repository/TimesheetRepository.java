package vn.com.la.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.la.domain.Timesheet;

import java.util.Date;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long>{

    Timesheet findByDateAndUserId(Date date, Long userId);

    List<Timesheet> findByDateBetweenAndCheckOutTimeIsNotNull(Date fromDate, Date toDate);
}

package vn.com.la.service.impl;

import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.config.Constants;
import vn.com.la.domain.JobTeam;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.User;
import vn.com.la.domain.enumeration.FileStatus;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.*;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.repository.JobTeamUserTaskRepository;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.JobTeamUserDTO;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;
import vn.com.la.service.dto.param.DeliveryFilesParamDTO;
import vn.com.la.service.dto.param.SearchJobTeamUserTaskParamDTO;
import vn.com.la.service.dto.param.UpdateJobTeamUserTaskStatusParamDTO;
import vn.com.la.service.mapper.JobTeamUserTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.specification.JobTeamUserTaskSpecifications;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.CheckInAllResponseVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;
import vn.com.la.web.rest.vm.response.DeliveryFilesResponseVM;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing JobTeamUserTask.
 */
@Service
@Transactional
public class JobTeamUserTaskServiceImpl implements JobTeamUserTaskService{

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskServiceImpl.class);

    private final JobTeamUserTaskRepository jobTeamUserTaskRepository;

    private final JobTeamUserTaskMapper jobTeamUserTaskMapper;

    private final JobService jobService;

    private final FileSystemHandlingService fileSystemHandlingService;

    private final UserService userService;
    private final CacheManager cacheManager;
    private final JobTeamService jobTeamService;
    private final JobTeamUserService jobTeamUserService;
    private final JobTeamUserTaskTrackingService jobTeamUserTaskTrackingService;

    public JobTeamUserTaskServiceImpl(JobTeamUserTaskRepository jobTeamUserTaskRepository, JobTeamUserTaskMapper jobTeamUserTaskMapper,
                                      JobService jobService, FileSystemHandlingService fileSystemHandlingService,
                                      UserService userService, JobTeamUserService jobTeamUserService,
                                      JobTeamUserTaskTrackingService jobTeamUserTaskTrackingService,
                                      JobTeamService jobTeamService, CacheManager cacheManager) {
        this.jobTeamUserTaskRepository = jobTeamUserTaskRepository;
        this.jobTeamUserTaskMapper = jobTeamUserTaskMapper;
        this.jobService = jobService;
        this.fileSystemHandlingService = fileSystemHandlingService;
        this.userService = userService;
        this.jobTeamUserService  = jobTeamUserService;
        this.jobTeamUserTaskTrackingService = jobTeamUserTaskTrackingService;
        this.jobTeamService = jobTeamService;
        this.cacheManager = cacheManager;
    }

    /**
     * Save a jobTeamUserTask.
     *
     * @param jobTeamUserTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobTeamUserTaskDTO save(JobTeamUserTaskDTO jobTeamUserTaskDTO) {
        log.debug("Request to save JobTeamUserTask : {}", jobTeamUserTaskDTO);
        JobTeamUserTask jobTeamUserTask = jobTeamUserTaskMapper.toEntity(jobTeamUserTaskDTO);
        jobTeamUserTask = jobTeamUserTaskRepository.save(jobTeamUserTask);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTask);
    }

    @Override
    public List<JobTeamUserTaskDTO> save(List<JobTeamUserTaskDTO> jobTeamUserTaskDTOList) {
        log.debug("Request to save List JobTeamUserTask : {}", jobTeamUserTaskDTOList);
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskMapper.toEntity(jobTeamUserTaskDTOList);
        jobTeamUserTaskList = jobTeamUserTaskRepository.save(jobTeamUserTaskList);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTaskList);
    }

    /**
     *  Get all the jobTeamUserTasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobTeamUserTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTeamUserTasks");
        return jobTeamUserTaskRepository.findAll(pageable)
            .map(jobTeamUserTaskMapper::toDto);
    }

    /**
     *  Get one jobTeamUserTask by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobTeamUserTaskDTO findOne(Long id) {
        log.debug("Request to get JobTeamUserTask : {}", id);
        JobTeamUserTask jobTeamUserTask = jobTeamUserTaskRepository.findOne(id);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTask);
    }

    @Override
    @Transactional(readOnly = true)
    public JobTeamUserTask findEntityById(Long id) {
        return jobTeamUserTaskRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public JobTeamUserTaskDTO findByFileName(String fileName) {
        log.debug("Request to get JobTeamUserTask : {}", fileName);
        JobTeamUserTask jobTeamUserTask = jobTeamUserTaskRepository.findByFileName(fileName);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTask);
    }

    /**
     *  Delete the  jobTeamUserTask by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTeamUserTask : {}", id);
        jobTeamUserTaskRepository.delete(id);
    }

    @Override
    public Page<JobTeamUserTaskDTO> search(Pageable pageable, SearchJobTeamUserTaskParamDTO params) {

        Specification<JobTeamUserTask> searchSpec = JobTeamUserTaskSpecifications.search(params);
        Page<JobTeamUserTaskDTO> page = jobTeamUserTaskRepository.findAll(searchSpec,pageable).map(jobTeamUserTaskMapper::toDto);

        return page;
    }

    @Override
    public CheckInAllResponseVM checkAll(String assignee) throws Exception {

        SearchJobTeamUserTaskParamDTO criteria = new SearchJobTeamUserTaskParamDTO();
        criteria.setAssignee(assignee);
        criteria.setStatusList(Constants.TO_DO_STATUS_LIST);

        List<JobTeamUserTaskDTO> jobTeamUserTasks = jobTeamUserTaskRepository.findAll(JobTeamUserTaskSpecifications.search(criteria)).stream().map(jobTeamUserTaskMapper::toDto).collect(Collectors.toList());

        List<Long> succeedCheckInTaskIds = new ArrayList<>();

        for(JobTeamUserTaskDTO task: jobTeamUserTasks) {
            try {
                checkIn(task.getId());
                succeedCheckInTaskIds.add(task.getId());
            } catch (Exception ex) {
                // ignore exception
            }

        }

        CheckInAllResponseVM rs = new CheckInAllResponseVM();
        rs.setIds(succeedCheckInTaskIds);

        return rs;
    }

    @Override
    public EmptyResponseVM checkIn(Long id) throws Exception{
        JobTeamUserTaskDTO jobTeamUserTaskDTO = findOne(id);

        if(jobTeamUserTaskDTO.getStatus() != FileStatusEnum.TODO && jobTeamUserTaskDTO.getStatus() != FileStatusEnum.REWORK) {
            throw new CustomParameterizedException("File status should be To Do or Rework");
        }

        String path = LAStringUtil.buildFolderPath(Constants.SLASH + jobTeamUserTaskDTO.getProjectCode(),
                                                            Constants.TO_CHECK,
                                                            jobTeamUserTaskDTO.getJobName(), jobTeamUserTaskDTO.getJobTeamUserLogin()) + jobTeamUserTaskDTO.getFileName();
        boolean fileExistOnToCheck = fileSystemHandlingService.checkFileExist(path);

        User loginedUser = userService.getUserWithAuthorities();

        JobTeamUserDTO jobTeamUserDTO = jobTeamUserService.findOne(jobTeamUserTaskDTO.getJobTeamUserId());

        if(jobTeamUserDTO == null || jobTeamUserDTO.getUserId() == null || jobTeamUserDTO.getUserId() != loginedUser.getId()) {
            throw new CustomParameterizedException("You can not check in this file because you are not assigned to this file");
        }


        if(fileExistOnToCheck) {
            jobTeamUserTaskDTO.setLastCheckInTime(ZonedDateTime.now());
            jobTeamUserTaskDTO.setStatus(FileStatusEnum.TOCHECK);
            jobTeamUserTaskDTO = save(jobTeamUserTaskDTO);
            jobService.updateJobToStart(jobTeamUserTaskDTO.getJobId());

            JobTeamUserTaskTrackingDTO trackingDTO = new JobTeamUserTaskTrackingDTO();
            trackingDTO.setStatus(FileStatus.TOCHECK);
            trackingDTO.setJobTeamUserTaskId(jobTeamUserTaskDTO.getId());
            trackingDTO.setUserId(loginedUser.getId());
            trackingDTO.setTrackingTime(ZonedDateTime.now());
            trackingDTO = jobTeamUserTaskTrackingService.save(trackingDTO);

            return new EmptyResponseVM();
        }

        throw new CustomParameterizedException("File " + jobTeamUserTaskDTO.getFileName() + " not found in to-check folder");
    }

    @Override
    public EmptyResponseVM rework(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception {
        JobTeamUserTaskDTO taskDTO = findByFileName(params.getFileName());

        if(taskDTO == null) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found");
        }

        if(taskDTO.getStatus() != FileStatusEnum.TOCHECK) {
            throw new CustomParameterizedException("File status is not To Check");
        }

        String filePath = LAStringUtil.buildFolderPath(Constants.SLASH + taskDTO.getProjectCode(),
            Constants.TO_CHECK,
            taskDTO.getJobName(), taskDTO.getJobTeamUserLogin()) + taskDTO.getFileName();
        boolean fileExistOnTodoFolder = fileSystemHandlingService.checkFileExist(filePath);
        if(!fileExistOnTodoFolder) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found in to do folder");
        }

        User loginedUser = userService.getUserWithAuthorities();

        taskDTO.setStatus(FileStatusEnum.REWORK);
        taskDTO.setQcId(loginedUser.getId());
        Integer numberOfRework = taskDTO.getNumberOfRework();
        if(numberOfRework == null) {
            numberOfRework = 1;
        }else {
            numberOfRework++;
        }
        taskDTO.setNumberOfRework(numberOfRework);
        taskDTO.setLastReworkTime(ZonedDateTime.now());
        taskDTO = save(taskDTO);

        JobTeamUserTaskTrackingDTO trackingDTO = new JobTeamUserTaskTrackingDTO();
        trackingDTO.setStatus(FileStatus.REWORK);
        trackingDTO.setJobTeamUserTaskId(taskDTO.getId());
        trackingDTO.setUserId(loginedUser.getId());
        trackingDTO.setTrackingTime(ZonedDateTime.now());
        trackingDTO = jobTeamUserTaskTrackingService.save(trackingDTO);

        return new EmptyResponseVM();
    }

    @Override
    public EmptyResponseVM qcEdit(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception {
        JobTeamUserTaskDTO taskDTO = findByFileName(params.getFileName());

        if(taskDTO == null) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found");
        }

        if(taskDTO.getStatus() != FileStatusEnum.TOCHECK) {
            throw new CustomParameterizedException("File status is not To Check");
        }

        // check file exist on to check folder
        String filePath = LAStringUtil.buildFolderPath(Constants.SLASH + taskDTO.getProjectCode(),
            Constants.TO_CHECK,
            taskDTO.getJobName(), taskDTO.getJobTeamUserLogin()) + taskDTO.getFileName();
        boolean fileExistOnTodoFolder = fileSystemHandlingService.checkFileExist(filePath);
        if(!fileExistOnTodoFolder) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found in to check folder");
        }

        // check file exist on done folder, uploaded by qc
        filePath = LAStringUtil.buildFolderPath(Constants.SLASH + taskDTO.getProjectCode(),
            Constants.DONE,
            taskDTO.getJobName(), taskDTO.getJobTeamUserLogin()) + taskDTO.getFileName();
        boolean fileExistOnDoneFolder = fileSystemHandlingService.checkFileExist(filePath);
        if(!fileExistOnDoneFolder) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found in Done folder. Please upload this file into Done folder before change to QC-Edit.");
        }

        User loginedUser = userService.getUserWithAuthorities();

        taskDTO.setStatus(FileStatusEnum.DONE);
        taskDTO.setQcId(loginedUser.getId());
        taskDTO.setQcEdit(true);
        taskDTO.setLastDoneTime(ZonedDateTime.now());
        taskDTO = save(taskDTO);

        JobTeamUserTaskTrackingDTO trackingDTO = new JobTeamUserTaskTrackingDTO();
        trackingDTO.setStatus(FileStatus.DONE);
        trackingDTO.setJobTeamUserTaskId(taskDTO.getId());
        trackingDTO.setUserId(loginedUser.getId());
        trackingDTO.setTrackingTime(ZonedDateTime.now());
        trackingDTO = jobTeamUserTaskTrackingService.save(trackingDTO);

        return new EmptyResponseVM();
    }

    @Override
    public EmptyResponseVM done(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception {
        JobTeamUserTaskDTO taskDTO = findByFileName(params.getFileName());

        if(taskDTO == null) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found");
        }

        if(taskDTO.getStatus() != FileStatusEnum.TOCHECK) {
            throw new CustomParameterizedException("File status is not To Check");
        }

        // check file exist on to check folder
        String toCheckFilePath = LAStringUtil.buildFolderPath(Constants.SLASH + taskDTO.getProjectCode(),
            Constants.TO_CHECK,
            taskDTO.getJobName(), taskDTO.getJobTeamUserLogin()) + taskDTO.getFileName();
        boolean fileExistOnToCheckFolder = fileSystemHandlingService.checkFileExist(toCheckFilePath);
        if(!fileExistOnToCheckFolder) {
            throw new CustomParameterizedException("File " + params.getFileName() + " not found in to do folder");
        }


        String doneFolderPath = LAStringUtil.buildFolderPath(Constants.SLASH + taskDTO.getProjectCode(),
            Constants.DONE,
            taskDTO.getJobName(), taskDTO.getJobTeamUserLogin());


        boolean moveResult = fileSystemHandlingService.move(toCheckFilePath, doneFolderPath);
        if(!moveResult) {
            throw new CustomParameterizedException("Can not move file from to check to done");
        }

        User loginedUser = userService.getUserWithAuthorities();

        taskDTO.setStatus(FileStatusEnum.DONE);
        taskDTO.setQcId(loginedUser.getId());
        taskDTO.setLastDoneTime(ZonedDateTime.now());
        taskDTO = save(taskDTO);

        JobTeamUserTaskTrackingDTO trackingDTO = new JobTeamUserTaskTrackingDTO();
        trackingDTO.setStatus(FileStatus.DONE);
        trackingDTO.setJobTeamUserTaskId(taskDTO.getId());
        trackingDTO.setUserId(loginedUser.getId());
        trackingDTO.setTrackingTime(ZonedDateTime.now());
        trackingDTO = jobTeamUserTaskTrackingService.save(trackingDTO);

        return new EmptyResponseVM();
    }

    @Override
    public DeliveryFilesResponseVM delivery(DeliveryFilesParamDTO params) throws Exception {

        List<JobTeamUserTaskDTO> tasks = jobTeamUserTaskMapper.toDto(jobTeamUserTaskRepository.findByFileNameInAndStatus(params.getFileNames(), FileStatusEnum.DONE));

        ZonedDateTime now = ZonedDateTime.now();

        User loginedUser = userService.getUserWithAuthorities();

        DeliveryFilesResponseVM rs = new DeliveryFilesResponseVM();

        rs.setFailedList(params.getFileNames());

        for (JobTeamUserTaskDTO task : tasks) {
            task.setLastDeliveryTime(now);
            JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = new JobTeamUserTaskTrackingDTO();
            jobTeamUserTaskTrackingDTO.setJobTeamUserTaskId(task.getId());
            jobTeamUserTaskTrackingDTO.setStatus(FileStatus.DELIVERY);
            jobTeamUserTaskTrackingDTO.setTrackingTime(now);
            jobTeamUserTaskTrackingDTO.setUserId(loginedUser.getId());

            jobTeamUserTaskTrackingService.save(jobTeamUserTaskTrackingDTO);

            if (fileSystemHandlingService.deliverFileToDelivery(task, loginedUser.getLogin())) {
                rs.getSuccessList().add(task.getFileName());
            }
        }

        rs.getFailedList().removeAll(rs.getSuccessList());

        save(tasks);

        return rs;
    }

    @Override
    public Long countByStatusAndDateRange(List<FileStatusEnum> statusList, Instant fromDate, Instant toDate) {
        return jobTeamUserTaskRepository.countByStatusInAndLastModifiedDateIsBetween(statusList, fromDate,toDate);
    }

    @Override
    public Long sumNumberOfReworkByLastReworkTimeIsBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
        return jobTeamUserTaskRepository.sumNumberOfReworkByLastReworkTimeIsBetween(fromDate, toDate);
    }

    @Override
    public List<JobTeamUserTask> findByJobTeamUserIdAndJobId(Long id, Long jobId) {
        return jobTeamUserTaskRepository.findByJobTeamUserIdAndJobTeamUserJobTeamJobId(id, jobId);
    }

    @Override
    public List<JobTeamUserTaskDTO> findJobToDoList(Long id, Long jobId) {
        return jobTeamUserTaskRepository.findByJobTeamUserIdAndStatusInAndJobTeamUserJobTeamJobId(id, Constants.TO_DO_STATUS_LIST, jobId).stream().map(jobTeamUserTaskMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Long countJobToDoList(Long id, Long jobId) {
        return jobTeamUserTaskRepository.countByJobTeamUserIdAndStatusInAndJobTeamUserJobTeamJobId(id, Constants.TO_DO_STATUS_LIST, jobId);
    }

    @Override
    public Long countNotDoneTask(Long jobId) {
        return jobTeamUserTaskRepository.countByJobTeamUserJobTeamJobIdAndStatusIn(jobId, Constants.NOT_DONE_STATUS_LIST);
    }

    @Override
    public int updateAdjustment(Long jobTeamUserId, String newFilePath, Long id) {
        return jobTeamUserTaskRepository.updateAdjustment(jobTeamUserId, newFilePath, id);
    }

    @Override
    public boolean deleteUnexpectedFile(String fileName) throws Exception{
        JobTeamUserTaskDTO jobTeamUserTaskDTO =  this.findByFileName(fileName);
        if(jobTeamUserTaskDTO == null) {
            return false;
        }

        if(jobTeamUserTaskDTO.getStatus() != FileStatusEnum.TODO) {
            return false;
        }else {
            JobTeamUserDTO jobTeamUserDTO = jobTeamUserService.findOne(jobTeamUserTaskDTO.getJobTeamUserId());
            jobTeamUserDTO.setTotalFiles(jobTeamUserDTO.getTotalFiles() - 1);
            jobTeamUserService.save(jobTeamUserDTO);


            JobTeamDTO jobTeamDTO = jobTeamService.findOne(jobTeamUserDTO.getJobTeamId());
            jobTeamDTO.setTotalFiles(jobTeamDTO.getTotalFiles() - 1);
            jobTeamService.save(jobTeamDTO);

            JobDTO jobDTO = jobService.findOne(jobTeamDTO.getJobId());
            jobDTO.setTotalFiles(jobDTO.getTotalFiles() - 1);
            jobService.save(jobDTO);


            jobTeamUserTaskRepository.deleteById(jobTeamUserTaskDTO.getId());

            // delete to do file
            String toDoFilePath = Constants.SLASH + jobTeamUserTaskDTO.getFilePath().concat(Constants.SLASH).concat(jobTeamUserTaskDTO.getFileName());
            if(fileSystemHandlingService.checkFileExist(toDoFilePath)) {
                fileSystemHandlingService.deleteFile(toDoFilePath);
            }

            // delete backlog file
            String backlogFilePath = Constants.SLASH + jobTeamUserTaskDTO.getOriginalFilePath().concat(Constants.SLASH).concat(jobTeamUserTaskDTO.getOriginalFileName());
            if(fileSystemHandlingService.checkFileExist(backlogFilePath)) {
                fileSystemHandlingService.deleteFile(backlogFilePath);
            }

//            cacheManager.getCache(JobTeamUserTask.class.getName()).clear();
//            cacheManager.getCache(JobTeamUser.class.getName()).clear();
//            cacheManager.getCache(JobTeam.class.getName()).clear();

        }

        return true;
    }
}

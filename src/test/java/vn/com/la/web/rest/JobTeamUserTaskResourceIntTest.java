package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.User;
import vn.com.la.repository.JobTeamUserTaskRepository;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.mapper.JobTeamUserTaskMapper;
import vn.com.la.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static vn.com.la.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import vn.com.la.domain.enumeration.FileStatusEnum;
/**
 * Test class for the JobTeamUserTaskResource REST controller.
 *
 * @see JobTeamUserTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class JobTeamUserTaskResourceIntTest {

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_PATH = "BBBBBBBBBB";

    private static final FileStatusEnum DEFAULT_STATUS = FileStatusEnum.TODO;
    private static final FileStatusEnum UPDATED_STATUS = FileStatusEnum.TOCHECK;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_REWORK = 1;
    private static final Integer UPDATED_NUMBER_OF_REWORK = 2;

    private static final ZonedDateTime DEFAULT_LAST_CHECK_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_CHECK_IN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_QC_EDIT = false;
    private static final Boolean UPDATED_QC_EDIT = true;

    private static final Boolean DEFAULT_REWORK = false;
    private static final Boolean UPDATED_REWORK = true;

    @Autowired
    private JobTeamUserTaskRepository jobTeamUserTaskRepository;

    @Autowired
    private JobTeamUserTaskMapper jobTeamUserTaskMapper;

    @Autowired
    private JobTeamUserTaskService jobTeamUserTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobTeamUserTaskMockMvc;

    private JobTeamUserTask jobTeamUserTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobTeamUserTaskResource jobTeamUserTaskResource = new JobTeamUserTaskResource(jobTeamUserTaskService);
        this.restJobTeamUserTaskMockMvc = MockMvcBuilders.standaloneSetup(jobTeamUserTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTeamUserTask createEntity(EntityManager em) {
        JobTeamUserTask jobTeamUserTask = new JobTeamUserTask()
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .originalFilePath(DEFAULT_ORIGINAL_FILE_PATH)
            .status(DEFAULT_STATUS)
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .numberOfRework(DEFAULT_NUMBER_OF_REWORK)
            .lastCheckInTime(DEFAULT_LAST_CHECK_IN_TIME)
            .qcEdit(DEFAULT_QC_EDIT)
            .rework(DEFAULT_REWORK);
        // Add required entity
        JobTeamUser jobTeamUser = JobTeamUserResourceIntTest.createEntity(em);
        em.persist(jobTeamUser);
        em.flush();
        jobTeamUserTask.setJobTeamUser(jobTeamUser);
        return jobTeamUserTask;
    }

    @Before
    public void initTest() {
        jobTeamUserTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTeamUserTask() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserTaskRepository.findAll().size();

        // Create the JobTeamUserTask
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);
        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUserTask in the database
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeCreate + 1);
        JobTeamUserTask testJobTeamUserTask = jobTeamUserTaskList.get(jobTeamUserTaskList.size() - 1);
        assertThat(testJobTeamUserTask.getOriginalFileName()).isEqualTo(DEFAULT_ORIGINAL_FILE_NAME);
        assertThat(testJobTeamUserTask.getOriginalFilePath()).isEqualTo(DEFAULT_ORIGINAL_FILE_PATH);
        assertThat(testJobTeamUserTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testJobTeamUserTask.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testJobTeamUserTask.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testJobTeamUserTask.getNumberOfRework()).isEqualTo(DEFAULT_NUMBER_OF_REWORK);
        assertThat(testJobTeamUserTask.getLastCheckInTime()).isEqualTo(DEFAULT_LAST_CHECK_IN_TIME);
        assertThat(testJobTeamUserTask.isQcEdit()).isEqualTo(DEFAULT_QC_EDIT);
        assertThat(testJobTeamUserTask.isRework()).isEqualTo(DEFAULT_REWORK);
    }

    @Test
    @Transactional
    public void createJobTeamUserTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserTaskRepository.findAll().size();

        // Create the JobTeamUserTask with an existing ID
        jobTeamUserTask.setId(1L);
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobTeamUserTask in the database
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOriginalFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskRepository.findAll().size();
        // set the field null
        jobTeamUserTask.setOriginalFileName(null);

        // Create the JobTeamUserTask, which fails.
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskRepository.findAll().size();
        // set the field null
        jobTeamUserTask.setOriginalFilePath(null);

        // Create the JobTeamUserTask, which fails.
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskRepository.findAll().size();
        // set the field null
        jobTeamUserTask.setStatus(null);

        // Create the JobTeamUserTask, which fails.
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskRepository.findAll().size();
        // set the field null
        jobTeamUserTask.setFileName(null);

        // Create the JobTeamUserTask, which fails.
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskRepository.findAll().size();
        // set the field null
        jobTeamUserTask.setFilePath(null);

        // Create the JobTeamUserTask, which fails.
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(post("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobTeamUserTasks() throws Exception {
        // Initialize the database
        jobTeamUserTaskRepository.saveAndFlush(jobTeamUserTask);

        // Get all the jobTeamUserTaskList
        restJobTeamUserTaskMockMvc.perform(get("/api/job-team-user-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTeamUserTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].originalFilePath").value(hasItem(DEFAULT_ORIGINAL_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].numberOfRework").value(hasItem(DEFAULT_NUMBER_OF_REWORK)))
            .andExpect(jsonPath("$.[*].lastCheckInTime").value(hasItem(sameInstant(DEFAULT_LAST_CHECK_IN_TIME))))
            .andExpect(jsonPath("$.[*].qcEdit").value(hasItem(DEFAULT_QC_EDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].rework").value(hasItem(DEFAULT_REWORK.booleanValue())));
    }

    @Test
    @Transactional
    public void getJobTeamUserTask() throws Exception {
        // Initialize the database
        jobTeamUserTaskRepository.saveAndFlush(jobTeamUserTask);

        // Get the jobTeamUserTask
        restJobTeamUserTaskMockMvc.perform(get("/api/job-team-user-tasks/{id}", jobTeamUserTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobTeamUserTask.getId().intValue()))
            .andExpect(jsonPath("$.originalFileName").value(DEFAULT_ORIGINAL_FILE_NAME.toString()))
            .andExpect(jsonPath("$.originalFilePath").value(DEFAULT_ORIGINAL_FILE_PATH.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.numberOfRework").value(DEFAULT_NUMBER_OF_REWORK))
            .andExpect(jsonPath("$.lastCheckInTime").value(sameInstant(DEFAULT_LAST_CHECK_IN_TIME)))
            .andExpect(jsonPath("$.qcEdit").value(DEFAULT_QC_EDIT.booleanValue()))
            .andExpect(jsonPath("$.rework").value(DEFAULT_REWORK.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobTeamUserTask() throws Exception {
        // Get the jobTeamUserTask
        restJobTeamUserTaskMockMvc.perform(get("/api/job-team-user-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTeamUserTask() throws Exception {
        // Initialize the database
        jobTeamUserTaskRepository.saveAndFlush(jobTeamUserTask);
        int databaseSizeBeforeUpdate = jobTeamUserTaskRepository.findAll().size();

        // Update the jobTeamUserTask
        JobTeamUserTask updatedJobTeamUserTask = jobTeamUserTaskRepository.findOne(jobTeamUserTask.getId());
        updatedJobTeamUserTask
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .originalFilePath(UPDATED_ORIGINAL_FILE_PATH)
            .status(UPDATED_STATUS)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .numberOfRework(UPDATED_NUMBER_OF_REWORK)
            .lastCheckInTime(UPDATED_LAST_CHECK_IN_TIME)
            .qcEdit(UPDATED_QC_EDIT)
            .rework(UPDATED_REWORK);
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(updatedJobTeamUserTask);

        restJobTeamUserTaskMockMvc.perform(put("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isOk());

        // Validate the JobTeamUserTask in the database
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeUpdate);
        JobTeamUserTask testJobTeamUserTask = jobTeamUserTaskList.get(jobTeamUserTaskList.size() - 1);
        assertThat(testJobTeamUserTask.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testJobTeamUserTask.getOriginalFilePath()).isEqualTo(UPDATED_ORIGINAL_FILE_PATH);
        assertThat(testJobTeamUserTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testJobTeamUserTask.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testJobTeamUserTask.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testJobTeamUserTask.getNumberOfRework()).isEqualTo(UPDATED_NUMBER_OF_REWORK);
        assertThat(testJobTeamUserTask.getLastCheckInTime()).isEqualTo(UPDATED_LAST_CHECK_IN_TIME);
        assertThat(testJobTeamUserTask.isQcEdit()).isEqualTo(UPDATED_QC_EDIT);
        assertThat(testJobTeamUserTask.isRework()).isEqualTo(UPDATED_REWORK);
    }

    @Test
    @Transactional
    public void updateNonExistingJobTeamUserTask() throws Exception {
        int databaseSizeBeforeUpdate = jobTeamUserTaskRepository.findAll().size();

        // Create the JobTeamUserTask
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskMapper.toDto(jobTeamUserTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobTeamUserTaskMockMvc.perform(put("/api/job-team-user-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUserTask in the database
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobTeamUserTask() throws Exception {
        // Initialize the database
        jobTeamUserTaskRepository.saveAndFlush(jobTeamUserTask);
        int databaseSizeBeforeDelete = jobTeamUserTaskRepository.findAll().size();

        // Get the jobTeamUserTask
        restJobTeamUserTaskMockMvc.perform(delete("/api/job-team-user-tasks/{id}", jobTeamUserTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobTeamUserTask> jobTeamUserTaskList = jobTeamUserTaskRepository.findAll();
        assertThat(jobTeamUserTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUserTask.class);
        JobTeamUserTask jobTeamUserTask1 = new JobTeamUserTask();
        jobTeamUserTask1.setId(1L);
        JobTeamUserTask jobTeamUserTask2 = new JobTeamUserTask();
        jobTeamUserTask2.setId(jobTeamUserTask1.getId());
        assertThat(jobTeamUserTask1).isEqualTo(jobTeamUserTask2);
        jobTeamUserTask2.setId(2L);
        assertThat(jobTeamUserTask1).isNotEqualTo(jobTeamUserTask2);
        jobTeamUserTask1.setId(null);
        assertThat(jobTeamUserTask1).isNotEqualTo(jobTeamUserTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUserTaskDTO.class);
        JobTeamUserTaskDTO jobTeamUserTaskDTO1 = new JobTeamUserTaskDTO();
        jobTeamUserTaskDTO1.setId(1L);
        JobTeamUserTaskDTO jobTeamUserTaskDTO2 = new JobTeamUserTaskDTO();
        assertThat(jobTeamUserTaskDTO1).isNotEqualTo(jobTeamUserTaskDTO2);
        jobTeamUserTaskDTO2.setId(jobTeamUserTaskDTO1.getId());
        assertThat(jobTeamUserTaskDTO1).isEqualTo(jobTeamUserTaskDTO2);
        jobTeamUserTaskDTO2.setId(2L);
        assertThat(jobTeamUserTaskDTO1).isNotEqualTo(jobTeamUserTaskDTO2);
        jobTeamUserTaskDTO1.setId(null);
        assertThat(jobTeamUserTaskDTO1).isNotEqualTo(jobTeamUserTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobTeamUserTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobTeamUserTaskMapper.fromId(null)).isNull();
    }
}

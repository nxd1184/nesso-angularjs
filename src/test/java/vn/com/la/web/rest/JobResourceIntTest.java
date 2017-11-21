package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.Job;
import vn.com.la.domain.Project;
import vn.com.la.repository.JobRepository;
import vn.com.la.service.JobService;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.mapper.JobMapper;
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

/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class JobResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEADLINE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CUSTOMER_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_REQUIREMENTS = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_FILES = 1L;
    private static final Long UPDATED_TOTAL_FILES = 2L;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobMockMvc;

    private Job job;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobService);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
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
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .name(DEFAULT_NAME)
            .deadline(DEFAULT_DEADLINE)
            .customerRequirements(DEFAULT_CUSTOMER_REQUIREMENTS)
            .totalFiles(DEFAULT_TOTAL_FILES);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        job.setProject(project);
        return job;
    }

    @Before
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJob.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testJob.getCustomerRequirements()).isEqualTo(DEFAULT_CUSTOMER_REQUIREMENTS);
        assertThat(testJob.getTotalFiles()).isEqualTo(DEFAULT_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);
        JobDTO jobDTO = jobMapper.toDto(job);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setName(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.toDto(job);

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeadlineIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setDeadline(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.toDto(job);

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalFilesIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setTotalFiles(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.toDto(job);

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(sameInstant(DEFAULT_DEADLINE))))
            .andExpect(jsonPath("$.[*].customerRequirements").value(hasItem(DEFAULT_CUSTOMER_REQUIREMENTS.toString())))
            .andExpect(jsonPath("$.[*].totalFiles").value(hasItem(DEFAULT_TOTAL_FILES.intValue())));
    }

    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.deadline").value(sameInstant(DEFAULT_DEADLINE)))
            .andExpect(jsonPath("$.customerRequirements").value(DEFAULT_CUSTOMER_REQUIREMENTS.toString()))
            .andExpect(jsonPath("$.totalFiles").value(DEFAULT_TOTAL_FILES.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findOne(job.getId());
        updatedJob
            .name(UPDATED_NAME)
            .deadline(UPDATED_DEADLINE)
            .customerRequirements(UPDATED_CUSTOMER_REQUIREMENTS)
            .totalFiles(UPDATED_TOTAL_FILES);
        JobDTO jobDTO = jobMapper.toDto(updatedJob);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJob.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testJob.getCustomerRequirements()).isEqualTo(UPDATED_CUSTOMER_REQUIREMENTS);
        assertThat(testJob.getTotalFiles()).isEqualTo(UPDATED_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDTO.class);
        JobDTO jobDTO1 = new JobDTO();
        jobDTO1.setId(1L);
        JobDTO jobDTO2 = new JobDTO();
        assertThat(jobDTO1).isNotEqualTo(jobDTO2);
        jobDTO2.setId(jobDTO1.getId());
        assertThat(jobDTO1).isEqualTo(jobDTO2);
        jobDTO2.setId(2L);
        assertThat(jobDTO1).isNotEqualTo(jobDTO2);
        jobDTO1.setId(null);
        assertThat(jobDTO1).isNotEqualTo(jobDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobMapper.fromId(null)).isNull();
    }
}

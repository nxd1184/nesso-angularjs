package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.JobTeamUserTaskTracking;
import vn.com.la.domain.User;
import vn.com.la.repository.JobTeamUserTaskTrackingRepository;
import vn.com.la.service.JobTeamUserTaskTrackingService;
import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;
import vn.com.la.service.mapper.JobTeamUserTaskTrackingMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import vn.com.la.domain.enumeration.FileStatus;
/**
 * Test class for the JobTeamUserTaskTrackingResource REST controller.
 *
 * @see JobTeamUserTaskTrackingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class JobTeamUserTaskTrackingResourceIntTest {

    private static final FileStatus DEFAULT_STATUS = FileStatus.TODO;
    private static final FileStatus UPDATED_STATUS = FileStatus.TOCHECK;

    @Autowired
    private JobTeamUserTaskTrackingRepository jobTeamUserTaskTrackingRepository;

    @Autowired
    private JobTeamUserTaskTrackingMapper jobTeamUserTaskTrackingMapper;

    @Autowired
    private JobTeamUserTaskTrackingService jobTeamUserTaskTrackingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobTeamUserTaskTrackingMockMvc;

    private JobTeamUserTaskTracking jobTeamUserTaskTracking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobTeamUserTaskTrackingResource jobTeamUserTaskTrackingResource = new JobTeamUserTaskTrackingResource(jobTeamUserTaskTrackingService);
        this.restJobTeamUserTaskTrackingMockMvc = MockMvcBuilders.standaloneSetup(jobTeamUserTaskTrackingResource)
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
    public static JobTeamUserTaskTracking createEntity(EntityManager em) {
        JobTeamUserTaskTracking jobTeamUserTaskTracking = new JobTeamUserTaskTracking()
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        jobTeamUserTaskTracking.setUser(user);
        return jobTeamUserTaskTracking;
    }

    @Before
    public void initTest() {
        jobTeamUserTaskTracking = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTeamUserTaskTracking() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserTaskTrackingRepository.findAll().size();

        // Create the JobTeamUserTaskTracking
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);
        restJobTeamUserTaskTrackingMockMvc.perform(post("/api/job-team-user-task-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUserTaskTracking in the database
        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeCreate + 1);
        JobTeamUserTaskTracking testJobTeamUserTaskTracking = jobTeamUserTaskTrackingList.get(jobTeamUserTaskTrackingList.size() - 1);
        assertThat(testJobTeamUserTaskTracking.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createJobTeamUserTaskTrackingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserTaskTrackingRepository.findAll().size();

        // Create the JobTeamUserTaskTracking with an existing ID
        jobTeamUserTaskTracking.setId(1L);
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTeamUserTaskTrackingMockMvc.perform(post("/api/job-team-user-task-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobTeamUserTaskTracking in the database
        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserTaskTrackingRepository.findAll().size();
        // set the field null
        jobTeamUserTaskTracking.setStatus(null);

        // Create the JobTeamUserTaskTracking, which fails.
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);

        restJobTeamUserTaskTrackingMockMvc.perform(post("/api/job-team-user-task-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskTrackingDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobTeamUserTaskTrackings() throws Exception {
        // Initialize the database
        jobTeamUserTaskTrackingRepository.saveAndFlush(jobTeamUserTaskTracking);

        // Get all the jobTeamUserTaskTrackingList
        restJobTeamUserTaskTrackingMockMvc.perform(get("/api/job-team-user-task-trackings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTeamUserTaskTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getJobTeamUserTaskTracking() throws Exception {
        // Initialize the database
        jobTeamUserTaskTrackingRepository.saveAndFlush(jobTeamUserTaskTracking);

        // Get the jobTeamUserTaskTracking
        restJobTeamUserTaskTrackingMockMvc.perform(get("/api/job-team-user-task-trackings/{id}", jobTeamUserTaskTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobTeamUserTaskTracking.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobTeamUserTaskTracking() throws Exception {
        // Get the jobTeamUserTaskTracking
        restJobTeamUserTaskTrackingMockMvc.perform(get("/api/job-team-user-task-trackings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTeamUserTaskTracking() throws Exception {
        // Initialize the database
        jobTeamUserTaskTrackingRepository.saveAndFlush(jobTeamUserTaskTracking);
        int databaseSizeBeforeUpdate = jobTeamUserTaskTrackingRepository.findAll().size();

        // Update the jobTeamUserTaskTracking
        JobTeamUserTaskTracking updatedJobTeamUserTaskTracking = jobTeamUserTaskTrackingRepository.findOne(jobTeamUserTaskTracking.getId());
        updatedJobTeamUserTaskTracking
            .status(UPDATED_STATUS);
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingMapper.toDto(updatedJobTeamUserTaskTracking);

        restJobTeamUserTaskTrackingMockMvc.perform(put("/api/job-team-user-task-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskTrackingDTO)))
            .andExpect(status().isOk());

        // Validate the JobTeamUserTaskTracking in the database
        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeUpdate);
        JobTeamUserTaskTracking testJobTeamUserTaskTracking = jobTeamUserTaskTrackingList.get(jobTeamUserTaskTrackingList.size() - 1);
        assertThat(testJobTeamUserTaskTracking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingJobTeamUserTaskTracking() throws Exception {
        int databaseSizeBeforeUpdate = jobTeamUserTaskTrackingRepository.findAll().size();

        // Create the JobTeamUserTaskTracking
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobTeamUserTaskTrackingMockMvc.perform(put("/api/job-team-user-task-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserTaskTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUserTaskTracking in the database
        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobTeamUserTaskTracking() throws Exception {
        // Initialize the database
        jobTeamUserTaskTrackingRepository.saveAndFlush(jobTeamUserTaskTracking);
        int databaseSizeBeforeDelete = jobTeamUserTaskTrackingRepository.findAll().size();

        // Get the jobTeamUserTaskTracking
        restJobTeamUserTaskTrackingMockMvc.perform(delete("/api/job-team-user-task-trackings/{id}", jobTeamUserTaskTracking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobTeamUserTaskTracking> jobTeamUserTaskTrackingList = jobTeamUserTaskTrackingRepository.findAll();
        assertThat(jobTeamUserTaskTrackingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUserTaskTracking.class);
        JobTeamUserTaskTracking jobTeamUserTaskTracking1 = new JobTeamUserTaskTracking();
        jobTeamUserTaskTracking1.setId(1L);
        JobTeamUserTaskTracking jobTeamUserTaskTracking2 = new JobTeamUserTaskTracking();
        jobTeamUserTaskTracking2.setId(jobTeamUserTaskTracking1.getId());
        assertThat(jobTeamUserTaskTracking1).isEqualTo(jobTeamUserTaskTracking2);
        jobTeamUserTaskTracking2.setId(2L);
        assertThat(jobTeamUserTaskTracking1).isNotEqualTo(jobTeamUserTaskTracking2);
        jobTeamUserTaskTracking1.setId(null);
        assertThat(jobTeamUserTaskTracking1).isNotEqualTo(jobTeamUserTaskTracking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUserTaskTrackingDTO.class);
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO1 = new JobTeamUserTaskTrackingDTO();
        jobTeamUserTaskTrackingDTO1.setId(1L);
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO2 = new JobTeamUserTaskTrackingDTO();
        assertThat(jobTeamUserTaskTrackingDTO1).isNotEqualTo(jobTeamUserTaskTrackingDTO2);
        jobTeamUserTaskTrackingDTO2.setId(jobTeamUserTaskTrackingDTO1.getId());
        assertThat(jobTeamUserTaskTrackingDTO1).isEqualTo(jobTeamUserTaskTrackingDTO2);
        jobTeamUserTaskTrackingDTO2.setId(2L);
        assertThat(jobTeamUserTaskTrackingDTO1).isNotEqualTo(jobTeamUserTaskTrackingDTO2);
        jobTeamUserTaskTrackingDTO1.setId(null);
        assertThat(jobTeamUserTaskTrackingDTO1).isNotEqualTo(jobTeamUserTaskTrackingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobTeamUserTaskTrackingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobTeamUserTaskTrackingMapper.fromId(null)).isNull();
    }
}

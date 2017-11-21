package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.JobTeam;
import vn.com.la.domain.Job;
import vn.com.la.domain.Team;
import vn.com.la.domain.Project;
import vn.com.la.repository.JobTeamRepository;
import vn.com.la.service.JobTeamService;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.mapper.JobTeamMapper;
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

/**
 * Test class for the JobTeamResource REST controller.
 *
 * @see JobTeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class JobTeamResourceIntTest {

    private static final Long DEFAULT_TOTAL_FILES = 1L;
    private static final Long UPDATED_TOTAL_FILES = 2L;

    @Autowired
    private JobTeamRepository jobTeamRepository;

    @Autowired
    private JobTeamMapper jobTeamMapper;

    @Autowired
    private JobTeamService jobTeamService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobTeamMockMvc;

    private JobTeam jobTeam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobTeamResource jobTeamResource = new JobTeamResource(jobTeamService);
        this.restJobTeamMockMvc = MockMvcBuilders.standaloneSetup(jobTeamResource)
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
    public static JobTeam createEntity(EntityManager em) {
        JobTeam jobTeam = new JobTeam()
            .totalFiles(DEFAULT_TOTAL_FILES);
        // Add required entity
        Job job = JobResourceIntTest.createEntity(em);
        em.persist(job);
        em.flush();
        jobTeam.setJob(job);
        // Add required entity
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        jobTeam.setTeam(team);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        jobTeam.setProject(project);
        return jobTeam;
    }

    @Before
    public void initTest() {
        jobTeam = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTeam() throws Exception {
        int databaseSizeBeforeCreate = jobTeamRepository.findAll().size();

        // Create the JobTeam
        JobTeamDTO jobTeamDTO = jobTeamMapper.toDto(jobTeam);
        restJobTeamMockMvc.perform(post("/api/job-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeam in the database
        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeCreate + 1);
        JobTeam testJobTeam = jobTeamList.get(jobTeamList.size() - 1);
        assertThat(testJobTeam.getTotalFiles()).isEqualTo(DEFAULT_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void createJobTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTeamRepository.findAll().size();

        // Create the JobTeam with an existing ID
        jobTeam.setId(1L);
        JobTeamDTO jobTeamDTO = jobTeamMapper.toDto(jobTeam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTeamMockMvc.perform(post("/api/job-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobTeam in the database
        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTotalFilesIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamRepository.findAll().size();
        // set the field null
        jobTeam.setTotalFiles(null);

        // Create the JobTeam, which fails.
        JobTeamDTO jobTeamDTO = jobTeamMapper.toDto(jobTeam);

        restJobTeamMockMvc.perform(post("/api/job-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobTeams() throws Exception {
        // Initialize the database
        jobTeamRepository.saveAndFlush(jobTeam);

        // Get all the jobTeamList
        restJobTeamMockMvc.perform(get("/api/job-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalFiles").value(hasItem(DEFAULT_TOTAL_FILES.intValue())));
    }

    @Test
    @Transactional
    public void getJobTeam() throws Exception {
        // Initialize the database
        jobTeamRepository.saveAndFlush(jobTeam);

        // Get the jobTeam
        restJobTeamMockMvc.perform(get("/api/job-teams/{id}", jobTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobTeam.getId().intValue()))
            .andExpect(jsonPath("$.totalFiles").value(DEFAULT_TOTAL_FILES.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobTeam() throws Exception {
        // Get the jobTeam
        restJobTeamMockMvc.perform(get("/api/job-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTeam() throws Exception {
        // Initialize the database
        jobTeamRepository.saveAndFlush(jobTeam);
        int databaseSizeBeforeUpdate = jobTeamRepository.findAll().size();

        // Update the jobTeam
        JobTeam updatedJobTeam = jobTeamRepository.findOne(jobTeam.getId());
        updatedJobTeam
            .totalFiles(UPDATED_TOTAL_FILES);
        JobTeamDTO jobTeamDTO = jobTeamMapper.toDto(updatedJobTeam);

        restJobTeamMockMvc.perform(put("/api/job-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamDTO)))
            .andExpect(status().isOk());

        // Validate the JobTeam in the database
        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeUpdate);
        JobTeam testJobTeam = jobTeamList.get(jobTeamList.size() - 1);
        assertThat(testJobTeam.getTotalFiles()).isEqualTo(UPDATED_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void updateNonExistingJobTeam() throws Exception {
        int databaseSizeBeforeUpdate = jobTeamRepository.findAll().size();

        // Create the JobTeam
        JobTeamDTO jobTeamDTO = jobTeamMapper.toDto(jobTeam);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobTeamMockMvc.perform(put("/api/job-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeam in the database
        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobTeam() throws Exception {
        // Initialize the database
        jobTeamRepository.saveAndFlush(jobTeam);
        int databaseSizeBeforeDelete = jobTeamRepository.findAll().size();

        // Get the jobTeam
        restJobTeamMockMvc.perform(delete("/api/job-teams/{id}", jobTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobTeam> jobTeamList = jobTeamRepository.findAll();
        assertThat(jobTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeam.class);
        JobTeam jobTeam1 = new JobTeam();
        jobTeam1.setId(1L);
        JobTeam jobTeam2 = new JobTeam();
        jobTeam2.setId(jobTeam1.getId());
        assertThat(jobTeam1).isEqualTo(jobTeam2);
        jobTeam2.setId(2L);
        assertThat(jobTeam1).isNotEqualTo(jobTeam2);
        jobTeam1.setId(null);
        assertThat(jobTeam1).isNotEqualTo(jobTeam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamDTO.class);
        JobTeamDTO jobTeamDTO1 = new JobTeamDTO();
        jobTeamDTO1.setId(1L);
        JobTeamDTO jobTeamDTO2 = new JobTeamDTO();
        assertThat(jobTeamDTO1).isNotEqualTo(jobTeamDTO2);
        jobTeamDTO2.setId(jobTeamDTO1.getId());
        assertThat(jobTeamDTO1).isEqualTo(jobTeamDTO2);
        jobTeamDTO2.setId(2L);
        assertThat(jobTeamDTO1).isNotEqualTo(jobTeamDTO2);
        jobTeamDTO1.setId(null);
        assertThat(jobTeamDTO1).isNotEqualTo(jobTeamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobTeamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobTeamMapper.fromId(null)).isNull();
    }
}

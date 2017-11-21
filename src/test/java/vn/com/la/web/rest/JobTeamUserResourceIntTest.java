package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.JobTeam;
import vn.com.la.domain.User;
import vn.com.la.repository.JobTeamUserRepository;
import vn.com.la.service.JobTeamUserService;
import vn.com.la.service.dto.JobTeamUserDTO;
import vn.com.la.service.mapper.JobTeamUserMapper;
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
 * Test class for the JobTeamUserResource REST controller.
 *
 * @see JobTeamUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class JobTeamUserResourceIntTest {

    private static final Long DEFAULT_TOTAL_FILES = 1L;
    private static final Long UPDATED_TOTAL_FILES = 2L;

    @Autowired
    private JobTeamUserRepository jobTeamUserRepository;

    @Autowired
    private JobTeamUserMapper jobTeamUserMapper;

    @Autowired
    private JobTeamUserService jobTeamUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobTeamUserMockMvc;

    private JobTeamUser jobTeamUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobTeamUserResource jobTeamUserResource = new JobTeamUserResource(jobTeamUserService);
        this.restJobTeamUserMockMvc = MockMvcBuilders.standaloneSetup(jobTeamUserResource)
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
    public static JobTeamUser createEntity(EntityManager em) {
        JobTeamUser jobTeamUser = new JobTeamUser()
            .totalFiles(DEFAULT_TOTAL_FILES);
        // Add required entity
        JobTeam jobTeam = JobTeamResourceIntTest.createEntity(em);
        em.persist(jobTeam);
        em.flush();
        jobTeamUser.setJobTeam(jobTeam);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        jobTeamUser.setUser(user);
        return jobTeamUser;
    }

    @Before
    public void initTest() {
        jobTeamUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTeamUser() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserRepository.findAll().size();

        // Create the JobTeamUser
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserMapper.toDto(jobTeamUser);
        restJobTeamUserMockMvc.perform(post("/api/job-team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUser in the database
        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeCreate + 1);
        JobTeamUser testJobTeamUser = jobTeamUserList.get(jobTeamUserList.size() - 1);
        assertThat(testJobTeamUser.getTotalFiles()).isEqualTo(DEFAULT_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void createJobTeamUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTeamUserRepository.findAll().size();

        // Create the JobTeamUser with an existing ID
        jobTeamUser.setId(1L);
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserMapper.toDto(jobTeamUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTeamUserMockMvc.perform(post("/api/job-team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobTeamUser in the database
        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTotalFilesIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTeamUserRepository.findAll().size();
        // set the field null
        jobTeamUser.setTotalFiles(null);

        // Create the JobTeamUser, which fails.
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserMapper.toDto(jobTeamUser);

        restJobTeamUserMockMvc.perform(post("/api/job-team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserDTO)))
            .andExpect(status().isBadRequest());

        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobTeamUsers() throws Exception {
        // Initialize the database
        jobTeamUserRepository.saveAndFlush(jobTeamUser);

        // Get all the jobTeamUserList
        restJobTeamUserMockMvc.perform(get("/api/job-team-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTeamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalFiles").value(hasItem(DEFAULT_TOTAL_FILES.intValue())));
    }

    @Test
    @Transactional
    public void getJobTeamUser() throws Exception {
        // Initialize the database
        jobTeamUserRepository.saveAndFlush(jobTeamUser);

        // Get the jobTeamUser
        restJobTeamUserMockMvc.perform(get("/api/job-team-users/{id}", jobTeamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobTeamUser.getId().intValue()))
            .andExpect(jsonPath("$.totalFiles").value(DEFAULT_TOTAL_FILES.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobTeamUser() throws Exception {
        // Get the jobTeamUser
        restJobTeamUserMockMvc.perform(get("/api/job-team-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTeamUser() throws Exception {
        // Initialize the database
        jobTeamUserRepository.saveAndFlush(jobTeamUser);
        int databaseSizeBeforeUpdate = jobTeamUserRepository.findAll().size();

        // Update the jobTeamUser
        JobTeamUser updatedJobTeamUser = jobTeamUserRepository.findOne(jobTeamUser.getId());
        updatedJobTeamUser
            .totalFiles(UPDATED_TOTAL_FILES);
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserMapper.toDto(updatedJobTeamUser);

        restJobTeamUserMockMvc.perform(put("/api/job-team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserDTO)))
            .andExpect(status().isOk());

        // Validate the JobTeamUser in the database
        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeUpdate);
        JobTeamUser testJobTeamUser = jobTeamUserList.get(jobTeamUserList.size() - 1);
        assertThat(testJobTeamUser.getTotalFiles()).isEqualTo(UPDATED_TOTAL_FILES);
    }

    @Test
    @Transactional
    public void updateNonExistingJobTeamUser() throws Exception {
        int databaseSizeBeforeUpdate = jobTeamUserRepository.findAll().size();

        // Create the JobTeamUser
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserMapper.toDto(jobTeamUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobTeamUserMockMvc.perform(put("/api/job-team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobTeamUserDTO)))
            .andExpect(status().isCreated());

        // Validate the JobTeamUser in the database
        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobTeamUser() throws Exception {
        // Initialize the database
        jobTeamUserRepository.saveAndFlush(jobTeamUser);
        int databaseSizeBeforeDelete = jobTeamUserRepository.findAll().size();

        // Get the jobTeamUser
        restJobTeamUserMockMvc.perform(delete("/api/job-team-users/{id}", jobTeamUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobTeamUser> jobTeamUserList = jobTeamUserRepository.findAll();
        assertThat(jobTeamUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUser.class);
        JobTeamUser jobTeamUser1 = new JobTeamUser();
        jobTeamUser1.setId(1L);
        JobTeamUser jobTeamUser2 = new JobTeamUser();
        jobTeamUser2.setId(jobTeamUser1.getId());
        assertThat(jobTeamUser1).isEqualTo(jobTeamUser2);
        jobTeamUser2.setId(2L);
        assertThat(jobTeamUser1).isNotEqualTo(jobTeamUser2);
        jobTeamUser1.setId(null);
        assertThat(jobTeamUser1).isNotEqualTo(jobTeamUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTeamUserDTO.class);
        JobTeamUserDTO jobTeamUserDTO1 = new JobTeamUserDTO();
        jobTeamUserDTO1.setId(1L);
        JobTeamUserDTO jobTeamUserDTO2 = new JobTeamUserDTO();
        assertThat(jobTeamUserDTO1).isNotEqualTo(jobTeamUserDTO2);
        jobTeamUserDTO2.setId(jobTeamUserDTO1.getId());
        assertThat(jobTeamUserDTO1).isEqualTo(jobTeamUserDTO2);
        jobTeamUserDTO2.setId(2L);
        assertThat(jobTeamUserDTO1).isNotEqualTo(jobTeamUserDTO2);
        jobTeamUserDTO1.setId(null);
        assertThat(jobTeamUserDTO1).isNotEqualTo(jobTeamUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(jobTeamUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(jobTeamUserMapper.fromId(null)).isNull();
    }
}

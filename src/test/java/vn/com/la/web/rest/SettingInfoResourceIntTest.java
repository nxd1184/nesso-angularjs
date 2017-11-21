package vn.com.la.web.rest;

import vn.com.la.NessoApp;

import vn.com.la.domain.SettingInfo;
import vn.com.la.domain.UserSetting;
import vn.com.la.repository.SettingInfoRepository;
import vn.com.la.service.SettingInfoService;
import vn.com.la.service.dto.SettingInfoDTO;
import vn.com.la.service.mapper.SettingInfoMapper;
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

import vn.com.la.domain.enumeration.DataTypeEnum;
/**
 * Test class for the SettingInfoResource REST controller.
 *
 * @see SettingInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NessoApp.class)
public class SettingInfoResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final DataTypeEnum DEFAULT_TYPE = DataTypeEnum.STRING;
    private static final DataTypeEnum UPDATED_TYPE = DataTypeEnum.NUMBER;

    @Autowired
    private SettingInfoRepository settingInfoRepository;

    @Autowired
    private SettingInfoMapper settingInfoMapper;

    @Autowired
    private SettingInfoService settingInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSettingInfoMockMvc;

    private SettingInfo settingInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SettingInfoResource settingInfoResource = new SettingInfoResource(settingInfoService);
        this.restSettingInfoMockMvc = MockMvcBuilders.standaloneSetup(settingInfoResource)
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
    public static SettingInfo createEntity(EntityManager em) {
        SettingInfo settingInfo = new SettingInfo()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .type(DEFAULT_TYPE);
        // Add required entity
        UserSetting setting = UserSettingResourceIntTest.createEntity(em);
        em.persist(setting);
        em.flush();
        settingInfo.setSetting(setting);
        return settingInfo;
    }

    @Before
    public void initTest() {
        settingInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSettingInfo() throws Exception {
        int databaseSizeBeforeCreate = settingInfoRepository.findAll().size();

        // Create the SettingInfo
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);
        restSettingInfoMockMvc.perform(post("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SettingInfo testSettingInfo = settingInfoList.get(settingInfoList.size() - 1);
        assertThat(testSettingInfo.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSettingInfo.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSettingInfo.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createSettingInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = settingInfoRepository.findAll().size();

        // Create the SettingInfo with an existing ID
        settingInfo.setId(1L);
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingInfoMockMvc.perform(post("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = settingInfoRepository.findAll().size();
        // set the field null
        settingInfo.setKey(null);

        // Create the SettingInfo, which fails.
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);

        restSettingInfoMockMvc.perform(post("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = settingInfoRepository.findAll().size();
        // set the field null
        settingInfo.setValue(null);

        // Create the SettingInfo, which fails.
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);

        restSettingInfoMockMvc.perform(post("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = settingInfoRepository.findAll().size();
        // set the field null
        settingInfo.setType(null);

        // Create the SettingInfo, which fails.
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);

        restSettingInfoMockMvc.perform(post("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSettingInfos() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);

        // Get all the settingInfoList
        restSettingInfoMockMvc.perform(get("/api/setting-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);

        // Get the settingInfo
        restSettingInfoMockMvc.perform(get("/api/setting-infos/{id}", settingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(settingInfo.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSettingInfo() throws Exception {
        // Get the settingInfo
        restSettingInfoMockMvc.perform(get("/api/setting-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);
        int databaseSizeBeforeUpdate = settingInfoRepository.findAll().size();

        // Update the settingInfo
        SettingInfo updatedSettingInfo = settingInfoRepository.findOne(settingInfo.getId());
        updatedSettingInfo
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .type(UPDATED_TYPE);
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(updatedSettingInfo);

        restSettingInfoMockMvc.perform(put("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isOk());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeUpdate);
        SettingInfo testSettingInfo = settingInfoList.get(settingInfoList.size() - 1);
        assertThat(testSettingInfo.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSettingInfo.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSettingInfo.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSettingInfo() throws Exception {
        int databaseSizeBeforeUpdate = settingInfoRepository.findAll().size();

        // Create the SettingInfo
        SettingInfoDTO settingInfoDTO = settingInfoMapper.toDto(settingInfo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSettingInfoMockMvc.perform(put("/api/setting-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);
        int databaseSizeBeforeDelete = settingInfoRepository.findAll().size();

        // Get the settingInfo
        restSettingInfoMockMvc.perform(delete("/api/setting-infos/{id}", settingInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SettingInfo> settingInfoList = settingInfoRepository.findAll();
        assertThat(settingInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingInfo.class);
        SettingInfo settingInfo1 = new SettingInfo();
        settingInfo1.setId(1L);
        SettingInfo settingInfo2 = new SettingInfo();
        settingInfo2.setId(settingInfo1.getId());
        assertThat(settingInfo1).isEqualTo(settingInfo2);
        settingInfo2.setId(2L);
        assertThat(settingInfo1).isNotEqualTo(settingInfo2);
        settingInfo1.setId(null);
        assertThat(settingInfo1).isNotEqualTo(settingInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingInfoDTO.class);
        SettingInfoDTO settingInfoDTO1 = new SettingInfoDTO();
        settingInfoDTO1.setId(1L);
        SettingInfoDTO settingInfoDTO2 = new SettingInfoDTO();
        assertThat(settingInfoDTO1).isNotEqualTo(settingInfoDTO2);
        settingInfoDTO2.setId(settingInfoDTO1.getId());
        assertThat(settingInfoDTO1).isEqualTo(settingInfoDTO2);
        settingInfoDTO2.setId(2L);
        assertThat(settingInfoDTO1).isNotEqualTo(settingInfoDTO2);
        settingInfoDTO1.setId(null);
        assertThat(settingInfoDTO1).isNotEqualTo(settingInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(settingInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(settingInfoMapper.fromId(null)).isNull();
    }
}

package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import vn.com.la.service.UserSettingService;
import vn.com.la.service.dto.param.SearchUserSettingParamDTO;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.UserSettingDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.web.rest.vm.response.DatatableResponseVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserSetting.
 */
@RestController
@RequestMapping("/api")
public class UserSettingResource {

    private final Logger log = LoggerFactory.getLogger(UserSettingResource.class);

    private static final String ENTITY_NAME = "userSetting";

    private final UserSettingService userSettingService;

    public UserSettingResource(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    /**
     * POST  /user-settings : Create a new userSetting.
     *
     * @param userSettingDTO the userSettingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSettingDTO, or with status 400 (Bad Request) if the userSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-settings")
    @Timed
    public ResponseEntity<UserSettingDTO> createUserSetting(@Valid @RequestBody UserSettingDTO userSettingDTO) throws URISyntaxException {
        log.debug("REST request to save UserSetting : {}", userSettingDTO);
        if (userSettingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userSetting cannot already have an ID")).body(null);
        }
        UserSettingDTO result = userSettingService.save(userSettingDTO);
        return ResponseEntity.created(new URI("/api/user-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-settings : Updates an existing userSetting.
     *
     * @param userSettingDTO the userSettingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userSettingDTO,
     * or with status 400 (Bad Request) if the userSettingDTO is not valid,
     * or with status 500 (Internal Server Error) if the userSettingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-settings")
    @Timed
    public ResponseEntity<UserSettingDTO> updateUserSetting(@Valid @RequestBody UserSettingDTO userSettingDTO) throws URISyntaxException {
        log.debug("REST request to update UserSetting : {}", userSettingDTO);
        if (userSettingDTO.getId() == null) {
            return createUserSetting(userSettingDTO);
        }
        UserSettingDTO result = userSettingService.save(userSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-settings : get all the userSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userSettings in body
     */
    @GetMapping("/user-settings")
    @Timed
    public ResponseEntity<List<UserSettingDTO>> getAllUserSettings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserSettings");
        Page<UserSettingDTO> page = userSettingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-settings/:id : get the "id" userSetting.
     *
     * @param id the id of the userSettingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSettingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-settings/{id}")
    @Timed
    public ResponseEntity<UserSettingDTO> getUserSetting(@PathVariable Long id) {
        log.debug("REST request to get UserSetting : {}", id);
        UserSettingDTO userSettingDTO = userSettingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userSettingDTO));
    }

    /**
     * DELETE  /user-settings/:id : delete the "id" userSetting.
     *
     * @param id the id of the userSettingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-settings/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserSetting(@PathVariable Long id) {
        log.debug("REST request to delete UserSetting : {}", id);
        userSettingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/search-user-settings")
    @Timed
    public ResponseEntity<DatatableResponseVM> search(@ApiParam Pageable pageable,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "date", required = false) String date) {
        log.debug("REST request to search UserSetting");


        SearchUserSettingParamDTO params = new SearchUserSettingParamDTO();
        params.setName(name);

        if(StringUtils.isNotBlank(date)) {
            date = LACommonUtil.decode(date);
            params.setDate(LADateTimeUtil.isoStringToZonedDateTime(date));
        }

        Page<UserSettingDTO> page = userSettingService.search(params, pageable);

        DatatableResponseVM rs = new DatatableResponseVM(page);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}

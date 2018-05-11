package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.service.IgnoreNameService;
import vn.com.la.service.UserSettingService;
import vn.com.la.service.dto.IgnoreNameDTO;
import vn.com.la.service.dto.UserSettingDTO;
import vn.com.la.service.dto.param.SearchUserSettingParamDTO;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.web.rest.vm.response.DatatableResponseVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing IgnoreName.
 */
@RestController
@RequestMapping("/api")
public class IgnoreNameResource {

    private final Logger log = LoggerFactory.getLogger(IgnoreNameResource.class);

    private static final String ENTITY_NAME = "ignoreName";

    private final IgnoreNameService ignoreNameService;

    public IgnoreNameResource(IgnoreNameService ignoreNameService) {
        this.ignoreNameService = ignoreNameService;
    }

    /**
     * POST  /ignore-names : Create a new userSetting.
     *
     * @param ignoreNameDTO the ignoreNameDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSettingDTO, or with status 400 (Bad Request) if the userSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ignore-names")
    @Timed
    public ResponseEntity<IgnoreNameDTO> createIgnoreName(@Valid @RequestBody IgnoreNameDTO ignoreNameDTO) throws URISyntaxException {
        log.debug("REST request to save UserSetting : {}", ignoreNameDTO);
        if (ignoreNameDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ignoreName cannot already have an ID")).body(null);
        }
        IgnoreNameDTO result = ignoreNameService.save(ignoreNameDTO);
        return ResponseEntity.created(new URI("/api/ignore-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * GET  /user-settings : get all the userSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userSettings in body
     */
    @GetMapping("/ignore-names")
    @Timed
    public ResponseEntity<DatatableResponseVM> getAllIgnoreNames(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserSettings");
        Page<IgnoreNameDTO> page = ignoreNameService.findAll(pageable);

        DatatableResponseVM response = new DatatableResponseVM();
        response.setData(page.getContent());
        response.setRecordsFiltered(page.getTotalElements());
        response.setRecordsTotal(page.getTotalElements());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(response));
    }

    /**
     * DELETE  /ignore-names/:id : delete the "id" userSetting.
     *
     * @param id the id of the ignoreNameDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ignore-names/{id}")
    @Timed
    public ResponseEntity<Void> deleteIgnoreName(@PathVariable Long id) {
        log.debug("REST request to delete IgnoreName : {}", id);
        ignoreNameService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

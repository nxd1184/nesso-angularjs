package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.com.la.service.SettingInfoService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.SettingInfoDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SettingInfo.
 */
@RestController
@RequestMapping("/api")
public class SettingInfoResource {

    private final Logger log = LoggerFactory.getLogger(SettingInfoResource.class);

    private static final String ENTITY_NAME = "settingInfo";

    private final SettingInfoService settingInfoService;

    public SettingInfoResource(SettingInfoService settingInfoService) {
        this.settingInfoService = settingInfoService;
    }

    /**
     * POST  /setting-infos : Create a new settingInfo.
     *
     * @param settingInfoDTO the settingInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new settingInfoDTO, or with status 400 (Bad Request) if the settingInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/setting-infos")
    @Timed
    public ResponseEntity<SettingInfoDTO> createSettingInfo(@Valid @RequestBody SettingInfoDTO settingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SettingInfo : {}", settingInfoDTO);
        if (settingInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new settingInfo cannot already have an ID")).body(null);
        }
        SettingInfoDTO result = settingInfoService.save(settingInfoDTO);
        return ResponseEntity.created(new URI("/api/setting-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /setting-infos : Updates an existing settingInfo.
     *
     * @param settingInfoDTO the settingInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated settingInfoDTO,
     * or with status 400 (Bad Request) if the settingInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the settingInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/setting-infos")
    @Timed
    public ResponseEntity<SettingInfoDTO> updateSettingInfo(@Valid @RequestBody SettingInfoDTO settingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SettingInfo : {}", settingInfoDTO);
        if (settingInfoDTO.getId() == null) {
            return createSettingInfo(settingInfoDTO);
        }
        SettingInfoDTO result = settingInfoService.save(settingInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, settingInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /setting-infos : get all the settingInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of settingInfos in body
     */
    @GetMapping("/setting-infos")
    @Timed
    public ResponseEntity<List<SettingInfoDTO>> getAllSettingInfos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SettingInfos");
        Page<SettingInfoDTO> page = settingInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/setting-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /setting-infos/:id : get the "id" settingInfo.
     *
     * @param id the id of the settingInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the settingInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/setting-infos/{id}")
    @Timed
    public ResponseEntity<SettingInfoDTO> getSettingInfo(@PathVariable Long id) {
        log.debug("REST request to get SettingInfo : {}", id);
        SettingInfoDTO settingInfoDTO = settingInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(settingInfoDTO));
    }

    /**
     * DELETE  /setting-infos/:id : delete the "id" settingInfo.
     *
     * @param id the id of the settingInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/setting-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSettingInfo(@PathVariable Long id) {
        log.debug("REST request to delete SettingInfo : {}", id);
        settingInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

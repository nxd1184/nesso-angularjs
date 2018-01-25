package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.service.FileSystemHandlingService;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.web.rest.vm.response.ListFileResponseVM;
import vn.com.la.web.rest.vm.response.ListFolderResponseVM;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for folder screen.
 */
@RestController
@RequestMapping("/api")
public class ResourceAPI {

    private final Logger log = LoggerFactory.getLogger(ResourceAPI.class);

    private static final String ENTITY_NAME = "job";

    private final FileSystemHandlingService fileSystemHandlingService;

    public ResourceAPI(FileSystemHandlingService fileSystemHandlingService) {
        this.fileSystemHandlingService = fileSystemHandlingService;
    }

    /**
     * GET  /directory/{path} : get all directory from path.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @GetMapping("/nfs/directories")
    @Timed
    public ResponseEntity<ListFolderResponseVM> getDirectories(@RequestParam(required = false) String path) {
        log.debug("REST request to get directories from path : {}", path);
        ListFolderResponseVM rs = fileSystemHandlingService.listNfsFolderFromPath(path);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    /**
     * GET  /directory/{path} : get all directory from path.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @GetMapping("/nfs/files")
    @Timed
    public ResponseEntity<ListFileResponseVM> getFiles(@RequestParam String path) {
        log.debug("REST request to get files from path : {}", path);
        ListFileResponseVM rs = fileSystemHandlingService.listNfsFileFromPath(path);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}

package vn.com.la.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.la.service.dto.IgnoreNameDTO;

import java.util.List;

/**
 * @author steven on 5/10/18
 * @project nesso-angularjs
 */
public interface IgnoreNameService {

    /**
     * Save a job.
     *
     * @param ignoreNameDTO the entity to save
     * @return the persisted entity
     */
    IgnoreNameDTO save(IgnoreNameDTO ignoreNameDTO);

    /**
     *  Get all the jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IgnoreNameDTO> findAll(Pageable pageable);

    List<IgnoreNameDTO> findAll();

    /**
     *  Get the "id" job.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IgnoreNameDTO findOne(Long id);

    /**
     *  Delete the "id" job.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

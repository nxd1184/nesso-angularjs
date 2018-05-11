package vn.com.la.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.domain.IgnoreName;
import vn.com.la.repository.IgnoreNameRepository;
import vn.com.la.service.IgnoreNameService;
import vn.com.la.service.dto.IgnoreNameDTO;
import vn.com.la.service.mapper.IgnoreNameMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author steven on 5/10/18
 * @project nesso-angularjs
 */

@Service
@Transactional
public class IgnoreNameServiceImpl implements IgnoreNameService {

    private final Logger log = LoggerFactory.getLogger(IgnoreNameServiceImpl.class);

    private final IgnoreNameRepository repository;
    private final IgnoreNameMapper ignoreNameMapper;

    public IgnoreNameServiceImpl(IgnoreNameRepository repository, IgnoreNameMapper ignoreNameMapper) {
        this.repository = repository;
        this.ignoreNameMapper = ignoreNameMapper;
    }

    @Override
    public IgnoreNameDTO save(IgnoreNameDTO ignoreNameDTO) {
        IgnoreName ignoreName = ignoreNameMapper.toEntity(ignoreNameDTO);
        ignoreName = repository.save(ignoreName);
        return ignoreNameMapper.toDto(ignoreName);
    }

    @Override
    public Page<IgnoreNameDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ignoreNameMapper::toDto);
    }

    @Override
    public IgnoreNameDTO findOne(Long id) {
        return ignoreNameMapper.toDto(repository.findOne(id));
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public List<IgnoreNameDTO> findAll() {
        return repository.findAll().stream().map(ignoreNameMapper::toDto).collect(Collectors.toList());
    }
}

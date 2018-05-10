package vn.com.la.service.mapper;

import org.mapstruct.Mapper;
import vn.com.la.domain.IgnoreName;
import vn.com.la.service.dto.IgnoreNameDTO;

@Mapper(componentModel = "spring", uses = {})
public interface IgnoreNameMapper extends EntityMapper <IgnoreNameDTO, IgnoreName> {

    default IgnoreName fromId(Long id) {
        if (id == null) {
            return null;
        }
        IgnoreName ignoreName = new IgnoreName();
        ignoreName.setId(id);
        return ignoreName;
    }
}

package vn.com.la.repository;

import vn.com.la.domain.UserSetting;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the UserSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long>, JpaSpecificationExecutor<UserSetting> {

    @Query("select user_setting from UserSetting user_setting where user_setting.userConfig.login = ?#{principal.username}")
    List<UserSetting> findByUserConfigIsCurrentUser();

}

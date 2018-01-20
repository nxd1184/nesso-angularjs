package vn.com.la.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.Constants;
import vn.com.la.repository.SequenceDataDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

@Repository
@Transactional(readOnly = true)
public class SequenceDataDaoImpl implements SequenceDataDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional(readOnly = false)
    public Long nextVal() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT nextval('" + Constants.NESSO_GLOBAL_SEQUENCE + "')")
            .getSingleResult()).longValue();

    }

    @Override
    @Transactional(readOnly = false)
    public Long nextJobTeamUserTaskId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT nextval('SQ_JOB_TEAM_USER_TASK_ID')")
            .getSingleResult()).longValue();

    }

}

package org.example.mhsrepository.repository.job;

import org.example.mhsrepository.repository.AbsRepository;
import org.example.mhscommons.data.tables.pojos.Job;
import org.example.mhscommons.data.tables.records.JobRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static org.example.mhscommons.data.Tables.JOB;

@Repository
public class JobRepositoryImpl extends AbsRepository<JobRecord, Job,Integer> implements IJobRepository {
    @Override
    protected TableImpl<JobRecord> getTable() {
        return JOB;
    }
}

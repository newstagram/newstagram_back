package com.newstagram.rss.repository;

import com.newstagram.rss.entity.NewsBatchLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsBatchLogRepository extends JpaRepository<NewsBatchLog,Long> {
}

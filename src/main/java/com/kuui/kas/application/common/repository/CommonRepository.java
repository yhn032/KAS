package com.kuui.kas.application.common.repository;

import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CommonRepository {
    private final EntityManager em;

    public void save(Teacher teacher) {em.persist(teacher);}
}

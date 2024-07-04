package com.kuui.kas.application.teacher.repository;

import com.kuui.kas.application.teacher.domain.QTeacher;
import com.kuui.kas.application.teacher.domain.Teacher;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.kuui.kas.application.teacher.domain.QTeacher.teacher;

@Repository
@RequiredArgsConstructor
public class TeacherRepositoryImpl implements TeacherRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    @Override
    public Teacher findByEmail(String email) {
        return queryFactory
                .selectFrom(teacher)
                .where(teacher.teacherEmailAddress.eq(email))
                .fetchOne();
    }

    @Override
    public Teacher findByLoginId(String id) {
        return queryFactory
                .selectFrom(teacher)
                .where(teacher.teacherLogInID.eq(id))
                .fetchOne();
    }

    @Override
    public Teacher findByTeacherNickName(String name) {
        return queryFactory
                .selectFrom(teacher)
                .where(teacher.teacherNickname.eq(name))
                .fetchOne();
    }

    @Override
    public List<Teacher> findAll() {
        return queryFactory
                .selectFrom(teacher)
                .fetch();
    }

    @Override
    public List<Teacher> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Teacher> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Teacher> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Teacher entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Teacher> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Teacher save(Teacher teacher) {
        if(teacher != null){
            em.persist(teacher);
            return  teacher;
        }else {
            throw new NullPointerException("Entity must Not be Null");
        }
    }

    @Override
    public <S extends Teacher> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Teacher> findById(Long teacherId) {
        Teacher teacher = queryFactory
                .selectFrom(QTeacher.teacher)
                .where(QTeacher.teacher.teacherId.eq(teacherId))
                .fetchOne();

        return Optional.of(teacher);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Teacher> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Teacher> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Teacher> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Teacher getOne(Long aLong) {
        return null;
    }

    @Override
    public Teacher getById(Long aLong) {
        return null;
    }

    @Override
    public Teacher getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Teacher> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Teacher> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Teacher> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Teacher> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Teacher> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Teacher> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Teacher, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}

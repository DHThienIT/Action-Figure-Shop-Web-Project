package com.baitapnhom.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.baitapnhom.entity.SanPham;


public interface SanPhamService {

    <S extends SanPham> List<S> findAll(Example<S> example, Sort sort);

    <S extends SanPham> List<S> findAll(Example<S> example);

    SanPham getReferenceById(Long id);

    SanPham getById(Long id);

    void deleteAll();

    void deleteAll(Iterable<? extends SanPham> entities);

    SanPham getOne(Long id);

    void deleteAllInBatch();

    void deleteAllById(Iterable<? extends Long> ids);

    <S extends SanPham, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

    void delete(SanPham entity);

    void deleteAllByIdInBatch(Iterable<Long> ids);

    void deleteById(Long id);

    <S extends SanPham> boolean exists(Example<S> example);

    long count();

    void deleteAllInBatch(Iterable<SanPham> entities);

    <S extends SanPham> long count(Example<S> example);

    void deleteInBatch(Iterable<SanPham> entities);

    <S extends SanPham> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends SanPham> List<S> saveAllAndFlush(Iterable<S> entities);

    <S extends SanPham> S saveAndFlush(S entity);

    boolean existsById(Long id);

    void flush();

    <S extends SanPham> List<S> saveAll(Iterable<S> entities);

    Optional<SanPham> findById(Long id);

    List<SanPham> findAllById(Iterable<Long> ids);

    List<SanPham> findAll(Sort sort);

    Page<SanPham> findAll(Pageable pageable);

    List<SanPham> findAll();

    <S extends SanPham> Optional<S> findOne(Example<S> example);

    <S extends SanPham> S save(S entity);
  



}

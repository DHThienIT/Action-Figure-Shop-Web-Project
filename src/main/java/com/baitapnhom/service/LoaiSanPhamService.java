package com.baitapnhom.service;


import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.baitapnhom.entity.LoaiSanPham;


public interface LoaiSanPhamService {

    <S extends LoaiSanPham> List<S> findAll(Example<S> example, Sort sort);

    <S extends LoaiSanPham> List<S> findAll(Example<S> example);

    LoaiSanPham getReferenceById(Integer id);

    LoaiSanPham getById(Integer id);

    void deleteAll();

    void deleteAll(Iterable<? extends LoaiSanPham> entities);

    LoaiSanPham getOne(Integer id);

    void deleteAllInBatch();

    void deleteAllById(Iterable<? extends Integer> ids);

    <S extends LoaiSanPham, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

    void delete(LoaiSanPham entity);

    void deleteAllByIdInBatch(Iterable<Integer> ids);

    void deleteById(Integer id);

    <S extends LoaiSanPham> boolean exists(Example<S> example);

    long count();

    void deleteAllInBatch(Iterable<LoaiSanPham> entities);

    <S extends LoaiSanPham> long count(Example<S> example);

    void deleteInBatch(Iterable<LoaiSanPham> entities);

    <S extends LoaiSanPham> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends LoaiSanPham> List<S> saveAllAndFlush(Iterable<S> entities);

    <S extends LoaiSanPham> S saveAndFlush(S entity);

    boolean existsById(Integer id);

    void flush();

    <S extends LoaiSanPham> List<S> saveAll(Iterable<S> entities);

    Optional<LoaiSanPham> findById(Integer id);

    List<LoaiSanPham> findAllById(Iterable<Integer> ids);

    List<LoaiSanPham> findAll(Sort sort);

    Page<LoaiSanPham> findAll(Pageable pageable);

    List<LoaiSanPham> findAll();

    <S extends LoaiSanPham> Optional<S> findOne(Example<S> example);

    <S extends LoaiSanPham> S save(S entity);

    LoaiSanPham findByName(String name);




}

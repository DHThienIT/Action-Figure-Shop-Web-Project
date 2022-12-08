package com.baitapnhom.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.SanPham;
import com.baitapnhom.renpository.SanPhamRepository;
import com.baitapnhom.service.SanPhamService;
@Service
public class SanPhamServiceIpml implements SanPhamService{

    @Autowired
    SanPhamRepository sanPhamRepository;

    public SanPhamServiceIpml(SanPhamRepository sanPhamRepository) {
        super();
        this.sanPhamRepository = sanPhamRepository;
    }

    public SanPhamServiceIpml() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public <S extends SanPham> S save(S entity) {
        return sanPhamRepository.save(entity);
    }

    @Override
    public <S extends SanPham> Optional<S> findOne(Example<S> example) {
        return sanPhamRepository.findOne(example);
    }

    @Override
    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }

    @Override
    public Page<SanPham> findAll(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    @Override
    public List<SanPham> findAll(Sort sort) {
        return sanPhamRepository.findAll(sort);
    }

    @Override
    public List<SanPham> findAllById(Iterable<Long> ids) {
        return sanPhamRepository.findAllById(ids);
    }

    @Override
    public Optional<SanPham> findById(Long id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    public <S extends SanPham> List<S> saveAll(Iterable<S> entities) {
        return sanPhamRepository.saveAll(entities);
    }

    @Override
    public void flush() {
        sanPhamRepository.flush();
    }

    @Override
    public boolean existsById(Long id) {
        return sanPhamRepository.existsById(id);
    }

    @Override
    public <S extends SanPham> S saveAndFlush(S entity) {
        return sanPhamRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends SanPham> List<S> saveAllAndFlush(Iterable<S> entities) {
        return sanPhamRepository.saveAllAndFlush(entities);
    }

    @Override
    public <S extends SanPham> Page<S> findAll(Example<S> example, Pageable pageable) {
        return sanPhamRepository.findAll(example, pageable);
    }

    @Override
    public void deleteInBatch(Iterable<SanPham> entities) {
        sanPhamRepository.deleteInBatch(entities);
    }

    @Override
    public <S extends SanPham> long count(Example<S> example) {
        return sanPhamRepository.count(example);
    }

    @Override
    public void deleteAllInBatch(Iterable<SanPham> entities) {
        sanPhamRepository.deleteAllInBatch(entities);
    }

    @Override
    public long count() {
        return sanPhamRepository.count();
    }

    @Override
    public <S extends SanPham> boolean exists(Example<S> example) {
        return sanPhamRepository.exists(example);
    }

    @Override
    public void deleteById(Long id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        sanPhamRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void delete(SanPham entity) {
        sanPhamRepository.delete(entity);
    }

    @Override
    public <S extends SanPham, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return sanPhamRepository.findBy(example, queryFunction);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        sanPhamRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
        sanPhamRepository.deleteAllInBatch();
    }

    @Override
    public SanPham getOne(Long id) {
        return sanPhamRepository.getOne(id);
    }

    @Override
    public void deleteAll(Iterable<? extends SanPham> entities) {
        sanPhamRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        sanPhamRepository.deleteAll();
    }

    @Override
    public SanPham getById(Long id) {
        return sanPhamRepository.getById(id);
    }

    @Override
    public SanPham getReferenceById(Long id) {
        return sanPhamRepository.getReferenceById(id);
    }

    @Override
    public <S extends SanPham> List<S> findAll(Example<S> example) {
        return sanPhamRepository.findAll(example);
    }

    @Override
    public <S extends SanPham> List<S> findAll(Example<S> example, Sort sort) {
        return sanPhamRepository.findAll(example, sort);
    }
    
    
 
    
}

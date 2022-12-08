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

import com.baitapnhom.entity.LoaiSanPham;
import com.baitapnhom.renpository.LoaiSanPhamRepository;
import com.baitapnhom.service.LoaiSanPhamService;
@Service
public class LoaiSanPhamServiceImpl  implements LoaiSanPhamService{
    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    
    
    public LoaiSanPhamServiceImpl(LoaiSanPhamRepository loaiSanPhamRepository) {
        super();
        this.loaiSanPhamRepository = loaiSanPhamRepository;
    }

    public LoaiSanPhamServiceImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public LoaiSanPham findByName(String name) {
        return loaiSanPhamRepository.findByName(name);
    }

    @Override
    public <S extends LoaiSanPham> S save(S entity) {
        return loaiSanPhamRepository.save(entity);
    }

    @Override
    public <S extends LoaiSanPham> Optional<S> findOne(Example<S> example) {
        return loaiSanPhamRepository.findOne(example);
    }

    @Override
    public List<LoaiSanPham> findAll() {
        return loaiSanPhamRepository.findAll();
    }

    @Override
    public Page<LoaiSanPham> findAll(Pageable pageable) {
        return loaiSanPhamRepository.findAll(pageable);
    }

    @Override
    public List<LoaiSanPham> findAll(Sort sort) {
        return loaiSanPhamRepository.findAll(sort);
    }

    @Override
    public List<LoaiSanPham> findAllById(Iterable<Integer> ids) {
        return loaiSanPhamRepository.findAllById(ids);
    }

    @Override
    public Optional<LoaiSanPham> findById(Integer id) {
        return loaiSanPhamRepository.findById(id);
    }

    @Override
    public <S extends LoaiSanPham> List<S> saveAll(Iterable<S> entities) {
        return loaiSanPhamRepository.saveAll(entities);
    }

    @Override
    public void flush() {
        loaiSanPhamRepository.flush();
    }

    @Override
    public boolean existsById(Integer id) {
        return loaiSanPhamRepository.existsById(id);
    }

    @Override
    public <S extends LoaiSanPham> S saveAndFlush(S entity) {
        return loaiSanPhamRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends LoaiSanPham> List<S> saveAllAndFlush(Iterable<S> entities) {
        return loaiSanPhamRepository.saveAllAndFlush(entities);
    }

    @Override
    public <S extends LoaiSanPham> Page<S> findAll(Example<S> example, Pageable pageable) {
        return loaiSanPhamRepository.findAll(example, pageable);
    }

    @Override
    public void deleteInBatch(Iterable<LoaiSanPham> entities) {
        loaiSanPhamRepository.deleteInBatch(entities);
    }

    @Override
    public <S extends LoaiSanPham> long count(Example<S> example) {
        return loaiSanPhamRepository.count(example);
    }

    @Override
    public void deleteAllInBatch(Iterable<LoaiSanPham> entities) {
        loaiSanPhamRepository.deleteAllInBatch(entities);
    }

    @Override
    public long count() {
        return loaiSanPhamRepository.count();
    }

    @Override
    public <S extends LoaiSanPham> boolean exists(Example<S> example) {
        return loaiSanPhamRepository.exists(example);
    }

    @Override
    public void deleteById(Integer id) {
        loaiSanPhamRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> ids) {
        loaiSanPhamRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void delete(LoaiSanPham entity) {
        loaiSanPhamRepository.delete(entity);
    }

    @Override
    public <S extends LoaiSanPham, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return loaiSanPhamRepository.findBy(example, queryFunction);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        loaiSanPhamRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
        loaiSanPhamRepository.deleteAllInBatch();
    }

    @Override
    public LoaiSanPham getOne(Integer id) {
        return loaiSanPhamRepository.getOne(id);
    }

    @Override
    public void deleteAll(Iterable<? extends LoaiSanPham> entities) {
        loaiSanPhamRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        loaiSanPhamRepository.deleteAll();
    }

    @Override
    public LoaiSanPham getById(Integer id) {
        return loaiSanPhamRepository.getById(id);
    }

    @Override
    public LoaiSanPham getReferenceById(Integer id) {
        return loaiSanPhamRepository.getReferenceById(id);
    }

    @Override
    public <S extends LoaiSanPham> List<S> findAll(Example<S> example) {
        return loaiSanPhamRepository.findAll(example);
    }

    @Override
    public <S extends LoaiSanPham> List<S> findAll(Example<S> example, Sort sort) {
        return loaiSanPhamRepository.findAll(example, sort);
    }
    
    

    
    
}

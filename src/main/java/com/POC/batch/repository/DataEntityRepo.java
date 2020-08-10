package com.POC.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.POC.batch.model.DataEntity;

@Service
public interface DataEntityRepo extends JpaRepository<DataEntity, Long>{

}

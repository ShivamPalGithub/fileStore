package com.demo.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.FileData;
import com.demo.entity.ImageData;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Integer> {
    Optional<FileData> findByName(String fileName);
}

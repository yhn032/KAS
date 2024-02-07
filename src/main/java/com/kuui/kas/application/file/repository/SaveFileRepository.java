package com.kuui.kas.application.file.repository;

import com.kuui.kas.application.file.domain.SaveFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveFileRepository extends JpaRepository<SaveFile, Long> {
    public SaveFile findBySaveName(String saveName);
}

package com.simplon.sondagesw.dao.impl;

import com.simplon.sondagesw.entity.Sondages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SondagesRepository extends JpaRepository<Sondages, Long> {
}

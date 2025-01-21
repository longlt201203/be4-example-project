package com.be4.qltc.modules.database.repositories;

import com.be4.qltc.modules.database.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
}

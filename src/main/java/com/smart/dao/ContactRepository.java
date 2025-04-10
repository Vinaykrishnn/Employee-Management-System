package com.smart.dao;

import com.smart.entities.Contacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contacts, Integer> {

    @Query("from Contacts as c where c.user.id =:userId")
    public Page<Contacts> findContactByUserId(@Param("userId") Long userId, Pageable pageable);
}

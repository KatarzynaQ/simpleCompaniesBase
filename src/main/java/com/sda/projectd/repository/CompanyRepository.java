package com.sda.projectd.repository;

import com.sda.projectd.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Collection<Company> findByNames(String name);
}

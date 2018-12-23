package com.sda.projectd.service;

import com.sda.projectd.model.Company;
import com.sda.projectd.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void updateCompany(Long id, Company company) throws CompanyDoesntExistException {
        findById(id).map(oldCompany -> {
            oldCompany.setCurrentName(company.getCurrentName());
            oldCompany.setAddress(company.getAddress());
            if (company.getKrs() != null) {
                oldCompany.setKrs(company.getKrs());
            }
            oldCompany.setNip(company.getNip());
            oldCompany.setRegon(company.getRegon());
            oldCompany.setAddress(company.getAddress());
            if (company.getFiles() != null) {
                oldCompany.setFiles(company.getFiles());
            }
            companyRepository.save(oldCompany);
            return oldCompany;
        }).orElseThrow(() -> new CompanyDoesntExistException("Nie ma takiej firmy"));
    }

    @Override
    public Company addCompany(Company company) throws CompanyAlreadyExistsException {
        if (company.getId() != null) {
            throw new CompanyAlreadyExistsException("Firma już istnieje");
        } else
            return companyRepository.
                    save(company);
    }

    @Override
    public Collection<Company> findByName(String name) {
        return companyRepository.findByNames(name);
    }

    @Override
    public Optional<Company> findById(Long companyId) {
        return companyRepository.findById(companyId);
    }

}

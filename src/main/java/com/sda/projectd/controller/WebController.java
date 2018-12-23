package com.sda.projectd.controller;

import com.sda.projectd.model.Company;
import com.sda.projectd.service.CompanyService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.util.Collection;

@Controller
public class WebController {
    private CompanyService companyService;

    public WebController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping(value = "/add")
    ModelAndView getAddForm(@RequestParam(value = "companyId", required = false) Long companyId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add");
        Company company = new Company();
        if (companyId != null) {
            company = companyService.findById(companyId).orElse(company);
        }
        modelAndView.addObject("company",
                company);
        return modelAndView;
    }
    @PostMapping(value = "/add")
    void addCompany(@ModelAttribute("company") Company company) throws Exception {
        if (company.getId() == null) {
                companyService.addCompany(company);
        } else
            companyService.updateCompany(company.getId(), company);
    }


    @GetMapping(value = "/companies")
    ModelAndView getCompanies(@RequestParam(name = "nameToFind", required = false) String name) {
        ModelAndView modelAndView = new ModelAndView();
        Collection<Company> companies = companyService.findByName(name);
        modelAndView.addObject("findCompanies", companies);
        modelAndView.setViewName("companies");
        return modelAndView;
    }
}
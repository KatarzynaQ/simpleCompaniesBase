package com.sda.projectd;

import com.sda.projectd.model.Address;
import com.sda.projectd.model.Company;
import com.sda.projectd.repository.CompanyRepository;
import com.sda.projectd.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringJUnitConfig
public class CompanyServiceTest {

    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void beforeEach() {
        //this.companyService = new CompanyServiceInMemoryImpl();
        this.companyService = new CompanyServiceImpl(companyRepository);
    }

    @DisplayName("should add a new company with all properties")
    @Test
    void test0() throws Exception {
        // given
        Company command = createCompanyWithAllProperties();

        // when
        companyService.addCompany(command);

        // then
        assertThat(companyService.findByName(command.getCurrentName()).size()).isEqualTo(1);
    }

    @DisplayName("should load a single company by name")
    @Test
    void test1() throws Exception {
        // given
        String name = "Company S.A.";
        Company companyToFind = createCompanyWithName(name);
        companyService.addCompany(createCompanyWithName("ACME"));
        companyService.addCompany(createCompanyWithName(name));
        companyService.addCompany(companyToFind);
        companyService.addCompany(createCompanyWithName("JetBrains"));

        // when
        Collection<Company> foundCompany = companyService.findByName(name);

        //then
        assertThat(foundCompany.size()).isEqualTo(2);
    }

    @DisplayName("should load two companies with the same name by name")
    @Test
    void test2() throws Exception {
        //given
        String name = "Company";
        companyService.addCompany(createCompanyWithName(name));
        companyService.addCompany(createCompanyWithName(name));
        companyService.addCompany(createCompanyWithName("different name"));
        //when
        Collection<Company> byName = companyService.findByName(name);
        //then
        assertThat(byName.size()).isEqualTo(2);
    }

    @DisplayName("should update names history when change current name in the existing company")
    @Test
    void test() throws Exception {
        // given
        String oldName = "old name";
        Company company = new Company();
        company.setCurrentName(oldName);
        company = companyService.addCompany(company);

        // when
        String newName = "new name";
        company.setCurrentName(newName);
        companyService.updateCompany(company.getId(), company);

        // then
        assertThat(companyService.findById(company.getId()).get()).isEqualTo(company);
    }

    @DisplayName("should actualize size of name's list when new name is added")
    @Test
    void test3() throws Exception {
        Company company = createCompanyWithTwoNames();
        assertThat(company.getNames().size()).isEqualTo(2);
        companyService.addCompany(company);
        //when
        Collection<Company> byName = companyService.findByName("Company sp. z o.o.");
        //than
        assertThat(byName).contains(company);
        List<Company> byNameList = new ArrayList(byName);
        assertThat(byNameList.get(0).getNames().size()).isEqualTo(2);
    }

    @DisplayName("should find by old name")
    @Test
    void test4() throws Exception {
        //given
        Company company = createCompanyWithTwoNames();
        companyService.addCompany(company);
        //when
        Collection<Company> byName = companyService.findByName("Nowa nazwa Firmy");
        assertThat(byName.size()).isEqualTo(1);
    }

    @DisplayName("should throw Excepion when try to add two companies with the same id")
    @Test
    void test5() throws CompanyAlreadyExistsException {
        Company company = companyService.addCompany(createCompanyWithAllProperties());
        Company companyWithTheSameId = new Company();
        companyWithTheSameId.setId(company.getId());

        // when
        Throwable e = catchThrowable(() -> companyService.addCompany(company));

        // then
        assertThat(e).isInstanceOf(CompanyAlreadyExistsException.class);
    }

    @DisplayName("should throw CompanyDoesntExistsException when companyId to find doesnt exist in base")
    @Test
    void test6() throws Exception {
        //given
        Company company = new Company();

        //when
        Throwable e = catchThrowable(() -> companyService.updateCompany(-2L, company));
        //than
        assertThat(e).isInstanceOf(CompanyDoesntExistException.class);
    }

    @DisplayName("should not add new company when update")
    @Test
    void test7() throws Exception {
        //given
        Company company = companyService.addCompany(createCompanyWithName("old"));

        //when
        companyService.updateCompany(company.getId(), createCompanyWithName("changeName"));

        //then
        Collection<Company> companiesWithNewName = companyService.findByName("changeName");
        Collection<Company> companiesWithOldName = companyService.findByName("old");
        assertThat(companiesWithNewName).hasSize(1).containsOnlyElementsOf(companiesWithOldName);
    }

	private Company createCompanyWithTwoNames() {
        Company company = createCompanyWithAllProperties();
        company.setCurrentName("Nowa nazwa Firmy");
        return company;
    }

    private Company createCompanyWithAllProperties() {
        Company company = new Company();
        company.setCurrentName("Company sp. z o.o.");
        company.setKrs("1234567891");
        company.setNip("1234567891");
        company.setRegon("1234567891");
        company.setAddress(new Address("Ulica", "1", "1", "11-111", "X"));
        return company;
    }

    private Company createCompanyWithName(String name) {
        Company company = new Company();
        company.setCurrentName(name);
        company.setKrs("1234567891");
        company.setNip("1234567891");
        company.setRegon("1234567891");
        return company;
    }
}
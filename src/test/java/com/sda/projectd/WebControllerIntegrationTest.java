package com.sda.projectd;

import com.sda.projectd.model.Address;
import com.sda.projectd.model.Company;
import com.sda.projectd.service.CompanyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collection;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest
public class WebControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @DisplayName("should add new Company when POST is called on /add")
    @Test
    void test1() throws Exception {
        Company company = createCompany();
        // when
        mockMvc.perform(postWithCompanyWithoutId(company, "/add"))
                //then
                .andExpect(status().isOk());
        verify(companyService, times(1)).addCompany(company);
    }

    @DisplayName("should call add company when id is not given")
    @Test
    void test21() throws Exception {
        //given
        Company company = createCompany();
        // when
        mockMvc.perform(postWithCompanyWithoutId(company, "/add"));
        verify(companyService, times(1)).addCompany(company);
    }

    @DisplayName("should call update company when id is given")
    @Test
    void test22() throws Exception {
        //given
        Company company = createCompany();
        company.setId(2L);
        //when
        mockMvc.perform(postWithCompanyId(company, "/add", "2"));
        verify(companyService, times(1)).updateCompany(2L, company);
    }

    @DisplayName("should load findBy name companies")
    @Test
    void test3() throws Exception {
        // given
        Company fakeCompany = new Company();
        fakeCompany.setCurrentName("C S.A.");
        fakeCompany.setAddress(new Address("Uliczna", "2", "1", "11-111", "Miasto"));
        fakeCompany.setKrs("11111111111");
        fakeCompany.setNip("222222222");
        fakeCompany.setRegon("333333333");
        Collection<Company> expectedCompanies = Arrays.asList(fakeCompany);
        when(companyService.findByName("C S.A.")).thenReturn(expectedCompanies);

        // mock mvc...
        mockMvc.perform(get("/companies").param("nameToFind", "C S.A."))
                // then
                .andExpect(status().isOk())
                .andExpect(model().attribute("findCompanies", expectedCompanies));
        verify(companyService, times(1)).findByName("C S.A.");
    }

    @DisplayName("should change currentName and save old name in names")
    @Test
    void test4() throws Exception {
        // given
        Collection<Company> companies = Arrays.asList(new Company());
        String nameToFindParamValue = "Name to change";
        when(companyService.findByName(nameToFindParamValue)).thenReturn(companies);

        // when
        mockMvc.perform(get("/companies")
                .param("nameToFind", nameToFindParamValue))

                // then
                .andExpect((model()
                        .attribute("findCompanies", companies)));
    }

    private Company createCompany() {
        Company company = new Company();
        company.setCurrentName("Company S.A.");
        company.setAddress(new Address("Kwiatowa", "1", "2", "00-111", "Miasto"));
        company.setKrs("1111111111");
        company.setNip("111111111");
        company.setRegon("111111111");
        return company;
    }

    private RequestBuilder postWithCompanyWithoutIdAndWithFile(Company company, MockMultipartFile file, String path) {
        return multipart(path).file(file).params(convertCompanyToParams(company));
    }

    private RequestBuilder postWithCompanyWithoutId(Company company, String path) {
        return post(path)
                .params(convertCompanyToParams(company));
    }

    private MultiValueMap<String, String> convertCompanyToParams(Company company) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("currentName", company.getCurrentName());
        params.add("address.street", company.getAddress().getStreet());
        params.add("address.houseNumber", company.getAddress().getHouseNumber());
        params.add("address.flatNumber", company.getAddress().getFlatNumber());
        params.add("address.postalCode", company.getAddress().getPostalCode());
        params.add("address.city", company.getAddress().getCity());
        params.add("krs", company.getKrs());
        params.add("nip", company.getNip());
        params.add("regon", company.getRegon());
        return params;
    }

    private RequestBuilder postWithCompanyId(Company company, String s, String id) {
        return post(s).param("currentName", company.getCurrentName())
                .param("address.street", company.getAddress().getStreet())
                .param("address.houseNumber", company.getAddress().getHouseNumber())
                .param("address.flatNumber", company.getAddress().getFlatNumber())
                .param("address.postalCode", company.getAddress().getPostalCode())
                .param("address.city", company.getAddress().getCity())
                .param("krs", company.getKrs())
                .param("nip", company.getNip())
                .param("regon", company.getRegon())
                .param("id", id);
    }
}

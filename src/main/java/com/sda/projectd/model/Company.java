package com.sda.projectd.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> names;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;
    private String currentName;
    private String krs;
    private String nip;
    private String regon;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> files;

    public void setId(Long id) {
        this.id = id;
    }

    public Company() {
        this.names=new HashSet<>();
        address = new Address();
        files=new HashSet<>();
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
        names.add(currentName);
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setKrs(String krs) {
        this.krs = krs;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public Long getId() {
        return id;
    }

    public Set<String> getNames() {
        return names;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNip() {
        return nip;
    }

    public String getKrs() {
        return krs;
    }

    public String getRegon() {
        return regon;
    }

    public String getCurrentName() {
        return currentName;
    }

    public Set<String> getFiles() {
        return files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) &&
                Objects.equals(names, company.names) &&
                Objects.equals(address, company.address) &&
                Objects.equals(currentName, company.currentName) &&
                Objects.equals(krs, company.krs) &&
                Objects.equals(nip, company.nip) &&
                Objects.equals(regon, company.regon) &&
                Objects.equals(files, company.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, names, address, currentName, krs, nip, regon, files);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", names=" + names +
                ", address=" + address +
                ", currentName='" + currentName + '\'' +
                ", krs='" + krs + '\'' +
                ", nip='" + nip + '\'' +
                ", regon='" + regon + '\'' +
                ", files=" + files +
                '}';
    }
}

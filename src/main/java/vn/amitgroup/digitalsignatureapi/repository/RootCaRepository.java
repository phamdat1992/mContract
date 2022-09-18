package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.amitgroup.digitalsignatureapi.entity.RootCa;

public interface RootCaRepository extends JpaRepository<RootCa,Integer> {
    RootCa findByName(String name);
}

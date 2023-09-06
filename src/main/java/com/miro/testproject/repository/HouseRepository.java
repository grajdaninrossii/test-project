package com.miro.testproject.repository;

import com.miro.testproject.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {


    Optional<House> findById(Long id);

    Boolean existsByAddress(String address);

}

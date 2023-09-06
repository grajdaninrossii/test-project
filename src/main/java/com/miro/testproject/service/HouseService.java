package com.miro.testproject.service;

import com.miro.testproject.entity.House;
import com.miro.testproject.entity.User;
import com.miro.testproject.pojo.HouseTemplate;
import com.miro.testproject.repository.HouseRepository;
import com.miro.testproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HouseService {

    private final UserRepository userRepository;

    private final HouseRepository houseRepository;

    private final AuthService auth;

    public List<House> getAllHouse(){
        return houseRepository.findAll();
    }

    public List<String>  deleteHouse(Long id){
        User user = auth.getAuthUser();
        House house = houseRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, House is not found!"));
        if (user.getId().equals(house.getHost().getId())){
            houseRepository.delete(house);
            return Arrays.asList("good", "House successfully deleted");
        }
        return Arrays.asList("bad", "Error: You are not host!");
    }


    public List<String> addHouse(HouseTemplate houseTemplate){
        User user = auth.getAuthUser();

        if (houseRepository.existsByAddress(houseTemplate.getAddress())){
            return  Arrays.asList("bad", "Error: House with current address is exists!");
        }
        House house;
        if (houseTemplate.getResidents() == null) {
            house = new House(
                    houseTemplate.getAddress(),
                    userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("Error, House is not found!"))
            );
        } else {
            house = new House(
                    houseTemplate.getAddress(),
                    userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("Error, House is not found!")),
                    houseTemplate.getResidents().stream().map(id -> userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, Resident is not found!"))).collect(Collectors.toSet())
            );
        }


        houseRepository.save(house);
        return Arrays.asList("good", "House added!");
    }


    public List<String> deleteResident(Long id_house, Long id_resident){
        User user = auth.getAuthUser();
        House house = houseRepository.findById(id_house).orElseThrow(() -> new RuntimeException("Error, House is not found!"));
        if (user.getId().equals(house.getHost().getId())){
            User resident = userRepository.findById(id_resident).orElseThrow(() -> new RuntimeException("Error, Resident is not found!"));
            Boolean result = house.deleteResidents(resident);
            houseRepository.save(house);
            return Arrays.asList("good", "Resident successfully deleted");
        }

        return Arrays.asList("bad", "Error: You are not host!");
    }


    public List<String> addResident(Long id_house, Long id_resident){
        User user = auth.getAuthUser();
        House house = houseRepository.findById(id_house).orElseThrow(() -> new RuntimeException("Error, House is not found!"));
        if (user.getId().equals(house.getHost().getId())){
            // Не успеваю сделать обработку result(((
            Boolean result = house.addResidents(userRepository.findById(id_resident).orElseThrow(() -> new RuntimeException("Error, Resident is not found!")));
            houseRepository.save(house);
            return Arrays.asList("good", "Resident successfully added");
        }
        return Arrays.asList("bad", "Error: You are not host!");
    }

}

package com.miro.testproject.controller;

import com.miro.testproject.pojo.HouseTemplate;
import com.miro.testproject.pojo.MessageResponse;
import com.miro.testproject.pojo.SignupRequest;
import com.miro.testproject.service.HouseService;
import com.miro.testproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/house")
@CrossOrigin(origins = "*", maxAge = 3600) // Работа с CORS
@RequiredArgsConstructor
public class HouseController {


    private final HouseService houseService;


    /** Удалить дом из бд
     *
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteHouse(@RequestParam Long id_house){
        List<String> result = houseService.deleteHouse(id_house);
        if (result.get(0).equals("bad")){
            return ResponseEntity.badRequest().body(result.get(1));
        }
        return ResponseEntity.ok(new MessageResponse(result.get(1)));
    }


    /** Добавить дом в бд
     *
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addHouse(@RequestBody HouseTemplate house){

        List<String> result = houseService.addHouse(house);
        if (result.get(0).equals("bad")){
            return ResponseEntity.badRequest().body(result.get(1));
        }
        return ResponseEntity.ok(new MessageResponse(result.get(1)));
    }


    /** Добавить проживающего в дом в бд
     *
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @PostMapping("/add_resident")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addResident(@RequestParam Long id_house, @RequestParam Long id_resident){

        List<String> result = houseService.addResident(id_house, id_resident);
        if (result.get(0).equals("bad")){
            return ResponseEntity.badRequest().body(result.get(1));
        }
        return ResponseEntity.ok(new MessageResponse(result.get(1)));
    }


    /** Удалить проживающего в доме в бд
     *
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @DeleteMapping("/delete_resident")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteResident(@RequestParam Long id_house, @RequestParam Long id_resident){

        List<String> result = houseService.deleteResident(id_house, id_resident);
        if (result.get(0).equals("bad")){
            return ResponseEntity.badRequest().body(result.get(1));
        }
        return ResponseEntity.ok(new MessageResponse(result.get(1)));
    }
}

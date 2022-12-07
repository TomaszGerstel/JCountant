package com.tgerstel.controller;

import com.tgerstel.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tgerstel.repository.UserRepository;

@RestController
@RequestMapping(path="/home", produces="application/json")
@CrossOrigin(origins="*")
public class HomeController {
	
	


//	@Autowired
//	EntityLinks entityLinks; // HATEOS

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.
            getLogger(HomeController.class);
    
    private final UserRepository userRepo;

//    @Autowired
    public HomeController(UserRepository userRepo) {
    	this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<?> users() {
//        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending())
//        return new ResponseEntity<>("test kurła", HttpStatus.OK);
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
    }
}


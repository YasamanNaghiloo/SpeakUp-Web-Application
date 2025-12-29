package com.example.speak_up.business_logic.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiController {
    public final Long MAXPETITIONSPERUSER = 5L;

    @GetMapping("/petitionlimit")
    public Long one() {
        return MAXPETITIONSPERUSER;
    }
}

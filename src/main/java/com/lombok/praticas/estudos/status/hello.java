package com.lombok.praticas.estudos.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("status")
public class hello {
    
    @GetMapping("/hello")
    public String helloSpringBoot(){
        return "Hello spring-boot starter";
    }
}

package com.example.demo.controller;

import com.example.demo.annotation.ApiVersion;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiVersion(1)
@RequestMapping("/users")
public class UserController {

    @ApiVersion({4, 2})
    @GetMapping
    public String getUsers() {
        return "User list for v1 or v2";
    }

    @ApiVersion(value = {1}, strategy = ApiVersion.Strategy.HEADER)
    @GetMapping
    public String getUsersV3() {
        return "User list for v3";
    }

    @GetMapping("/test")
    public String getUsersV4() {
        return "User list for v3";
    }
}

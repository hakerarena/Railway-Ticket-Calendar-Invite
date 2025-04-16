package com.hakerarena.railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hakerarena.railway.service.InviteService;

@RestController
@RequestMapping("/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @PostMapping(value = "/generate/", consumes = "multipart/form-data")
    public String generateInvite(@RequestPart("file") MultipartFile file, @RequestParam("email") String email) {
        try {
            return inviteService.generateInvite(file, email);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
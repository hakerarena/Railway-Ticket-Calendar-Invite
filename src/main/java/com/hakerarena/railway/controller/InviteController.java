package com.hakerarena.railway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hakerarena.railway.model.SuccessResponse;
import com.hakerarena.railway.service.InviteService;

@RestController
@RequestMapping("/invite")
public class InviteController {

    private final InviteService inviteService;

    @Autowired
    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping(value = "/generate/", consumes = "multipart/form-data")
    public SuccessResponse generateInvite(@RequestPart("file") MultipartFile file,
            @RequestParam("email") String email) {
        return inviteService.generateInvite(file, email);
    }
}
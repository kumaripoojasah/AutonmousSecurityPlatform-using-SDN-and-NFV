package com.example.demo.controller;



import com.example.demo.dto.attack.AttackResponse;
import com.example.demo.service.AttackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attacks")
@RequiredArgsConstructor
public class AttackController {

    private final AttackService attackService;

    @GetMapping
    public List<AttackResponse> getRecentAttacks() {
        return attackService.getRecentAttacks();
    }

    @GetMapping("/{id}")
    public AttackResponse getAttack(@PathVariable Long id) {
        return attackService.getAttackById(id);
    }
}
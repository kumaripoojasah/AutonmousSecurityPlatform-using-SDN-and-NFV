package com.example.demo.controller;



import com.example.demo.dto.mitigation.MitigationActionResponse;
import com.example.demo.service.MitigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mitigations")
@RequiredArgsConstructor
public class MitigationController {

    private final MitigationService mitigationService;

    @GetMapping
    public List<MitigationActionResponse> getRecentActions() {
        return mitigationService.getRecentActions();
    }

    @GetMapping("/{id}")
    public MitigationActionResponse getAction(
            @PathVariable Long id) {

        return mitigationService.getActionById(id);
    }
}
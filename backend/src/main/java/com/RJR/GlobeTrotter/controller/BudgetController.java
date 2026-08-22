package com.RJR.GlobeTrotter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RJR.GlobeTrotter.dto.response.BudgetResponse;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.BudgetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<BudgetResponse> getTripBudget(Authentication authentication, @PathVariable Long tripId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(budgetService.getTripBudget(userId, tripId));
    }
}

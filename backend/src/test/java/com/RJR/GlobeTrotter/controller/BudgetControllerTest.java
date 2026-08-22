package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.RJR.GlobeTrotter.dto.response.BudgetResponse;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.BudgetService;

class BudgetControllerTest {

    @Test
    void getTripBudgetReturnsOkResponseForCurrentUser() {
        BudgetService service = mock(BudgetService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        BudgetController controller = new BudgetController(service, users);
        BudgetResponse expected = BudgetResponse.builder().tripId(10L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getTripBudget(5L, 10L)).thenReturn(expected);

        ResponseEntity<BudgetResponse> response = controller.getTripBudget(authentication, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getTripBudget(5L, 10L);
    }
}

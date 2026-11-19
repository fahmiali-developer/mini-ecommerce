package com.fahmi.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiResponseTest {

    @Test
    void shouldBuildSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success(ResponseMessage.LOGIN_SUCCESS, "token-123");
        assertEquals(ResponseStatus.SUCCESS, response.getStatus());
        assertEquals(ResponseMessage.LOGIN_SUCCESS, response.getMessage());
        assertEquals("token-123", response.getData());
    }

    @Test
    void shouldBuildErrorResponse() {
        ApiResponse<Void> response = ApiResponse.error(ResponseMessage.INVALID_CREDENTIALS);
        assertEquals(ResponseStatus.ERROR, response.getStatus());
        assertEquals(ResponseMessage.INVALID_CREDENTIALS, response.getMessage());
        assertNull(response.getData());
    }
}

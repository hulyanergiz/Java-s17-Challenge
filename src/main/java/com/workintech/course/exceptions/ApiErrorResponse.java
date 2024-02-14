package com.workintech.course.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime createdAt;
}

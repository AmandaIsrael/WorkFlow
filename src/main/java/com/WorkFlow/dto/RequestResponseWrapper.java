package com.WorkFlow.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record RequestResponseWrapper(HttpServletRequest request, HttpServletResponse response) {}
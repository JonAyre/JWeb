package com.jweb.server;

public record ServiceResponse(int statusCode,
                              String response) {
}

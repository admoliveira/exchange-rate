package com.admoliveira.exchangerate.model;

public record RateLimitStatus(int limit, int remaining, long reset, boolean isRequestAllowed) {

}

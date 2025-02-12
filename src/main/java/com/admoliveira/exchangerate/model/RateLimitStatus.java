package com.admoliveira.exchangerate.model;

public record RateLimitStatus(long limit, long remaining, long reset, boolean isRequestAllowed) {

}

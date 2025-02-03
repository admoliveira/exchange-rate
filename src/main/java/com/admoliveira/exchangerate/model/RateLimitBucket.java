package com.admoliveira.exchangerate.model;

public record RateLimitBucket (int limit, int remaining, long reset) {

    public boolean isRequestAllowed() {
        return remaining > 0;
    }
}

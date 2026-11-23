package main.java.org.example.amazonlocker.model;

import java.time.LocalDateTime;

public class AccessToken {
    private final String code;
    private final int lockerId;
    private final LocalDateTime expiryTime;

    public AccessToken(String code, int lockerId, LocalDateTime expiryTime) {
        this.code = code;
        this.lockerId = lockerId;
        this.expiryTime = expiryTime;
    }

    public String getCode() {
        return code;
    }

    public int getLockerId() {
        return lockerId;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return !LocalDateTime.now().isBefore(expiryTime);
    }
}

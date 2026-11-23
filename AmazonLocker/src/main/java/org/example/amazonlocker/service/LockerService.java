package main.java.org.example.amazonlocker.service;

import main.java.org.example.amazonlocker.enums.RetrievalStatus;
import main.java.org.example.amazonlocker.enums.Size;
import main.java.org.example.amazonlocker.model.AccessToken;
import main.java.org.example.amazonlocker.model.LockerCompartment;
import main.java.org.example.amazonlocker.model.UserPackage;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LockerService {
    private static final int TOKEN_VALIDITY = 7;

    private final Map<Integer, LockerCompartment> lockers;
    private final Map<String, AccessToken> accessTokens;

    private final SecureRandom random = new SecureRandom();

    public LockerService(Map<Integer, LockerCompartment> lockers) {
        this.lockers = lockers;
        this.accessTokens = new HashMap<>();
    }

    private LockerCompartment findAvailableLocker(Size packageSize) {
        Size[] sizes = Size.values();

        for(int i=packageSize.ordinal(); i<sizes.length; i++) {
            Size requiredSize = sizes[i];

            for(LockerCompartment locker: lockers.values()) {
                if(locker.isAvailable() && locker.getLockerSize() == requiredSize) {
                    return locker;
                }
            }
        }

        return null;
    }

    private String generateToken() {
        String token;

        do {
            int number = random.nextInt(1_000_000);
            token = String.format("%06d", number);
        } while (accessTokens.containsKey(token));

        return token;
    }

    public synchronized String putPackageInLocker(UserPackage userPackage) {
        LockerCompartment locker = findAvailableLocker(userPackage.getSize());

        if(locker == null) {
            return null;
        }

        String tokenCode = generateToken();

        LocalDateTime expiryTime = LocalDateTime.now().plusDays(TOKEN_VALIDITY);

        AccessToken token = new AccessToken(tokenCode, locker.getLockerId(), expiryTime);

        locker.storePackage(userPackage);

        accessTokens.put(tokenCode, token);
        return tokenCode;
    }

    public synchronized RetrievalStatus retrievePackageFromLocker(String tokenCode) {
        AccessToken token = accessTokens.get(tokenCode);

        if(token == null) {
            return RetrievalStatus.INVALID_TOKEN;
        }

        if(token.isExpired()) {
            return RetrievalStatus.EXPIRED_TOKEN;
        }

        LockerCompartment locker = lockers.get(token.getLockerId());

        if(locker == null || locker.isAvailable()) {
            return RetrievalStatus.LOCKER_NOT_FOUND;
        }

        locker.removePackage();

        accessTokens.remove(tokenCode);

        return RetrievalStatus.SUCCESS;
    }

    public synchronized UserPackage removePackageByStaff(int lockerId) {
        LockerCompartment locker = lockers.get(lockerId);

        if(locker == null || locker.isAvailable()) {
            return null;
        }

        AccessToken associatedToken = null;

        for(AccessToken accessToken: accessTokens.values()) {
            if(accessToken.getLockerId() == lockerId) {
                associatedToken = accessToken;
                break;
            }
        }

        if(associatedToken == null || !associatedToken.isExpired()) {
            return null;
        }

        UserPackage removedPackage = locker.removePackage();

        accessTokens.remove(associatedToken.getCode());

        return removedPackage;
    }
}

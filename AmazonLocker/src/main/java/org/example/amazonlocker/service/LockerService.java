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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class LockerService {
    private static final int TOKEN_VALIDITY = 7;

    private final Map<Integer, LockerCompartment> lockers;
    private final Map<Size, Queue<LockerCompartment>> availableLockers;

    private final Map<String, AccessToken> accessTokens;
    private final Map<Integer, String> lockerToToken;

    private final SecureRandom random = new SecureRandom();

    public LockerService(Map<Integer, LockerCompartment> lockers) {
        this.lockers = new ConcurrentHashMap<>(lockers);
        this.accessTokens = new ConcurrentHashMap<>();

        this.lockerToToken = new ConcurrentHashMap<>();
        this.availableLockers = new EnumMap<>(Size.class);

        for (Size size : Size.values()) {
            availableLockers.put(size, new ConcurrentLinkedQueue<>());
        }

        for (LockerCompartment locker : this.lockers.values()) {

            if (locker.isAvailable()) {
                availableLockers.get(locker.getLockerSize()).offer(locker);
            }
        }
    }

    private LockerCompartment findAvailableLocker(Size packageSize) {
        Size[] sizes = Size.values();

        for(int i=packageSize.ordinal(); i<sizes.length; i++) {
            Size requiredSize = sizes[i];

            LockerCompartment locker = availableLockers.get(requiredSize).poll();

            if(locker != null) {
                return locker;
            }
        }

        return null;
    }

    private AccessToken generateToken(int lockerId) {
        while (true) {
            int number = random.nextInt(1_000_000);
            String tokenCode = String.format("%06d", number);

            LocalDateTime expiryTime = LocalDateTime.now().plusDays(TOKEN_VALIDITY);
            AccessToken token = new AccessToken(tokenCode, lockerId, expiryTime);

            AccessToken existingToken = accessTokens.putIfAbsent(tokenCode, token);

            if(existingToken == null) {
                return token;
            }
        }
    }

    public String putPackageInLocker(UserPackage userPackage) {
        LockerCompartment locker = findAvailableLocker(userPackage.getSize());

        if(locker == null) {
            return null;
        }

        AccessToken token = null;
        boolean packageStored = false;

        try {
            token = generateToken(locker.getLockerId());

            locker.storePackage(userPackage);
            packageStored = true;

            lockerToToken.put(locker.getLockerId(), token.getCode());

            return token.getCode();
        } catch (RuntimeException exception) {
            if(token != null) {
                accessTokens.remove(token.getCode(), token);
            }

            lockerToToken.remove(
                    locker.getLockerId(),
                    token == null ? null : token.getCode()
            );

            if (packageStored) {
                locker.removePackage();
            }

            if(locker.isAvailable()) {
                availableLockers.get(locker.getLockerSize()).offer(locker);
            }

            throw exception;
        }
    }

    public RetrievalStatus retrievePackageFromLocker(String tokenCode) {
        if (tokenCode == null) {
            return RetrievalStatus.INVALID_TOKEN;
        }

        AccessToken token = accessTokens.get(tokenCode);

        if(token == null) {
            return RetrievalStatus.INVALID_TOKEN;
        }

        if(token.isExpired()) {
            return RetrievalStatus.EXPIRED_TOKEN;
        }

        if (!accessTokens.remove(tokenCode, token)) {
            return RetrievalStatus.INVALID_TOKEN;
        }

        LockerCompartment locker = lockers.get(token.getLockerId());

        if(locker == null || locker.isAvailable()) {
            lockerToToken.remove(token.getLockerId(), tokenCode);
            return RetrievalStatus.LOCKER_NOT_FOUND;
        }

        UserPackage removedPackage = locker.removePackage();
        lockerToToken.remove(token.getLockerId(), tokenCode);

        if (removedPackage == null) {
            return RetrievalStatus.LOCKER_NOT_FOUND;
        }

        availableLockers.get(locker.getLockerSize()).offer(locker);

        return RetrievalStatus.SUCCESS;
    }

    public UserPackage removePackageByStaff(int lockerId) {
        LockerCompartment locker = lockers.get(lockerId);

        if(locker == null || locker.isAvailable()) {
            return null;
        }

        String tokenCode = lockerToToken.get(lockerId);

        if (tokenCode == null) {
            return null;
        }

        AccessToken associatedToken = accessTokens.get(tokenCode);

        if (associatedToken == null || !associatedToken.isExpired()) {
            return null;
        }

        if (!accessTokens.remove(tokenCode, associatedToken)) {
            return null;
        }

        lockerToToken.remove(lockerId, tokenCode);

        UserPackage removedPackage = locker.removePackage();

        if (removedPackage == null) {
            return null;
        }

        availableLockers.get(locker.getLockerSize()).offer(locker);

        return removedPackage;
    }
}

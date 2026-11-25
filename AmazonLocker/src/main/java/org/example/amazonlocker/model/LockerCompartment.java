package main.java.org.example.amazonlocker.model;

import main.java.org.example.amazonlocker.enums.Size;

public class LockerCompartment {
    private final int lockerId;
    private final Size size;
    private UserPackage storedPackage;

    public LockerCompartment(int lockerId, Size size, UserPackage storedPackage) {
        this.lockerId = lockerId;
        this.size = size;
        this.storedPackage = storedPackage;
    }

    public int getLockerId() {
        return lockerId;
    }

    public Size getLockerSize() {
        return size;
    }

    public synchronized boolean isAvailable() {
        return storedPackage == null;
    }

    public synchronized void storePackage(UserPackage userPackage) {
        if(storedPackage != null) {
            throw new IllegalStateException("Locker is already occupied");
        }

        this.storedPackage = userPackage;
    }

    public synchronized UserPackage removePackage() {
        UserPackage removedPackage = storedPackage;
        storedPackage = null;
        return removedPackage;
    }

    public synchronized UserPackage getStoredPackage() {
        return storedPackage;
    }
}

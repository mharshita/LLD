package main.java.org.example.amazonlocker.model;

import main.java.org.example.amazonlocker.enums.Size;

public class UserPackage {
    private final String packageId;
    private final Size size;

    public UserPackage(String packageId, Size size) {
        this.packageId = packageId;
        this.size = size;
    }

    public String getPackageId() {
        return packageId;
    }

    public Size getSize() {
        return size;
    }
}

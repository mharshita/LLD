import main.java.org.example.amazonlocker.enums.Size;
import main.java.org.example.amazonlocker.model.LockerCompartment;
import main.java.org.example.amazonlocker.model.UserPackage;
import main.java.org.example.amazonlocker.service.LockerService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
       Map<Integer, LockerCompartment> lockers = new HashMap<>();

        lockers.put(1, new LockerCompartment(1, Size.SMALL, null));
        lockers.put(2, new LockerCompartment(2, Size.SMALL, null));
        lockers.put(3, new LockerCompartment(3, Size.MEDIUM, null));
        lockers.put(4, new LockerCompartment(4, Size.LARGE, null));

        LockerService service = new LockerService(lockers);

        UserPackage package1 =
                new UserPackage("P001", Size.SMALL);

        UserPackage package2 =
                new UserPackage("P002", Size.MEDIUM);

//        UserPackage package3 =
//                new UserPackage("P003", Size.LARGE);
//
//        UserPackage package4 =
//                new UserPackage("P004", Size.LARGE);


        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startSignal = new CountDownLatch(1);

     Future<String> result1 = executor.submit(() -> {
              startSignal.await();
              return service.putPackageInLocker(package1);
             }
     );

     Future<String> result2 = executor.submit(() -> {
       startSignal.await();
       return service.putPackageInLocker(package2);
     });

     startSignal.countDown();

     String token1 = result1.get();
     String token2 = result2.get();

//        String token1 = service.putPackageInLocker(package1);
//        String token2 = service.putPackageInLocker(package2);
//        String token3 = service.putPackageInLocker(package3);
//        String token4 = service.putPackageInLocker(package4);

        System.out.println("Package 1 token: " + token1);
        System.out.println("Package 2 token: " + token2);
//        System.out.println("Package 3 token: " + token3);
//        System.out.println("Package 4 token: " + token4);

        System.out.println(
                "Retrieval: " +
                        service.retrievePackageFromLocker(token1)
        );

        System.out.println(
                "Retrieval again: " +
                        service.retrievePackageFromLocker(token1)
        );

        System.out.println(
                "Invalid token: " +
                        service.retrievePackageFromLocker("123456")
        );

     executor.shutdown();
    }
}
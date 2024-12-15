package org.alsception.bootboard.utils;

import java.util.concurrent.atomic.AtomicLong;

public class UniqueIdGenerator {
    private static final AtomicLong counter = new AtomicLong();

    public static long generateAtomicId() {
        long uniqueID = counter.incrementAndGet(); // Gets the next unique ID
        System.out.println("Generated Unique ID: " + uniqueID);
        return uniqueID;
    }
    
    public static long generateNanoId() {
        long uniqueID = System.nanoTime(); // High-resolution time-based unique ID
        System.out.println("Generated Unique ID: " + uniqueID);
        return uniqueID;
    }
}

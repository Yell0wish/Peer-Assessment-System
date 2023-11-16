package com.ye.utils;

import reactor.util.annotation.Nullable;

import java.util.*;

public class AllocateAlgorithm {
    @Nullable
    public static Map<Integer, Integer> allocateRandom(List<Integer> submitList) {

        Map<Integer, Integer> allocationMap = new HashMap<>();
        List<Integer> remainingNumbers = new ArrayList<>(submitList);

        if (submitList.isEmpty() || submitList.size() == 1) {
            return null;
        }

        Random random = new Random();

        for (int number : submitList) {
            int index = random.nextInt(remainingNumbers.size());
            int allocatedNumber = remainingNumbers.get(index);

            // Ensure a number is not mapped to itself
            while (allocatedNumber == number) {
                index = random.nextInt(remainingNumbers.size());
                allocatedNumber = remainingNumbers.get(index);
            }

            allocationMap.put(number, allocatedNumber);
            remainingNumbers.remove(index);
        }

        return allocationMap;
    }
}

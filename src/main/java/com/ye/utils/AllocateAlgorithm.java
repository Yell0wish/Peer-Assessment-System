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

    // 互助学习环境下可抗恶意评价的同伴互评算法
    @Nullable
    public static Map<Integer, List<Integer>> allocateRandom(List<Integer> submitList, int everyoneReviewNum) {

        if (submitList.isEmpty() || submitList.size() == 1 || submitList.size() <= everyoneReviewNum) {
            return null;
        }

        Map<Integer, List<Integer>> allocationMap = new HashMap<>();

        for (int i = 0; i < submitList.size(); i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < everyoneReviewNum; j++) {
                list.add(submitList.get((i + j + 1) % submitList.size()));
            }
            allocationMap.put(submitList.get(i), list);
        }

        // 将allocationMap中key和value的顺序打乱
        List<Integer> keyList = new ArrayList<>(allocationMap.keySet());
        List<List<Integer>> valueList = new ArrayList<>(allocationMap.values());
        Collections.shuffle(keyList);
        Collections.shuffle(valueList);

        // 需要防止自己评价自己

        Map<Integer, List<Integer>> newAllocationMap = new HashMap<>();
        for (Integer integer : keyList) {
            int j = 0;
            while (valueList.get(j).contains(integer)) {
                j++;
            }
            newAllocationMap.put(integer, valueList.get(j));
            valueList.remove(j);
        }

        return newAllocationMap;
    }

}

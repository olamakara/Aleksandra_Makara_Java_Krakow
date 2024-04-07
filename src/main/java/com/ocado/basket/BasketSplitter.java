package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasketSplitter {

    private Map<String, List<String>> configMap;

    private List<String> deliveries;

    public BasketSplitter(String absolutePathToConfigFile) {
        try {
            // reading items from json and saving deliveries types
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, List<String>>> typeRef = new TypeReference<Map<String, List<String>>>() {};
            configMap = objectMapper.readValue(new File(absolutePathToConfigFile), typeRef);
            deliveries = configMap.values().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getConfigMap() {
        return configMap;
    }

    // generating all subsets of deliveries, e.g. [1,2,3] -> [[1],[2],[3],[1,2],[1,3],[2,3],[1,2,3]]
    public static <T> List<List<T>> generateSubsets(List<T> list) {
        int n = list.size();
        return IntStream.range(1, (1 << n))
                .mapToObj(mask -> IntStream.range(0, n)
                        .filter(bit -> (mask & (1 << bit)) != 0)
                        .mapToObj(list::get)
                        .collect(Collectors.toList()))
                .sorted(Comparator.comparing(List::size))
                .collect(Collectors.toList());
    }

    // creating reverse map of config: "product": [deli1, deli2] -> "deli1": [product], "deli2": [product]
    public Map<String, List<String>> createReverseConfig(List<String> items) {
        Map<String, List<String>> reverseCurrentConfigMap = new HashMap<>();
        for (String delivery : deliveries) {
            reverseCurrentConfigMap.put(delivery, new ArrayList<>());
        }
        for (String item : items) {
            for (String delivery : configMap.get(item)) {
                reverseCurrentConfigMap.get(delivery).add(item);
            }
        }
        return reverseCurrentConfigMap;
    }

    // finding min deliveries to cover all items
    private List<String> chooseDeliveries(List<String> items, Map<String, List<String>> reverseCurrentConfigMap) {
        List<List<String>> deliveriesSubsets = generateSubsets(deliveries);
        Set<String> takenItemsSet = new HashSet<>();
        List<String> takenDeliveries = new ArrayList<>();
        for (List<String> subset : deliveriesSubsets) {
            takenDeliveries = subset;
            for (String delivery : subset) {
                takenItemsSet.addAll(reverseCurrentConfigMap.get(delivery));
            }
            if (takenItemsSet.size() == items.size()) {
                break;
            }
            takenItemsSet.clear();
        }
        return takenDeliveries;
    }

    // pairing items with delivery so that deliveries have max possible items
    private Map<String, List<String>> createMaxDeliveryGroups(
            List<String> items, Map<String,
            List<String>> reverseCurrentConfigMap,
            List<String> takenDeliveries)
    {
        Map<String, List<String>> result = new HashMap<>();
        Set<String> takenItemsSet = new HashSet<>();
        while (takenItemsSet.size() < items.size()) {
            int maxItems = 0;
            String biggestDelivery = "";
            List<String> currentItems;
            for (String delivery: takenDeliveries) {
                currentItems = reverseCurrentConfigMap.get(delivery);
                currentItems.removeAll(takenItemsSet);
                int size = currentItems.size();
                if (size > maxItems) {
                    maxItems = size;
                    biggestDelivery = delivery;
                }
            }
            currentItems = reverseCurrentConfigMap.get(biggestDelivery);
            currentItems.removeAll(takenItemsSet);
            result.put(biggestDelivery, currentItems);
            takenItemsSet.addAll(currentItems);
            takenDeliveries.remove(biggestDelivery);
        }
        return result;
    }

    public Map<String, List<String>> split(List<String> items) {

        Map<String, List<String>> reverseCurrentConfigMap = createReverseConfig(items);

        List<String> takenDeliveries = chooseDeliveries(items, reverseCurrentConfigMap);

        return createMaxDeliveryGroups(items, reverseCurrentConfigMap, takenDeliveries);
    }

}

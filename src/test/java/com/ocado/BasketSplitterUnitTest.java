package com.ocado;

import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasketSplitterUnitTest {

    @Test
    public void testGeneratingSubsets() {
        List<Integer> inputList = List.of(1, 2, 3);
        List<List<Integer>> expectedSubsets = List.of(
                List.of(1),
                List.of(2),
                List.of(3),
                List.of(1, 2),
                List.of(1, 3),
                List.of(2, 3),
                List.of(1, 2, 3)
        );

        List<List<Integer>> actualSubsets = BasketSplitter.generateSubsets(inputList);

        assertEquals(expectedSubsets.size(), actualSubsets.size());
        assertTrue(actualSubsets.containsAll(expectedSubsets));
    }

    @Test
    public void testCreatingReverseConfig() {
        BasketSplitter splitter = new BasketSplitter("src/test/resources/config.json");
        List<String> items = List.of("Cheese Cloth", "English Muffin", "Ecolab - Medallion");

        Map<String, List<String>> reverseConfigMap = splitter.createReverseConfig(items);

        assertEquals(8, reverseConfigMap.size());
        assertTrue(reverseConfigMap.containsKey("Courier"));
        assertTrue(reverseConfigMap.containsKey("Parcel locker"));
        assertTrue(reverseConfigMap.containsKey("Same day delivery"));
        assertTrue(reverseConfigMap.containsKey("Next day shipping"));
        assertTrue(reverseConfigMap.containsKey("Pick-up point"));
        assertTrue(reverseConfigMap.containsKey("Mailbox delivery"));
        assertTrue(reverseConfigMap.containsKey("In-store pick-up"));
        assertTrue(reverseConfigMap.containsKey("Express Collection"));

        assertEquals(List.of("Cheese Cloth", "English Muffin", "Ecolab - Medallion"), reverseConfigMap.get("Courier"));
        assertEquals(List.of("Cheese Cloth", "English Muffin", "Ecolab - Medallion"), reverseConfigMap.get("Parcel locker"));
        assertEquals(List.of("Cheese Cloth"), reverseConfigMap.get("Same day delivery"));
        assertEquals(List.of("Cheese Cloth", "English Muffin"), reverseConfigMap.get("Next day shipping"));
        assertEquals(List.of("Cheese Cloth"), reverseConfigMap.get("Pick-up point"));
        assertEquals(List.of("English Muffin", "Ecolab - Medallion"), reverseConfigMap.get("Mailbox delivery"));
        assertEquals(List.of("English Muffin", "Ecolab - Medallion"), reverseConfigMap.get("In-store pick-up"));
        assertEquals(List.of("English Muffin"), reverseConfigMap.get("Express Collection"));
    }

    @Test
    public void testSplit() {
        BasketSplitter splitter = new BasketSplitter("src/test/resources/config.json");
        List<String> items = List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht");

        Map<String, List<String>> result = splitter.split(items);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Courier"));
        assertTrue(result.containsKey("Pick-up point"));
        assertEquals(List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"), result.get("Courier"));
        assertEquals(List.of("Fond - Chocolate"), result.get("Pick-up point"));
    }
}

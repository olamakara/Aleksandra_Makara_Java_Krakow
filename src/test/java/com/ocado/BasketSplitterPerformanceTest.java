package com.ocado;


import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;

public class BasketSplitterPerformanceTest {

    @Test
    @Timeout(1)
    // max items: 100
    public void testSplitPerformanceWithMaximumSplitInput() {
        BasketSplitter splitter = new BasketSplitter("src/test/resources/config.json");
        List<String> items = new ArrayList<>(splitter.getConfigMap().keySet());
        splitter.split(items);
    }

    @Test
    @Timeout(1)
    // max items in config: 1000
    public void testSplitPerformanceWithMaximumConfigItems() {
        BasketSplitter splitter = new BasketSplitter("src/test/resources/big-config.json");
    }
}

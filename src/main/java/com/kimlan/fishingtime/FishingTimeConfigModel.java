package com.kimlan.fishingtime;

import java.util.List;
import java.util.ArrayList;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.PredicateConstraint;

@Config(name = "fishing-time", wrapperName = "FishingTimeConfig")
public class FishingTimeConfigModel {
    @PredicateConstraint("isValidRange")
    public List<Integer> fishTravelTimeRange = new ArrayList<Integer>(List.of(20, 80));

    @PredicateConstraint("isValidRange")
    public List<Integer> waitTimeRange = new ArrayList<Integer>(List.of(100, 600));

    @PredicateConstraint("isNonNegative")
    public int lureLevelFactor = 100;

    public static boolean isValidRange(List<Integer> list) {
        return list.size() == 2 && list.get(0) > 0 && list.get(1) >= list.get(0);
    }

    public static boolean isNonNegative(int n) {
        return n >= 0;
    }
}

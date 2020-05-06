package org.mabartos.api.data.general.capability.manage;

import org.mabartos.api.common.CapabilityType;

import java.util.Random;

public class CapabilityUtils {

    public static String getRandomNameForCap(CapabilityType type) {
        Random random = new Random();
        String topic = type.getTopic();
        return topic.substring(1) + "_" + random.nextInt(100000);
    }
}
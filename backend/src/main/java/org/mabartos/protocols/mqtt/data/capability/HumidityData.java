package org.mabartos.protocols.mqtt.data.capability;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mabartos.general.CapabilityType;
import org.mabartos.persistence.model.CapabilityModel;
import org.mabartos.persistence.model.capability.HumidityCapModel;
import org.mabartos.protocols.mqtt.data.CapabilityData;
import org.mabartos.protocols.mqtt.utils.MqttSerializeUtils;

public class HumidityData extends CapabilityData {

    @JsonProperty("actual")
    private Byte actual;

    public HumidityData(@JsonProperty("name") String name,
                        @JsonProperty("type") CapabilityType type,
                        @JsonProperty("actual") Byte actual) {
        super(name, type);
        this.actual = actual;
    }

    public Byte getActual() {
        return actual;
    }

    public static HumidityData fromJson(String json) {
        return MqttSerializeUtils.fromJson(json, HumidityData.class);
    }

    @Override
    public CapabilityModel editModel(CapabilityModel model) {
        if (model != null) {
            super.editModel(model);
            HumidityCapModel hum = (HumidityCapModel) model;
            hum.setType(CapabilityType.HUMIDITY);
            hum.setValue(getActual());
            return hum;
        }
        return null;
    }
}

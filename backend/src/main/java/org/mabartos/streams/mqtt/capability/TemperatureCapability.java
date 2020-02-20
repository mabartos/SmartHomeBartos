package org.mabartos.streams.mqtt.capability;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.mabartos.persistence.model.CapabilityModel;
import org.mabartos.persistence.model.capability.TemperatureCapModel;
import org.mabartos.service.core.CapabilityService;
import org.mabartos.streams.mqtt.BarMqttClient;
import org.mabartos.streams.mqtt.exceptions.WrongMessageTopicException;
import org.mabartos.streams.mqtt.messages.BarMqttSender;
import org.mabartos.streams.mqtt.messages.capability.TemperatureCapMessage;
import org.mabartos.streams.mqtt.topics.CapabilityTopic;

public class TemperatureCapability extends GeneralMqttCapability<TemperatureCapModel> {

    private TemperatureCapMessage tempMessage;

    public TemperatureCapability() {
        super();
    }

    public TemperatureCapability(BarMqttClient client, CapabilityService capabilityService, CapabilityTopic capabilityTopic, MqttMessage message) {
        super(client, capabilityService, capabilityTopic, message);
        try {
            this.tempMessage = TemperatureCapMessage.fromJson(message.toString());
        } catch (WrongMessageTopicException e) {
            BarMqttSender.sendResponse(client, 400, "Wrong message");
        }
    }

    @Override
    public void parseMessage() {
        if (tempMessage != null) {
            CapabilityModel model = capabilityService.findByID(capabilityTopic.getCapabilityID());
            if (model instanceof TemperatureCapModel) {
                TemperatureCapModel result = (TemperatureCapModel) model;
                result.setValue(tempMessage.getActualTemperature());
                CapabilityModel updated = capabilityService.updateByID(capabilityTopic.getCapabilityID(), model);
                if (updated != null) {
                    BarMqttSender.sendResponse(client, 200, "Temperature was updated");
                    return;
                }
            }
        }
        BarMqttSender.sendResponse(client, 400, "Update temperature");
    }
}

package org.mabartos.protocols.mqtt.capability;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.mabartos.api.protocol.BartMqttClient;
import org.mabartos.api.service.AppServices;
import org.mabartos.persistence.model.CapabilityModel;
import org.mabartos.protocols.mqtt.topics.CapabilityTopic;

import java.util.logging.Logger;

public class GeneralMqttCapability {

    protected static Logger logger = Logger.getLogger(GeneralMqttCapability.class.getName());

    protected MqttMessage message;
    protected CapabilityModel model;
    protected CapabilityTopic capabilityTopic;
    protected AppServices services;
    protected BartMqttClient client;

    public GeneralMqttCapability() {
    }

    public GeneralMqttCapability(AppServices services, BartMqttClient client, CapabilityTopic capabilityTopic, MqttMessage message) {
        this.services = services;
        this.capabilityTopic = capabilityTopic;
        this.message = message;
        this.model = services.capabilities().findByID(capabilityTopic.getCapabilityID());
        this.client = client;
    }

    public void parseMessage() {
        logger.info("Device type: " + capabilityTopic.getCapabilityType() + ", message: " + message);
    }
}
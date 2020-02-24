package org.mabartos.protocols.mqtt;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jboss.logmanager.Level;
import org.mabartos.api.model.BartSession;
import org.mabartos.persistence.model.HomeModel;
import org.mabartos.protocols.mqtt.capability.HumidityCapability;
import org.mabartos.protocols.mqtt.capability.LightCapability;
import org.mabartos.protocols.mqtt.capability.TemperatureCapability;
import org.mabartos.protocols.mqtt.topics.CapabilityTopic;
import org.mabartos.protocols.mqtt.topics.GeneralTopic;
import org.mabartos.protocols.mqtt.utils.TopicUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class BartMqttHandler {
    public static Logger logger = Logger.getLogger(BartMqttHandler.class.getName());

    BartSession session;
    HandleManageMessage handler;

    void onStartup(@Observes StartupEvent event) {
        logger.info("Initialized BarMqttHandler Bean");
    }

    @Inject
    public BartMqttHandler(BartSession session, HandleManageMessage handler) {
        this.session = session;
        this.handler = handler;
    }

    //TODO own exceptions, common topics
    public void executeMessage(HomeModel home, final String receivedTopic, final MqttMessage message) {
        String homeTopic = TopicUtils.getHomeTopic(home);
        try {
            GeneralTopic resultTopic = TopicUtils.getSpecificTopic(receivedTopic);

            if (resultTopic != null && homeTopic != null && receivedTopic.length() > homeTopic.length()) {
                handler.init(home, resultTopic, message);

                // It's the 'manage' topic
                if (handler.handleManageTopics())
                    return;

                if (resultTopic instanceof CapabilityTopic) {
                    CapabilityTopic capTopic = (CapabilityTopic) resultTopic;
                    redirectParsing(capTopic, message);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            logger.log(Level.ERROR, "Invalid topic : " + receivedTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectParsing(CapabilityTopic capabilityTopic, MqttMessage message) {
        switch (capabilityTopic.getCapabilityType()) {
            case NONE:
                break;
            case TEMPERATURE:
                new TemperatureCapability(session, capabilityTopic, message).parseMessage();
                break;
            case HUMIDITY:
                new HumidityCapability(session, capabilityTopic, message).parseMessage();
                break;
            case HEATER:
                break;
            case LIGHT:
                new LightCapability(session, capabilityTopic, message).parseMessage();
                break;
            case RELAY:
                break;
            case SOCKET:
                break;
            case PIR:
                break;
            case GAS_SENSOR:
                break;
            case STATISTICS:
                break;
            case AIRCONDITIONER:
                break;
            case OTHER:
                break;
            default:
                // NOP
                break;
        }
    }

}

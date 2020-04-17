
#include "TemperatureCap.h"

#include "mqtt/MqttClient.h"

extern MqttClient client;

TemperatureCap::TemperatureCap(const uint8_t &pin) : CapabilityWithValue(pin) {
    _type = CapabilityType::TEMPERATURE;
    setName(getRandomName());
}

void TemperatureCap::init() {
    Serial.println("TEMP_INIT");
}

void TemperatureCap::execute() {
    Serial.println("TEMP_EXEC");
}

void TemperatureCap::reactToMessage(const JsonObject &obj) {
    Serial.println("TEMP_REACT");
}

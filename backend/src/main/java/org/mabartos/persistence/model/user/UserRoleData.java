package org.mabartos.persistence.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mabartos.general.UserRole;
import org.mabartos.protocols.mqtt.data.general.MqttSerializable;

public class UserRoleData implements MqttSerializable {

    @JsonProperty("id")
    private Long homeID;

    @JsonProperty("role")
    private UserRole role;

    @JsonCreator
    public UserRoleData(@JsonProperty("id") Long homeID,
                        @JsonProperty("role") UserRole role) {
        this.role = role;
        this.homeID = homeID;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getHomeID() {
        return homeID;
    }

    public void setHomeID(Long homeID) {
        this.homeID = homeID;
    }
}

package org.mabartos.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.mabartos.api.controller.device.DevicesResource;
import org.mabartos.interfaces.IdentifiableName;
import org.mabartos.persistence.model.home.HomeModel;
import org.mabartos.persistence.model.room.RoomModel;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Devices")
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
        @NamedQuery(name = "setDeviceRoomToNull", query = "update DeviceModel set room=null where room.id=:roomID"),
        @NamedQuery(name = "deleteDevicesFromHome", query = "delete from DeviceModel where home.id=:homeID")
})
public class DeviceModel extends PanacheEntityBase implements Serializable, IdentifiableName<Long> {

    @Id
    @GeneratedValue
    @Column(name = "DEVICE_ID")
    Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "ROOM")
    private RoomModel room;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "HOME")
    private HomeModel home;

    @OneToMany(targetEntity = CapabilityModel.class, mappedBy = "device", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CapabilityModel> capabilities = new HashSet<>();

    public DeviceModel() {
    }

    public DeviceModel(String name) {
        this.name = name;
    }

    public DeviceModel(String name, Set<CapabilityModel> capabilities) {
        this(name);
        setCapabilities(capabilities);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Long getID() {
        return this.id;
    }

    @Override
    public void setID(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public RoomModel getRoom() {
        return room;
    }

    @JsonProperty("roomID")
    public Long getRoomID() {
        return (room != null) ? room.getID() : -1;
    }

    @JsonProperty("homeID")
    public Long getHomeID() {
        return (home != null) ? home.getID() : -1;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }

    public String getTopic() {
        if (home != null) {
            return home.getMqttClient().getTopic() + DevicesResource.DEVICE_PATH + "/" + id;
        }
        return null;
    }

    @JsonIgnore
    public HomeModel getHome() {
        return home;
    }

    public void setHome(HomeModel home) {
        this.home = home;
    }

    @JsonIgnore
    public Set<String> getCapabilitiesName() {
        if (capabilities != null) {
            return capabilities
                    .stream()
                    .map(CapabilityModel::getName)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @JsonIgnore
    public Set<CapabilityModel> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Set<CapabilityModel> capabilities) {
        this.capabilities = capabilities;
        this.capabilities.forEach(f -> f.setDevice(this));
    }

    public boolean addCapability(CapabilityModel capability) {
        return capabilities.add(capability);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof DeviceModel))
            return false;
        else {
            DeviceModel object = (DeviceModel) obj;

            if (object.getRoom() != null && this.getRoom() != null && !object.getRoom().equals(this.getRoom())) {
                return false;
            }

            return (object.getID().equals(this.getID())
                    && object.getName().equals(this.getName())
                    && object.getHome().equals(this.getHome())
            );
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, room, home);
    }

}

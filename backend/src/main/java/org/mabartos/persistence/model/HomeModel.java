package org.mabartos.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.mabartos.general.UserRole;
import org.mabartos.interfaces.HasChildren;
import org.mabartos.utils.DedicatedUserRole;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Homes")
@Cacheable
public class HomeModel extends PanacheEntityBase implements HasChildren<RoomModel> {

    @Id
    @GeneratedValue
    @Column(name = "HOME_ID")
    Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String brokerURL;

    @Column
    private String imageURL;

    @Column
    private String topic = "/homes/" + id;

    @OneToMany(targetEntity = DeviceModel.class, mappedBy = "home", cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<DeviceModel> unAssignedDevices = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "HOMES_USERS",
            joinColumns = {
                    @JoinColumn(referencedColumnName = "HOME_ID")},
            inverseJoinColumns = {
                    @JoinColumn(referencedColumnName = "USER_ID")}
    )
    private Set<UserModel> usersSet = new HashSet<>();

    @OneToMany(targetEntity = RoomModel.class, mappedBy = "home", cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<RoomModel> roomsSet = new HashSet<>();

    @ElementCollection
    @JsonIgnore
    private Set<DedicatedUserRole> userRoles = new HashSet<>();

    public HomeModel() {
        setTopic("/homes/" + id);
    }

    public HomeModel(String name) {
        this.name = name;
        setTopic("/homes/" + id);
    }

    public HomeModel(String name, String brokerURL) {
        this(name);
        this.brokerURL = brokerURL;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public void setID(Long id) {
        this.id = id;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTopic() {
        if (topic == null)
            return "/homes/" + id;
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // Collections
    @JsonIgnore
    public Set<UserModel> getUsers() {
        return usersSet;
    }

    public boolean addUser(UserModel user) {
        return usersSet.add(user);
    }

    public boolean removeUser(UserModel user) {
        return user.removeChild(this) && usersSet.remove(user);
    }

    public boolean removeUserByID(Long id) {
        return usersSet.removeIf(user -> user.getID().equals(id));
    }

    @JsonIgnore
    public Set<DedicatedUserRole> getUserRoles() {
        return userRoles;
    }

    public boolean changeRoleForUser(UserModel user, UserRole role) {
        return userRoles.add(new DedicatedUserRole(user, role));
    }

    public boolean changeRoleForUser(DedicatedUserRole userRole) {
        return userRoles.add(userRole);
    }

    public boolean removeRoleForUser(UserModel user, UserRole role) {
        return userRoles.remove(new DedicatedUserRole(user, role));
    }

    public boolean removeRoleForUser(DedicatedUserRole userRole) {
        return userRoles.remove(userRole);
    }

    @JsonIgnore
    public Set<DeviceModel> getUnAssignedDevices() {
        return unAssignedDevices;
    }

    public boolean addDevice(DeviceModel device) {
        return unAssignedDevices.add(device);
    }

    public boolean removeDeviceFromHome(DeviceModel device) {
        return unAssignedDevices.remove(device);
    }

    @Override
    @JsonIgnore
    public Set<RoomModel> getChildren() {
        return roomsSet;
    }

    @Override
    public boolean addChild(RoomModel child) {
        return roomsSet.add(child);
    }

    @Override
    public boolean removeChild(RoomModel child) {
        return roomsSet.remove(child);
    }

    @Override
    public boolean removeChildByID(Long id) {
        return roomsSet.removeIf(room -> room.getID().equals(id));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof HomeModel))
            return false;
        else {
            HomeModel object = (HomeModel) obj;
            return (object.getID().equals(this.getID())
                    && object.getName().equals(this.getName())
                    && object.getBrokerURL().equals(this.getBrokerURL())
                    && object.getImageURL().equals(this.getImageURL())
                    && object.getChildren().equals(this.getChildren())
                    && object.getTopic().equals(this.getTopic())
                    && object.getUsers().equals(this.getUsers())
            );
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brokerURL, imageURL, topic, getChildren(), getUsers());
    }
}

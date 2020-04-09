package org.mabartos.api.controller.home;

import org.mabartos.api.controller.device.DevicesResource;
import org.mabartos.api.controller.home.invitations.HomeInvitationsResource;
import org.mabartos.api.controller.home.mqtt.MqttResource;
import org.mabartos.api.controller.room.RoomsResource;
import org.mabartos.api.controller.user.UsersResource;
import org.mabartos.authz.annotations.HasRoleInHome;
import org.mabartos.general.UserRole;
import org.mabartos.persistence.model.DeviceModel;
import org.mabartos.persistence.model.home.HomeModel;
import org.mabartos.persistence.model.user.UserRoleData;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@HasRoleInHome
public interface HomeResource {

    @GET
    @Path(DevicesResource.DEVICE_PATH)
    Set<DeviceModel> getDevices();

    @GET
    HomeModel getHome();

    @GET
    @Path("/my-role")
    UserRoleData getAuthUserRole();

    @PATCH
    @HasRoleInHome(minRole = UserRole.HOME_ADMIN)
    HomeModel updateHome(String JSON);

    @DELETE
    @HasRoleInHome(minRole = UserRole.HOME_ADMIN)
    Response deleteHome();

    @Path("/mqtt")
    MqttResource forwardToMqttInfo();

    @Path(DevicesResource.DEVICE_PATH)
    DevicesResource forwardToDevices();

    @Path(UsersResource.USER_PATH)
    UsersResource forwardToUsers();

    @Path(RoomsResource.ROOM_PATH)
    RoomsResource forwardToRooms();

    @Path(HomeInvitationsResource.INVITE_PATH)
    HomeInvitationsResource forwardToInvitations();
}

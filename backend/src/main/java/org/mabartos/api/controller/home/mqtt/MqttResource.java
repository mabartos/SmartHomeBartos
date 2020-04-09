package org.mabartos.api.controller.home.mqtt;

import org.mabartos.authz.annotations.HasRoleInHome;
import org.mabartos.general.UserRole;
import org.mabartos.persistence.model.MqttClientModel;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@HasRoleInHome
public interface MqttResource {

    @GET
    MqttClientModel getInfo();

    @GET
    @Path("/restart")
    @HasRoleInHome(minRole = UserRole.HOME_ADMIN)
    Response restartMqttClient();
}

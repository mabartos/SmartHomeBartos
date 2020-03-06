package org.mabartos.services;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import org.mabartos.api.model.MqttClientService;
import org.mabartos.api.protocol.MqttClientManager;
import org.mabartos.api.service.AppServices;
import org.mabartos.api.service.CapabilityService;
import org.mabartos.api.service.DeviceService;
import org.mabartos.api.service.HomeService;
import org.mabartos.api.service.RoomService;
import org.mabartos.api.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultAppServices implements AppServices {
    @Inject
    BeanManager beanManager;

    @Inject
    MqttClientManager mqttClientManager;

    @Inject
    Vertx vertx;

    @PersistenceContext
    EntityManager entityManager;

    public static Logger logger = Logger.getLogger(DefaultAppServices.class.getName());
    private Map<Class, Object> providers = new HashMap<>();

    @Inject
    public DefaultAppServices() {
    }

    public void start(@Observes StartupEvent start) {
        logger.info("App services are initialized");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        return (T) providers.computeIfAbsent(clazz, item -> {
            Set<Bean<?>> beans = beanManager.getBeans(clazz);
            if (beans.iterator().hasNext()) {
                Bean<?> bean = beans.iterator().next();
                CreationalContext<?> context = beanManager.createCreationalContext(bean);
                return beanManager.getReference(bean, clazz, context);
            }
            return null;
        });
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public UserService users() {
        return getProvider(UserService.class);
    }

    @Override
    public HomeService homes() {
        return getProvider(HomeService.class);
    }

    @Override
    public RoomService rooms() {
        return getProvider(RoomService.class);
    }

    @Override
    public DeviceService devices() {
        return getProvider(DeviceService.class);
    }

    @Override
    public CapabilityService capabilities() {
        return getProvider(CapabilityService.class);
    }

    @Override
    public MqttClientService mqttClientsRepresentation() {
        return getProvider(MqttClientService.class);
    }

    @Override
    public Vertx getVertx() {
        return vertx;
    }
}
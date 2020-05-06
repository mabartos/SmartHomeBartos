package org.mabartos.services.model.user;

import io.quarkus.runtime.StartupEvent;
import org.mabartos.api.model.user.UserModel;
import org.mabartos.api.service.user.UserRoleService;
import org.mabartos.api.service.user.UserService;
import org.mabartos.persistence.jpa.repository.UserRepository;
import org.mabartos.services.model.CRUDServiceImpl;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.UUID;

@Dependent
public class UserServiceImpl extends CRUDServiceImpl<UserModel, UserEntity, UserRepository, UUID> implements UserService {

    private UserRoleService userRoleService;

    public void start(@Observes StartupEvent event) {
    }

    @Inject
    UserServiceImpl(UserRepository repository, UserRoleService userRoleService) {
        super(repository);
        this.userRoleService = userRoleService;
    }

    @Override
    public UserModel findByID(UUID id) {
        try {
            Query query = getEntityManager().createNamedQuery("findUserByUUID");
            query.setParameter("id", id);
            return (UserModel) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean deleteByID(UUID id) {
        return getRepository().delete("uuid", id) > 0;
    }

    @Override
    public UserRoleService roles() {
        return userRoleService;
    }

    @Override
    public UserModel findByUsername(String username) {
        return getRepository().find("username", username).firstResultOptional().orElse(null);
    }

    @Override
    public UserModel findByEmail(String email) {
        return getRepository().find("email", email).firstResultOptional().orElse(null);
    }
}
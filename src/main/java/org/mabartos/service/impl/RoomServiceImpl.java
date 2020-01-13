package org.mabartos.service.impl;

import org.mabartos.general.RoomType;
import org.mabartos.persistence.model.RoomModel;
import org.mabartos.persistence.repository.RoomRepository;
import org.mabartos.service.core.RoomService;

import java.util.List;

public class RoomServiceImpl extends CRUDServiceImpl<RoomModel, RoomRepository> implements RoomService {

    RoomServiceImpl(RoomRepository repository) {
        super(repository);
    }

    @Override
    public List<RoomModel> findByType(RoomType type) {
        return getRepository().find("type", type).list();
    }
}

package com.gee.blg.tinyid.server;

import java.util.List;

import com.gee.blg.tinyid.base.generator.IdGenerator;
import com.gee.blg.tinyid.server.service.TinyIdTokenService;
import com.geely.space.commons.model.exceptions.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TinyId {
    @Autowired
    private TinyIdTokenService tinyIdTokenService;

    @Autowired
    private IdGeneratorFactoryServer idGeneratorFactoryServer;

    public long nextId(String bizType) {
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            List<Long> ids = idGenerator.nextId(1);
            return ids.get(0);
        } catch (Exception e) {
            log.error("tinyid get next id error", e);
            throw new BizException(406, e.getMessage(), null);
        }
    }

}

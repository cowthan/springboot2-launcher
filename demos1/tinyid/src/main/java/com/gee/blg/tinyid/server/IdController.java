package com.gee.blg.tinyid.server;

import java.util.List;

import com.gee.blg.tinyid.base.entity.SegmentId;
import com.gee.blg.tinyid.base.generator.IdGenerator;
import com.gee.blg.tinyid.base.service.SegmentIdService;
import com.gee.blg.tinyid.server.service.TinyIdTokenService;
import com.gee.blg.tinyid.server.vo.ErrorCode;
import com.geely.space.commons.model.exceptions.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author du_imba
 */
@RestController
@RequestMapping("/id/")
public class IdController {

    private static final Logger logger = LoggerFactory.getLogger(IdController.class);
    @Autowired
    private IdGeneratorFactoryServer idGeneratorFactoryServer;
    @Autowired
    private SegmentIdService segmentIdService;
    @Autowired
    private TinyIdTokenService tinyIdTokenService;
    @Value("${batch.size.max:10000}")
    private Integer batchSizeMax;

    @GetMapping("nextId")
    public List<Long> nextId(String bizType, Integer batchSize, String token) {
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            throw new BizException(406, ErrorCode.TOKEN_ERR.getMessage(), null);
        }
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            List<Long> ids = idGenerator.nextId(newBatchSize);
            return ids;
        } catch (Exception e) {
            logger.error("nextId error", e);
            throw new BizException(406, e.getMessage(), null);
        }
    }

    private Integer checkBatchSize(Integer batchSize) {
        if (batchSize == null) {
            batchSize = 1;
        }
        if (batchSize > batchSizeMax) {
            batchSize = batchSizeMax;
        }
        return batchSize;
    }

    @RequestMapping("nextIdSimple")
    public String nextIdSimple(String bizType, Integer batchSize, String token) {
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        String response = "";
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            if (newBatchSize == 1) {
                Long id = idGenerator.nextId();
                response = id + "";
            } else {
                List<Long> idList = idGenerator.nextId(newBatchSize);
                StringBuilder sb = new StringBuilder();
                for (Long id : idList) {
                    sb.append(id).append(",");
                }
                response = sb.deleteCharAt(sb.length() - 1).toString();
            }
        } catch (Exception e) {
            logger.error("nextIdSimple error", e);
        }
        return response;
    }

    @RequestMapping("nextSegmentId")
    public SegmentId nextSegmentId(String bizType, String token) {
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            throw new BizException(406, ErrorCode.TOKEN_ERR.getMessage(), null);
        }
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            return segmentId;
        } catch (Exception e) {
            logger.error("nextSegmentId error", e);
            throw new BizException(406, e.getMessage(), null);
        }
    }

    @RequestMapping("nextSegmentIdSimple")
    public String nextSegmentIdSimple(String bizType, String token) {
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        String response = "";
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            response = segmentId.getCurrentId() + "," + segmentId.getLoadingId() + "," + segmentId.getMaxId()
                    + "," + segmentId.getDelta() + "," + segmentId.getRemainder();
        } catch (Exception e) {
            logger.error("nextSegmentIdSimple error", e);
        }
        return response;
    }

}

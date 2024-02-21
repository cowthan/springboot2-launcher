package com.gee.blg.tinyid.base.factory;


import com.gee.blg.tinyid.base.generator.IdGenerator;

/**
 * @author du_imba
 */
public interface IdGeneratorFactory {
    /**
     * 根据bizType创建id生成器
     * @param bizType
     * @return
     */
    IdGenerator getIdGenerator(String bizType);
}

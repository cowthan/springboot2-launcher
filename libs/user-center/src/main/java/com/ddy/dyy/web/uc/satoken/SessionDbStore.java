package com.ddy.dyy.web.uc.satoken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.util.SaFoxUtil;
import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.uc.models.entity.SessionEntity;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.SessionService;
import com.ddy.dyy.web.uc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SessionDbStore
 */
@Component
public class SessionDbStore implements SaTokenDao {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    private void saveOrUpdate2(SessionEntity row) {
        SessionEntity oldRow = getByKey(row.getK());
        if (oldRow == null) {
            sessionService.save(row);
        } else {
            row.setId(oldRow.getId());
            sessionService.updateById(row);
        }
    }

    private SessionEntity getByKey(String key) {
        SessionEntity row = sessionService.getOne(sessionService.lambdaWrapper().eq(SessionEntity::getK, key));
        if (row != null && row.getDieTime() > 0
                && row.getDieTime() < System.currentTimeMillis() / 1000L) {
            sessionService.removeById(row.getId());
            row = null;
        }
        return row;
    }

    /**
     * 获取Value，如无返空
     */
    @Override
    public String get(String key) {
        SessionEntity row = getByKey(key);
        return row == null ? null : row.getV();
    }

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     */
    @Override
    public void set(String key, String value, long timeout) {
        // x-danger-token:login:session:4 ==> {"id":"x-danger-token:login:session:4","createTime":1676994060149,"dataMap":{},"tokenSignList":[{"value":"7fb048f8782542ab897d60fbb6975cbf","device":"default-device"}],"timeout":2592000}
        // x-danger-token:login:token:7fb048f8782542ab897d60fbb6975cbf ==> 4
        // x-danger-token:login:last-activity:7d132df5b1ae44fbaae28d33cf61c46b ==> 1677597081940
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        // 判断是否为永不过期
        SessionEntity row = new SessionEntity();
        row.setK(key);
        row.setV(value);
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            row.setDieTime(-1L);
        } else {
            row.setDieTime(new Date(System.currentTimeMillis() + timeout * 1000L).getTime() / 1000L);
        }

        long userId = 0;
        if (key.contains("login:session")) {
            userId = Lang.toLong(key.split(":")[3]);
        } else if(key.contains("login:token")){
            userId = Lang.toLong(value);
        }
        if(userId > 0){
            UserEntity user = userService.getById(userId);
            row.setAppId(user.getAppId());
        }

        saveOrUpdate2(row);
    }

    /**
     * 修改指定key-value键值对 (过期时间不变)
     */
    @Override
    public void update(String key, String value) {
        long expire = getTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.set(key, value, expire);
    }

    /**
     * 删除Value
     */
    @Override
    public void delete(String key) {
        sessionService.remove(sessionService.lambdaWrapper().eq(SessionEntity::getK, key));
    }

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public long getTimeout(String key) {
        SessionEntity row = getByKey(key);
        if (row != null) {
            return row.getDieTime() - System.currentTimeMillis() / 1000L;
        }
        return 0;
    }

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public void updateTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                this.set(key, this.get(key), timeout);
            }
        } else {
            SessionEntity row = getByKey(key);
            SessionEntity row2 = new SessionEntity();
            row2.setId(row.getId());
            row2.setDieTime(new Date(System.currentTimeMillis() + timeout * 1000L).getTime() / 1000L);
            sessionService.updateById(row2);
        }
    }


    /**
     * 获取Object，如无返空
     */
    @Override
    public Object getObject(String key) {
        SessionEntity row = getByKey(key);
        if (row == null) {
            return null;
        } else {
            return row.getV();
        }
    }

    @Override
    public SaSession getSession(String sessionId) {
        Object obj = getObject(sessionId);
        if (obj == null) {
            return null;
        }
        return JsonUtils.toObj(obj.toString(), SaSession.class);
    }

    /**
     * 写入Object，并设定存活时间 (单位: 秒)
     */
    @Override
    public void setObject(String key, Object object, long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        String toValue = JsonUtils.toJson(object);
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            set(key, toValue, timeout);
        } else {
            set(key, toValue, timeout);
        }
    }

    /**
     * 更新Object (过期时间不变)
     */
    @Override
    public void updateObject(String key, Object object) {
        long expire = getObjectTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.setObject(key, object, expire);
    }

    /**
     * 删除Object
     */
    @Override
    public void deleteObject(String key) {
        delete(key);
    }

    /**
     * 获取Object的剩余存活时间 (单位: 秒)
     */
    @Override
    public long getObjectTimeout(String key) {
        return getTimeout(key);
    }

    /**
     * 修改Object的剩余存活时间 (单位: 秒)
     */
    @Override
    public void updateObjectTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getObjectTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                this.setObject(key, this.getObject(key), timeout);
            }
        } else {
            SessionEntity row = getByKey(key);
            if (row == null) {
                // 如果客户端cookie还在，但session已经手动删了，请求接口尝试刷洗session存活时间时就会出这个错
//                StpUtil.logout();
            }
            SessionEntity row2 = new SessionEntity();
            row2.setId(row.getId());
            row2.setDieTime(new Date(System.currentTimeMillis() + timeout * 1000L).getTime() / 1000L);
            sessionService.updateById(row2);

        }
    }

    /**
     * 搜索数据
     */
    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {

        List<SessionEntity> rows = sessionService.list(
                sessionService.lambdaWrapper()
                        .likeLeft(SessionEntity::getK, prefix)
                        .like(SessionEntity::getK, keyword));
        if (Lang.isEmpty(rows)) {
            return new ArrayList<>();
        } else {
            List<String> list = rows.stream().map(e -> e.getK()).collect(Collectors.toList());
            return SaFoxUtil.searchList(list, start, size, sortType);
        }
    }

}

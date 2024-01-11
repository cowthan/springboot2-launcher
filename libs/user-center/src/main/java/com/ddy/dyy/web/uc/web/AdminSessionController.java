package com.ddy.dyy.web.uc.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import com.ddy.dyy.mybatis.page.PageUtils;
import com.ddy.dyy.web.lang.BeanUtils2;
import com.ddy.dyy.web.lang.JsonUtils;
import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.lang.SpringContext;
import com.ddy.dyy.web.models.Response;
import com.ddy.dyy.web.models.biz.LongVo;
import com.ddy.dyy.web.models.biz.PageList;
import com.ddy.dyy.web.models.biz.PageRequest;
import com.ddy.dyy.web.uc.UserCenterRestController;
import com.ddy.dyy.web.uc.models.RequestData;
import com.ddy.dyy.web.uc.models.SessionAdmin;
import com.ddy.dyy.web.uc.models.SimpleUser;
import com.ddy.dyy.web.uc.models.entity.SessionEntity;
import com.ddy.dyy.web.uc.models.entity.UserEntity;
import com.ddy.dyy.web.uc.service.AuthService;
import com.ddy.dyy.web.uc.service.SessionService;
import com.ddy.dyy.web.uc.service.UserService;
import com.ddy.dyy.web.uc.utils.AuthUtils;
import com.ddy.dyy.web.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@UserCenterRestController("/admin/session")
public class AdminSessionController extends BaseController {
    @Autowired
    private UserService userService;


    @Autowired
    private SessionService sessionService;


    @GetMapping("/list")
    public Response<PageList> list(@Validated SessionAdmin.ListRequest form, @Validated PageRequest pageForm) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        PageUtils.startPage(pageForm);
        List<SessionEntity> list = sessionService.list(sessionService.lambdaWrapper()
                .eq(SessionEntity::getAppId, requestData.getAppId())
                .likeRight(SessionEntity::getK, "x-danger-token:login:token:")
        );
        PageList<SessionEntity> pageData = PageUtils.parse(list);
        PageList<SessionAdmin.ItemVo> ret = new PageList<>();
        ret.setPageSize(pageData.getPageSize());
        ret.setPages(pageData.getPages());
        ret.setTotalCount(pageData.getTotalCount());
        ret.setList(BeanUtils2.copyList(pageData.getList(), SessionAdmin.ItemVo.class));
        fill(pageData.getList(), ret.getList());
        return Response.ok(new PageList(ret.getList(), ret.getTotalCount(), pageForm.getSize()));
    }


    public void fill(List<SessionEntity> rows, List<SessionAdmin.ItemVo> vos) {
        if (Lang.isEmpty(rows)) {
            return;
        }
        // 填充用户信息
        List<Long> userIds = rows.stream().map(e -> Lang.toLong(e.getV())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userIds)) {

            List<UserEntity> authList = SpringContext.getBean(AuthService.class).listByIds(userIds);
            Map<Long, UserEntity> authMap = authList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

            List<String> sessionKeys = rows.stream().map(e -> "x-danger-token:login:session:" + e.getV()).collect(Collectors.toList());
            List<SessionEntity> sessionRows = sessionService.list(sessionService.lambdaWrapper()
                    .in(SessionEntity::getK, sessionKeys));
            Map<Long, SessionEntity> sessionMap = sessionRows.stream()
                    .collect(Collectors.toMap(e -> Lang.toLong(e.getK().split(":")[3]), e -> e));


            int i = -1;
            for (SessionAdmin.ItemVo vo : vos) {
                i++;
                SessionEntity row = rows.get(i);
                long userId = Lang.toLong(row.getV());
                UserEntity auth = authMap.get(userId);
                if (auth != null) {
                    vo.setUser(new SimpleUser(auth.getUid(), auth.getNickname(), auth.getHeadIcon()));
                    vo.setUsername(auth.getUsername());
                }
                vo.setDieTime(new Date(row.getDieTime() * 1000L));
                vo.setToken(row.getK().split(":")[3]);

                SessionEntity sessionRow = sessionMap.get(userId);
                if (sessionRow != null) {
                    SaSession saSession = JsonUtils.toObj(sessionRow.getV(), SaSession.class);
                    for (TokenSign tokenSign : saSession.getTokenSignList()) {
                        if (tokenSign.getValue().equals(vo.getToken())) {
                            vo.setDevice(tokenSign.getDevice());
                        }
                    }
                } else {
                    vo.setDevice("【已失效】");
                }
            }
        }
    }

    //    @OpLog(title = "会话管理", businessType = BusinessType.DELETE)
    @GetMapping("/kickoff")
    public Response<LongVo> delete(@RequestParam String ids) {
        RequestData requestData = AuthUtils.getRequestInfo(true, true);
        List<Long> userIds = Lang.splitToLongList(ids, ",");
        for (Long id : userIds) {
            SessionEntity row = sessionService.getById(id);
//            StpUtil.kickout(row.getV());
            StpUtil.logoutByTokenValue(row.getK().split(":")[3]);
        }
        return Response.ok(LongVo.of(1));
    }

}

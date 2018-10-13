package cn.tz.security.listener;

import cn.tz.common.service.Interface.SystemLogService;
import cn.tz.domain.UserInfoDTO;
import cn.tz.exception.SystemLogServiceException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro Session 监听器
 * 当Shiro 的 session 被创建、注销或过期的时候触发
 *
 * @author tz
 * @since 
 */
public class SessionListener extends SessionListenerAdapter {

    @Autowired
    private SystemLogService systemLogService;

    @Override
    public void onStart(Session session) {
        super.onStart(session);
    }

    @Override
    public void onStop(Session session) {
        super.onStop(session);
        sessionDestroyedLog(session);
    }

    @Override
    public void onExpiration(Session session) {
        super.onExpiration(session);
        sessionDestroyedLog(session);
    }

    /**
     * 当 Shiro Session 被销毁时， 对已登陆的用户记录访问日志
     *
     * @param session 被销毁的 Session
     */
    private void sessionDestroyedLog(Session session) {
        // 判断是否为已经登陆的用户, 判断依据是isAuthentication的值
        UserInfoDTO userInfo = (UserInfoDTO) session.getAttribute("userInfo");
        if (userInfo != null) {
            try {
                // 记录登出日志
                systemLogService.insertAccessRecord(userInfo.getUserID(), userInfo.getUserName(),
                        userInfo.getAccessIP(), SystemLogService.ACCESS_TYPE_LOGOUT);
            } catch (SystemLogServiceException e) {
                // do log
            }
        }
    }
}

package cn.tz.dao;

import cn.tz.domain.RolePermissionDO;

import java.util.List;

/**
 * 角色权限信息 Mapper
 *
 * @author tz
 * @since 
 */
public interface RolePermissionMapper {

    List<RolePermissionDO> selectAll();
}

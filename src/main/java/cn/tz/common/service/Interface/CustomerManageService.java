package cn.tz.common.service.Interface;


import cn.tz.domain.Customer;
import cn.tz.exception.CustomerManageServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 客户信息管理 service
 *
 * @author tz
 */
public interface CustomerManageService {

    /**
     * 返回指定customer id 的客户记录
     *
     * @param customerId 客户ID
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectById(Integer customerId) throws CustomerManageServiceException;

    /**
     * 返回指定 customer name 的客户记录
     * 支持查询分页以及模糊查询
     *
     * @param offset       分页的偏移值
     * @param limit        分页的大小
     * @param customerName 客户的名称
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByName(int offset, int limit, String customerName) throws CustomerManageServiceException;

    /**
     * 返回指定 customer Name 的客户记录
     * 支持模糊查询
     *
     * @param customerName 客户名称
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByName(String customerName) throws CustomerManageServiceException;

    /**
     * 分页查询客户的记录
     *
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectAll(int offset, int limit) throws CustomerManageServiceException;

    /**
     * 查询所有客户的记录
     *
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectAll() throws CustomerManageServiceException;

    /**
     * 添加客户信息
     *
     * @param customer 客户信息
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    boolean addCustomer(Customer customer) throws CustomerManageServiceException;

    /**
     * 更新客户信息
     *
     * @param customer 客户信息
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    boolean updateCustomer(Customer customer) throws CustomerManageServiceException;

    /**
     * 删除客户信息
     *
     * @param customerId 客户ID
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    boolean deleteCustomer(Integer customerId) throws CustomerManageServiceException;

}

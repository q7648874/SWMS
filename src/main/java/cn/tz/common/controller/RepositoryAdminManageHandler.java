package cn.tz.common.controller;

import cn.tz.common.service.Interface.RepositoryAdminManageService;
import cn.tz.common.util.Response;
import cn.tz.common.util.ResponseFactory;
import cn.tz.domain.RepositoryAdmin;
import cn.tz.exception.RepositoryAdminManageServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理员管理请求 Handler
 *
 * @author tz
 */
@Controller
@RequestMapping(value = "/**/repositoryAdminManage")
public class RepositoryAdminManageHandler {

    @Autowired
    private RepositoryAdminManageService repositoryAdminManageService;

    // 查询类型
    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_NAME = "searchByName";
    private static final String SEARCH_BY_REPOSITORY_ID = "searchByRepositoryID";
    private static final String SEARCH_ALL = "searchAll";

    /**
     * 通用记录查询
     *
     * @param keyWord    查询关键字
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @return 返回所有符合条件的记录
     */
    private Map<String, Object> query(String keyWord, String searchType, int offset, int limit) throws RepositoryAdminManageServiceException {
        Map<String, Object> queryResult = null;

        // query
        switch (searchType) {
            case SEARCH_ALL:
                queryResult = repositoryAdminManageService.selectAll(offset, limit);
                break;
            case SEARCH_BY_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = repositoryAdminManageService.selectByID(Integer.valueOf(keyWord));
                break;
            case SEARCH_BY_NAME:
                queryResult = repositoryAdminManageService.selectByName(offset, limit, keyWord);
                break;
            case SEARCH_BY_REPOSITORY_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = repositoryAdminManageService.selectByRepositoryID(Integer.valueOf(keyWord));
                break;
            default:
                // do other things
                break;
        }

        return queryResult;
    }

    /**
     * 查询仓库管理员信息
     *
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @param keyWord    查询关键字
     * @return 返回一个Map，其中key=rows，表示查询出来的记录；key=total，表示记录的总条数
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getRepositoryAdminList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getRepositoryAdmin(@RequestParam("searchType") String searchType,
                                           @RequestParam("keyWord") String keyWord, @RequestParam("offset") int offset,
                                           @RequestParam("limit") int limit) throws RepositoryAdminManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        List<RepositoryAdmin> rows = null;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = query(keyWord, searchType, offset, limit);

        if (queryResult != null) {
            rows = (List<RepositoryAdmin>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

    /**
     * 添加一条仓库管理员信息
     *
     * @param repositoryAdmin 仓库管理员信息
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "addRepositoryAdmin", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addRepositoryAdmin(@RequestBody RepositoryAdmin repositoryAdmin) throws RepositoryAdminManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 添加结果
        String result = repositoryAdminManageService.addRepositoryAdmin(repositoryAdmin)
                ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 查询指定 ID 的仓库管理员信息
     *
     * @param repositoryAdminID 仓库管理员ID
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库管理员信息
     */
    @RequestMapping(value = "getRepositoryAdminInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getRepositoryAdminInfo(Integer repositoryAdminID) throws RepositoryAdminManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        // 查询
        RepositoryAdmin repositoryAdmin = null;
        Map<String, Object> queryResult = repositoryAdminManageService.selectByID(repositoryAdminID);
        if (queryResult != null) {
            if ((repositoryAdmin = (RepositoryAdmin) queryResult.get("data")) != null)
                result = Response.RESPONSE_RESULT_SUCCESS;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseData(repositoryAdmin);
        return responseContent.generateResponse();
    }

    /**
     * 更新仓库管理员信息
     *
     * @param repositoryAdmin 仓库管理员信息
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库管理员信息
     */
    @RequestMapping(value = "updateRepositoryAdmin", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateRepositoryAdmin(@RequestBody RepositoryAdmin repositoryAdmin) throws RepositoryAdminManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 更新
        String result = repositoryAdminManageService.updateRepositoryAdmin(repositoryAdmin)
                ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 删除指定 ID 的仓库管理员信息
     *
     * @param repositoryAdminID 仓库ID
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库管理员信息
     */
    @RequestMapping(value = "deleteRepositoryAdmin", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deleteRepositoryAdmin(Integer repositoryAdminID) throws RepositoryAdminManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 删除记录
        String result = repositoryAdminManageService.deleteRepositoryAdmin(repositoryAdminID)
                ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

}

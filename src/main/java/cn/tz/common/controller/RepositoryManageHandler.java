package cn.tz.common.controller;

import cn.tz.common.service.Interface.RepositoryService;
import cn.tz.common.util.Response;
import cn.tz.common.util.ResponseFactory;
import cn.tz.domain.Repository;
import cn.tz.exception.RepositoryManageServiceException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息管理请求 Handler
 *
 * @author tz
 */
@Controller
@RequestMapping(value = "/**/repositoryManage")
public class RepositoryManageHandler {

    @Autowired
    private RepositoryService repositoryService;

    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_ADDRESS = "searchByAddress";
    private static final String SEARCH_ALL = "searchAll";

    /**
     * 通用的记录查询
     *
     * @param searchType 查询方式
     * @param keyword    查询关键字
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @return 返回所有符合条件的查询结果
     */
    private Map<String, Object> query(String searchType, String keyword, int offset, int limit) throws RepositoryManageServiceException {
        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_ID:
                if (StringUtils.isNumeric(keyword)) {
                    queryResult = repositoryService.selectById(Integer.valueOf(keyword));
                }
                break;
            case SEARCH_BY_ADDRESS:
                queryResult = repositoryService.selectByAddress(offset, limit, keyword);
                break;
            case SEARCH_ALL:
                queryResult = repositoryService.selectAll(offset, limit);
                break;
            default:
                // do other thing
                break;
        }

        return queryResult;
    }

    /**
     * 查询仓库信息
     *
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @param keyWord    查询关键字
     * @return 返回一个Map，其中key=rows，表示查询出来的记录；key=total，表示记录的总条数
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getRepositoryList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getRepositoryList(@RequestParam("searchType") String searchType,
                                          @RequestParam("offset") int offset, @RequestParam("limit") int limit,
                                          @RequestParam("keyWord") String keyWord) throws RepositoryManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        List<Repository> rows = null;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = query(searchType, keyWord, offset, limit);

        if (queryResult != null) {
            rows = (List<Repository>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

    /**
     * 查询所有未指派管理员的仓库
     *
     * @return 返回一个 map，其中key=data表示查询的记录，key=total表示记录的条数
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getUnassignRepository", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getUnassignRepository() throws RepositoryManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<Repository> data;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = repositoryService.selectUnassign();
        if (queryResult != null) {
            data = (List<Repository>) queryResult.get("data");
            total = (long) queryResult.get("total");
        } else
            data = new ArrayList<>();

        resultSet.put("data", data);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 添加一条仓库信息
     *
     * @param repository 仓库信息
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "addRepository", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addRepository(@RequestBody Repository repository) throws RepositoryManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 添加记录
        String result = repositoryService.addRepository(repository) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 查询指定 ID 的仓库信息
     *
     * @param repositoryID 仓库ID
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库信息
     */
    @RequestMapping(value = "getRepositoryInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getRepositoryInfo(@RequestParam("repositoryID") Integer repositoryID) throws RepositoryManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        // 查询
        Repository repository = null;
        Map<String, Object> queryResult = repositoryService.selectById(repositoryID);
        if (queryResult != null) {
            repository = (Repository) queryResult.get("data");
            if (repository != null)
                result = Response.RESPONSE_RESULT_SUCCESS;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseData(repository);
        return responseContent.generateResponse();
    }

    /**
     * 更新仓库信息
     *
     * @param repository 仓库信息
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库信息
     */
    @RequestMapping(value = "updateRepository", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateRepository(@RequestBody Repository repository) throws RepositoryManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 更新
        String result = repositoryService.updateRepository(repository) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 删除指定 ID 的仓库信息
     *
     * @param repositoryID 仓库ID
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为仓库信息
     */
    @RequestMapping(value = "deleteRepository", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deleteRepository(@RequestParam("repositoryID") Integer repositoryID) throws RepositoryManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 删除记录
        String result = repositoryService.deleteRepository(repositoryID) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

}

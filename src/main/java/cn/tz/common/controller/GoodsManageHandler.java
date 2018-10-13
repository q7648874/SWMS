package cn.tz.common.controller;

import cn.tz.common.service.Interface.GoodsManageService;
import cn.tz.common.util.Response;
import cn.tz.common.util.ResponseFactory;
import cn.tz.domain.Goods;
import cn.tz.domain.Supplier;
import cn.tz.exception.GoodsManageServiceException;
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
import java.util.List;
import java.util.Map;

/**
 * 货物信息管理请求 Handler
 *
 * @author tz
 */
@RequestMapping(value = "/**/goodsManage")
@Controller
public class GoodsManageHandler {

    @Autowired
    private GoodsManageService goodsManageService;

    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_NAME = "searchByName";
    private static final String SEARCH_ALL = "searchAll";

    /**
     * 通用的记录查询
     *
     * @param searchType 查询类型
     * @param keyWord    查询关键字
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @return 返回一个 Map ，包含所有符合要求的查询结果，以及记录的条数
     */
    private Map<String, Object> query(String searchType, String keyWord, int offset, int limit) throws GoodsManageServiceException {
        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = goodsManageService.selectById(Integer.valueOf(keyWord));
                break;
            case SEARCH_BY_NAME:
                queryResult = goodsManageService.selectByName(keyWord);
                break;
            case SEARCH_ALL:
                queryResult = goodsManageService.selectAll(offset, limit);
                break;
            default:
                // do other thing
                break;
        }

        return queryResult;
    }

    /**
     * 搜索货物信息
     *
     * @param searchType 搜索类型
     * @param offset     如有多条记录时分页的偏移值
     * @param limit      如有多条记录时分页的大小
     * @param keyWord    搜索的关键字
     * @return 返回所有符合要求的记录
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getGoodsList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getGoodsList(@RequestParam("searchType") String searchType,
                                     @RequestParam("offset") int offset, @RequestParam("limit") int limit,
                                     @RequestParam("keyWord") String keyWord) throws GoodsManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();
        List<Supplier> rows = null;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = query(searchType, keyWord, offset, limit);

        if (queryResult != null) {
            rows = (List<Supplier>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

    /**
     * 添加一条货物信息
     *
     * @param goods 货物信息
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "addGoods", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addGoods(@RequestBody Goods goods) throws GoodsManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 添加记录
        String result = goodsManageService.addGoods(goods) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);

        return responseContent.generateResponse();
    }

    /**
     * 查询指定 goods ID 货物的信息
     *
     * @param goodsID 货物ID
     * @return 返回一个map，其中：key 为 result 的值为操作的结果，包括：success 与 error；key 为 data
     * 的值为货物信息
     */
    @RequestMapping(value = "getGoodsInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getGoodsInfo(@RequestParam("goodsID") Integer goodsID) throws GoodsManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        // 获取货物信息
        Goods goods = null;
        Map<String, Object> queryResult = goodsManageService.selectById(goodsID);
        if (queryResult != null) {
            goods = (Goods) queryResult.get("data");
            if (goods != null) {
                result = Response.RESPONSE_RESULT_SUCCESS;
            }
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseData(goods);
        return responseContent.generateResponse();
    }

    /**
     * 更新货物信息
     *
     * @param goods 货物信息
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "updateGoods", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateGoods(@RequestBody Goods goods) throws GoodsManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 更新
        String result = goodsManageService.updateGoods(goods) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 删除货物记录
     *
     * @param goodsID 货物ID
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "deleteGoods", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deleteGoods(@RequestParam("goodsID") Integer goodsID) throws GoodsManageServiceException {
        // 初始化 Response
        Response responseContent = ResponseFactory.newInstance();

        // 删除
        String result = goodsManageService.deleteGoods(goodsID) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

}

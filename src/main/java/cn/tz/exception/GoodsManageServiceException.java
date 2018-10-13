package cn.tz.exception;

/**
 * GoodsManageService异常
 *
 * @author tz
 * @since 
 */
public class GoodsManageServiceException extends BusinessException {

    GoodsManageServiceException(){
        super();
    }

    public GoodsManageServiceException(Exception e){
        super(e);
    }

    GoodsManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

}

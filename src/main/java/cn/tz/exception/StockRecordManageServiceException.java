package cn.tz.exception;

/**
 * StockRecordManageService异常
 *
 * @author tz
 * @since 
 */
public class StockRecordManageServiceException extends BusinessException {

    public StockRecordManageServiceException(){
        super();
    }

    public StockRecordManageServiceException(Exception e){
        super(e);
    }

    public StockRecordManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public StockRecordManageServiceException(String exceptionDesc){
        super(exceptionDesc);
    }

}

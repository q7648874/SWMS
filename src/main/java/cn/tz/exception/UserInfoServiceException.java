package cn.tz.exception;

/**
 * UserInfoService异常
 *
 * @author tz
 * @since 
 */
public class UserInfoServiceException extends BusinessException {

    public UserInfoServiceException(){
        super();
    }

    public UserInfoServiceException(Exception e){
        super(e);
    }

    public UserInfoServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public UserInfoServiceException(String exceptionDesc){
        super(exceptionDesc);
    }

}

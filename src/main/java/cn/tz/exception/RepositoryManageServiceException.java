package cn.tz.exception;

/**
 * RepositoryManageService异常
 *
 * @author tz
 * @since 
 */
public class RepositoryManageServiceException extends BusinessException {

    RepositoryManageServiceException(){
        super();
    }

    public RepositoryManageServiceException(Exception e){
        super(e);
    }

    RepositoryManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

}

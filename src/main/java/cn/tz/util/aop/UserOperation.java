package cn.tz.util.aop;

import java.lang.annotation.*;

/**
 * 用户操作注解
 * 用于标注用户操作的方法名称
 *
 * @author tz
 * @since 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserOperation {
    String value();
}

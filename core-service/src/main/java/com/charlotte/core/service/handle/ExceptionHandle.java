package com.charlotte.core.service.handle;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import com.charlotte.core.service.entity.ServiceResponse;
import com.charlotte.core.service.enums.BusinessExceptionEnum;
import com.charlotte.core.service.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(ExceptionHandle.class)
public class ExceptionHandle {

    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ServiceResponse handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return ServiceResponse.fail(e);
    }


    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,// 首先根据请求 URL 查找有没有对应的控制器，若没有则会抛该异常，也就是大家非常熟悉的 404 异常。
            HttpRequestMethodNotSupportedException.class,// 若匹配到了（匹配结果是一个列表，不同的是 HTTP 方法不同，如：Get、Post 等），则尝试将请求的 HTTP 方法与列表的控制器做匹配，若没有对应 HTTP 方法的控制器，则抛该异常。
            HttpMediaTypeNotSupportedException.class,// 然后再对请求头与控制器支持的做比较。比如 content-type 请求头，若控制器的参数签名包含注解 @RequestBody，但是请求的 content-type 请求头的值没有包含 application/json，那么会抛该异常（当然，不止这种情况会抛这个异常）。
            MissingPathVariableException.class,// 未检测到路径参数。比如 URL 为：/licence/{licenceId}，参数签名包含 @PathVariable("licenceId")。当请求的 URL 为 /licence，在没有明确定义 URL 为 /licence 的情况下，会被判定为：缺少路径参数。
            MissingServletRequestParameterException.class,// 缺少请求参数。比如定义了参数 @RequestParam("licenceId") String licenceId，但发起请求时，未携带该参数，则会抛该异常。
            TypeMismatchException.class,// 参数类型匹配失败。比如：接收参数为 Long 型，但传入的值确是一个字符串，那么将会出现类型转换失败的情况，这时会抛该异常。
            HttpMessageNotReadableException.class,// 与上面的 HttpMediaTypeNotSupportedException 举的例子完全相反。即请求头携带了"content-type: application/json;charset=UTF-8"，但接收参数却没有添加注解 @RequestBody，或者请求体携带的 json 串反序列化成 pojo 的过程中失败了，也会抛该异常。
            HttpMessageNotWritableException.class,// 返回的 pojo 在序列化成 json 过程失败了，那么抛该异常。
            // BindException.class,
            // MethodArgumentNotValidException.class
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ServiceResponse handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        BusinessExceptionEnum exception = BusinessExceptionEnum.SERVER_ERROR;
//        try {
//            定义具体的SERVER_ERROR
//            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
//            code = servletExceptionEnum.getCode();
//        } catch (IllegalArgumentException e1) {
//            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
//        }
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
            return ServiceResponse.fail(exception);
        }
        return ServiceResponse.fail(exception.getCode(), e.getMessage());
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ServiceResponse handleBindException(BindException e) {
        log.error("参数绑定校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ServiceResponse handleValidException(MethodArgumentNotValidException e) {
        log.error("参数绑定校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private ServiceResponse wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }
        return ServiceResponse.fail(BusinessExceptionEnum.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServiceResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        BusinessExceptionEnum exception = BusinessExceptionEnum.SERVER_ERROR;
        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            return ServiceResponse.fail(exception);
        }
        return ServiceResponse.fail(exception.getCode(), e.getMessage());
    }

}

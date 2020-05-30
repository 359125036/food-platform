package com.zx.exception;


import com.zx.utils.JSONResult;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

/**
 * @ClassName: CustomExceptionHandler
 * @Author: zhengxin
 * @Description: 自定义的异常处理助手类
 * @Date: 2020/5/30 9:05
 * @Version: 1.0
 */
@RestControllerAdvice
/**
 * 在spring3.2中，新增@RestControllerAdvice注解，用于定义
 * @ExceptionHandler、@InitBinder、@ModelAttribute,并应用
 * 到所有@RequestMapping中。
 *
 */
public class CustomExceptionHandler {

    /**
     * @Method handlerMapUploadFile
     * @Author zhengxin
     * @Description 上传文件大于500k时，捕获异常：MaxUploadSizeExceededException
     * @param exception
     * @Return com.zx.utils.JSONResult
     * @Exception
     * @Date 2020/5/30 9:39
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMapUploadFile(MaxUploadSizeExceededException exception){
        return JSONResult.errorMsg("文件上传大小不能超过500k,请压缩图片或降低图片质量！");
    }

}

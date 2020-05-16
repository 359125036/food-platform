package com.zx.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @ClassName: ServiceLogAspect
 * @Description: 体现service层方法运行时长（aop）
 * @Author: zhengxin
 * @Date: 2019/12/23 17:04
 * @Version: 1.0
 */
@Aspect
@Component//组件
public class ServiceLogAspect {
    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);
    /*
     *AOP通知：
     * 1. 前置通知：在方法调用前执行
     * 2. 后置通知：在方法正常调用之后执行
     * 3. 环绕通知：在方法调用之前和之后，都分别可以执行
     * 4. 异常通知：如果在方法调用过程中发送异常，则通知
     * 5. 最终通知： 在方法调用之后执行
     */

    /**
     * @Method recordTimeLog
     * @Author zhengxin
     * @Version 1.0
     * @Description 环绕执行com.zx.service.impl包下的所有方法
     * @Param 切面表达式：
     * execution 代表所要执行的表达式主体
     * 第一处 * 代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及其子包下的所有类方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
     * @Return java.lang.Object
     * @Exception
     * @Date 2019/12/23 17:20
     */
    @Around("execution(* com.zx.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) {
        log.info("=== 开始执行 {}.{} ===",
                joinPoint.getTarget().getClass(),//执行的类名
                joinPoint.getSignature().getName());//执行的方法名
        // 记录开始时间
        long begin = System.currentTimeMillis();
        // 执行目标 service
        Object result = null;
        /**
         *
         * 调用执行并捕获异常获取@Transactional事务回滚失效
         * 默认情况下基于Spring AOP的事务管理，即声明式事务管理，
         * 默认是针对RuntimeException回滚；try/catch情况下如果没有
         * 添加throw new RuntimeException();则事务不会回滚
         */
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException();
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if (takeTime > 3000) {
            log.error("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else {
            log.info("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        }
        return result;
    }

}

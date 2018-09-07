package com.ctosb.tool.cost;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * spring bean方法统计
 * @author alan
 * @date 2018/9/7 13:29
 */
@Aspect
@Component
public class SpringCost {

    @Pointcut("execution(public * com.paic.icore.css.rule.console.mapper.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        String methosName = point.getSignature().toShortString();
        return Costs.execute(methosName, new Costs.Callback() {

            @Override
            public Object call() throws Throwable {
                return point.proceed(args);
            }
        });
    }
}
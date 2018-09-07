package com.ctosb.tool.cost;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis执行统计
 * @author alan
 * @date 2018/9/7 13:15
 */
@Configuration
public class MybatisCost {

    @Bean
    public StatementInterceptor statementInterceptor() {
        StatementInterceptor statementInterceptor = new StatementInterceptor();
        return statementInterceptor;
    }

    @Bean
    public ParameterInterceptor parameterInterceptor() {
        return new ParameterInterceptor();
    }

    @Bean
    public ResultSetInterceptor resultSetInterceptor() {
        return new ResultSetInterceptor();
    }

    /**
     * 查询拦截
     * @author alan
     * @date 2018/9/7 13:25
     */
    @Intercepts({ @Signature(type = StatementHandler.class, method = "query", args = { Statement.class,
            ResultHandler.class }) })
    private class StatementInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            String methodName = getShortMethodName(invocation);
            return Costs.execute(methodName, new Costs.Callback() {

                @Override
                public Object call() throws Throwable {
                    return invocation.proceed();
                }
            });
        }

        private String getShortMethodName(Invocation invocation) {
            Method method = invocation.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            return className + "." + method.getName() + "()";
        }

        @Override
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {
        }
    }

    /**
     * 设置参数拦截
     * @author alan
     * @date 2018/9/7 13:25
     */
    @Intercepts({ @Signature(type = StatementHandler.class, method = "parameterize", args = { Statement.class }) })
    private class ParameterInterceptor extends StatementInterceptor {
    }

    /**
     * 转换结果集拦截
     * @author alan
     * @date 2018/9/7 13:25
     */
    @Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
    private class ResultSetInterceptor extends StatementInterceptor {
    }
}
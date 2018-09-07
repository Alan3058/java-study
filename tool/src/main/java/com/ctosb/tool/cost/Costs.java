package com.ctosb.tool.cost;


        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

/**
 * 统计类
 * @author alan
 * @date 2018/9/7 13:21
 */
public class Costs {

    private static Logger LOGGER = LoggerFactory.getLogger(Costs.class);

    public static Object execute(String methodName, Callback callback) throws Throwable {
        LOGGER.info("---------开始{}方法---------", methodName);
        long fm = System.currentTimeMillis();
        try {
            return callback.call();
        } catch (Throwable e) {
            throw e;
        } finally {
            long to = System.currentTimeMillis();
            LOGGER.info("---------结束{}方法,耗时[{}]s---------", methodName, (to - fm) / 1000.0);
        }
    }

    /**
     * 回调接口
     * @author alan
     * @date 2018/9/7 13:26
     */
    public interface Callback {

        Object call() throws Throwable;
    }
}
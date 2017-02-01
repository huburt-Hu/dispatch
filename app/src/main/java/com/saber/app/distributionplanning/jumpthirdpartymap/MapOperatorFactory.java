package com.saber.app.distributionplanning.jumpthirdpartymap;

/**
 * 地图操作类工厂，使用此类获取指定地图操作类
 *
 * @author ex-huxibing552
 * @date 2017-01-18 17:01
 */
public class MapOperatorFactory {

    public static <T extends IMapOperator> T getOperator(Class<T> clazz) {
        IMapOperator operator = null;
        try {
            operator = (IMapOperator) Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) operator;
    }
}

package com.example.annotation.annoAndreflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnoTest {

    /**
     * 这个方法会将一段文本注入到某个类中添加了@Autowired注解的方法中，并将实例对象返回
     * @param className
     * @param str
     * @return
     * @throws ClassNotFoundException
     */
    public static Object injectStrToInstance(String className, String str)throws ClassNotFoundException{
        //获取AnnoDemo的Class对象
        Class demoClass = Class.forName(className);
        // 从Class对象中获取Demo中生命方法对应的Method对象
        Method[] methods = demoClass.getDeclaredMethods();
        for (Method method :methods) {
            // 判断方法是否被加上了@Autowired这个注解
            if (method.isAnnotationPresent(Autowiredd.class)) {
                // 将方法设置为空调用的
                method.setAccessible(true);
                try {
                    Object object = demoClass.newInstance();
                    // 调用method方法，向其中注入str字符串
                    method.invoke(object,str);
                    return object;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // 进行依赖注入，并取得注入后的AnnoDemo的对象实例
        AnnoDemo annoDemo = (AnnoDemo) injectStrToInstance("com.example.annotation.annoAndreflect.AnnoDemo", "this is 被注入的文本");
        // 输出一下看看我们的文本是不是被成功注入进去了
        System.out.println(annoDemo.getStr());
    }

}

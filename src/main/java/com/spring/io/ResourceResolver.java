package com.spring.io;

import com.spring.log.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;

/**
 * spring源文档对资源解释器的描述：
 *      一种用于解析对服务器端资源的请求的策略。
 *      提供用于将传入请求解析为实际 Resource 和获取 客户端在请求资源时应使用的公共 URL 路径。
 *
 * demo中的实现：
 * 扫描器扫描指定路径下的.class或者property文件
 * 1.兼容win,unix,mac环境下的路径
 * 2.递归查找，实在找不到则返回null
 * 3.分别支持在目录中和jar包中搜索，file:xxxxx  jar:xxxxx
 * 4.web应用使用servlet提供的classloader，比如tomcat架构的/WEB-INF/classes目录和/WEB-INF/lib。要先从线程上下文去找，找不到再从当前类去找。
 *
 * @author Zyra
 * @date 2024/11/12
 */
public class ResourceResolver {

    private Logger logger = Logger.getInstance();
    private String basePackage;

    public ResourceResolver(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }


    public <R> List<R> scan(Function<Resource, R> mapper) {


        return null;
    }



    /**
     * 扫描指定包下的所有类
     *
     * @param packageName 包名
     * @return 类列表
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        // 获取当前线程的ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 将包名转换为路径格式
        String path = packageName.replace('.', '/');

        // 获取资源路径下所有的URL
        Enumeration<URL> resources = classLoader.getResources(path);

        List<Class<?>> classes = new ArrayList<>();

        // 遍历所有资源
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File f = new File(resource.getFile());

            // 如果是目录，则遍历该目录中的所有文件
            if (f.isDirectory()) {
                scanDirectory(f, packageName, classes);
            }
        }

        return classes;
    }






    /**
     * 递归扫描目录下的类文件
     *
     * @param directory 当前目录
     * @param packageName 包名
     * @param classes 存储类的列表
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes) {
        // 获取目录下所有文件
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是子目录，递归扫描
                    scanDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    // 如果是.class文件，加载它
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6); // 去掉".class"
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ClassLoader getClassLoader(){
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        return cl;
    }

//    public static void main(String[]ss){
//        ResourceResolver rr = new ResourceResolver("test");
//        ClassLoader cl1 = rr.getClassLoader();
//        System.out.println(cl1);
//
//        ClassLoader cl2 = Thread.currentThread().getContextClassLoader();
//        System.out.println(cl2);
//
//        ClassLoader cl3 = rr.getClass().getClassLoader();
//        System.out.println(cl3);
//    }

    public static void main(String[] ss){
        try {
            List<Class<?>> classes = ResourceResolver.scanClasses("com.spring.io");
            for(Class<?>c:classes){
                System.out.println(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

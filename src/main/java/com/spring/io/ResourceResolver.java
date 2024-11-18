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
 * springԴ�ĵ�����Դ��������������
 *      һ�����ڽ����Է���������Դ������Ĳ��ԡ�
 *      �ṩ���ڽ������������Ϊʵ�� Resource �ͻ�ȡ �ͻ�����������ԴʱӦʹ�õĹ��� URL ·����
 *
 * demo�е�ʵ�֣�
 * ɨ����ɨ��ָ��·���µ�.class����property�ļ�
 * 1.����win,unix,mac�����µ�·��
 * 2.�ݹ���ң�ʵ���Ҳ����򷵻�null
 * 3.�ֱ�֧����Ŀ¼�к�jar����������file:xxxxx  jar:xxxxx
 * 4.webӦ��ʹ��servlet�ṩ��classloader������tomcat�ܹ���/WEB-INF/classesĿ¼��/WEB-INF/lib��Ҫ�ȴ��߳�������ȥ�ң��Ҳ����ٴӵ�ǰ��ȥ�ҡ�
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
     * ɨ��ָ�����µ�������
     *
     * @param packageName ����
     * @return ���б�
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        // ��ȡ��ǰ�̵߳�ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // ������ת��Ϊ·����ʽ
        String path = packageName.replace('.', '/');

        // ��ȡ��Դ·�������е�URL
        Enumeration<URL> resources = classLoader.getResources(path);

        List<Class<?>> classes = new ArrayList<>();

        // ����������Դ
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File f = new File(resource.getFile());

            // �����Ŀ¼���������Ŀ¼�е������ļ�
            if (f.isDirectory()) {
                scanDirectory(f, packageName, classes);
            }
        }

        return classes;
    }






    /**
     * �ݹ�ɨ��Ŀ¼�µ����ļ�
     *
     * @param directory ��ǰĿ¼
     * @param packageName ����
     * @param classes �洢����б�
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes) {
        // ��ȡĿ¼�������ļ�
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // �������Ŀ¼���ݹ�ɨ��
                    scanDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    // �����.class�ļ���������
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6); // ȥ��".class"
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

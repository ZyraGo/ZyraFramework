package com.spring.log;

public class Logger {
    // 1. 创建 Logger 的单例实例
    private static Logger instance;

    // 2. 构造器私有化，防止外部实例化
    private Logger() {}

    // 3. 获取单例实例
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    // 4. 打印日志的方法
    public void log(String message) {
        // 获取当前时间戳
        String timestamp = java.time.LocalDateTime.now().toString();
        System.out.println("[" + timestamp + "] " + message);
    }

    // 5. 可以扩展更多日志级别的功能
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }
}

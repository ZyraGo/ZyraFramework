package com.spring.log;

public class Logger {
    // 1. ���� Logger �ĵ���ʵ��
    private static Logger instance;

    // 2. ������˽�л�����ֹ�ⲿʵ����
    private Logger() {}

    // 3. ��ȡ����ʵ��
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

    // 4. ��ӡ��־�ķ���
    public void log(String message) {
        // ��ȡ��ǰʱ���
        String timestamp = java.time.LocalDateTime.now().toString();
        System.out.println("[" + timestamp + "] " + message);
    }

    // 5. ������չ������־����Ĺ���
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

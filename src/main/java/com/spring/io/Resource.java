package com.spring.io;

/**
 *
 * @author Zyra
 * @date 2024/11/12
 */
public class Resource {
    private String name;
    private String path;

    public Resource(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

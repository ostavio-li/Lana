package com.edwardlee.library.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author EdwardLee
 * @date 2020/11/2
 */
public class PropertyReader {

    private Properties properties = null;

    public PropertyReader() {
        this.properties = new Properties();

        try {
            this.properties.load(new FileInputStream("E:\\IDEAProjects\\library\\src\\main\\resources\\static\\conf.properties"));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public String readProperty(String key) {
        return this.properties.getProperty(key);
    }

}

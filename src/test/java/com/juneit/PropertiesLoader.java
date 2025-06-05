package com.juneit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    public final Properties properties = getResource ();
    public final String baseUrl = properties.getProperty ("base-url");
    public final String username = properties.getProperty ("username");
    public final String password = properties.getProperty ("password");
    public final String gmailAccount = properties.getProperty ("gmail-account");
    public final String gmailPassword = properties.getProperty ("gmail-password");


    private Properties getResource () {
        try {
            String path = this.getClass ().getClassLoader ().getResource ("config.properties").getPath ();
            Properties appProps = new Properties ();
            appProps.load (new FileInputStream (path));
            return appProps;
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

}

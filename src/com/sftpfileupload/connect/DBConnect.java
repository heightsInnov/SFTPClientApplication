/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sftpfileupload.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author aojinadu
 */
public class DBConnect {
	static private String user = null;
	static private String driver = null;
	static private String url = null;
	static private String pass = null;
	static PBEncrytor en = new PBEncrytor();
	private static void setConnDetails() {
		user = en.PBDecrypt(getPropertiesValue("DB_USER"));
		pass = en.PBDecrypt(getPropertiesValue("DB_PASS"));
		driver = getPropertiesValue("DB_DRIVER");
		url = getPropertiesValue("DB_CONNECTION");
	}
	
	public static Connection getConn() {
		setConnDetails();
        Connection connection = null;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

	public static String getPropertiesValue(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		String retValue = "";
		String config_path = "C:\\sftpUpload" + File.separator + "sftpUpload.properties";
		try {
			input = new FileInputStream(config_path);
			prop.load(input);
			retValue = prop.getProperty(key);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
					prop.clear();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return retValue;
	}
}

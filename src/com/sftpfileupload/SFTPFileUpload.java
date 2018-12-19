/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sftpfileupload;

import java.io.Console;

/**
 *
 * @author aojinadu
 */
public class SFTPFileUpload {

	/**
	 * @param args the command line arguments
	 */
	static Console console = null;

	public static void main(String[] args) throws Exception {
		// TODO code application logic here
		try {
			console = System.console();
			while (1 == 1) {
				process.mains();
				console.readLine();
				Thread.sleep(300000);
			}
		} catch (Exception e) {
		}
	}

}

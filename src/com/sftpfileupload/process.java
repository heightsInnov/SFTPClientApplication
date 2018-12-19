/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sftpfileupload;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sftpfileupload.connect.DBConnect;
import com.sftpfileupload.connect.PBEncrytor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author aojinadu
 */
public class process {

	static JSch jsch = null;
	static PBEncrytor pb = new PBEncrytor();

	public static void DownloadFiles(String filecopy, String destfilename) {
		jsch = new JSch();
		Session session = null;
		Channel channel = null;
		ChannelSftp sftpChannel = null;
		try {
			session = jsch.getSession(pb.PBDecrypt(DBConnect.getPropertiesValue("SOURCE_USER")), pb.PBDecrypt(DBConnect.getPropertiesValue("SOURCE_SER")), 22);
			session.setPassword(pb.PBDecrypt(DBConnect.getPropertiesValue("SOURCE_PASS")));
			Properties prop = new Properties();
			prop.put("StrictHostKeyChecking", "no");
			session.setConfig(prop);
			session.connect();
			System.out.println("Host Connected on upload server 0.0.0.0 : 22");

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.get(filecopy, DBConnect.getPropertiesValue("ROOT_DIR") + destfilename);
			System.out.println("Download Successful, proceeding to uploading to server...");
			sftpChannel.exit();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			sftpChannel.exit();
			System.out.println("download sftp channel exited.");
			channel.disconnect();
			System.out.println("download channel disconnected.");
			session.disconnect();
			System.out.println("download host session disconnected.");
		}
	}

	public static boolean UploadFiles(String destFilename) throws Exception {
		jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = null;
		Channel channel = null;
		String absolutePath = "/ZFS/ubslive/Upload/DEUBN/incoming/ready";
		boolean resp = false;
		try {
			session = jsch.getSession(pb.PBDecrypt(DBConnect.getPropertiesValue("DEST_USER")), pb.PBDecrypt(DBConnect.getPropertiesValue("DEST_SER")), 22);
			session.setPassword(pb.PBDecrypt(DBConnect.getPropertiesValue("DEST_PASS")));
			Properties prop = new Properties();
			prop.put("StrictHostKeyChecking", "no");
			session.setConfig(prop);
			session.connect();
			System.out.println("Host Connected on upload server 0.0.0.0 : 22");

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(absolutePath);
			File file = new File(DBConnect.getPropertiesValue("ROOT_DIR") + destFilename);
			sftpChannel.put(new FileInputStream(file), file.getName());
			sftpChannel.chmod(0777, absolutePath + "/" + destFilename);
			System.out.println("Successful Upload...");
			sftpChannel.exit();
			session.disconnect();
			resp = true;
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			sftpChannel.exit();
			System.out.println("upload sftp channel exited.");
			channel.disconnect();
			System.out.println("upload channel disconnected.");
			session.disconnect();
			System.out.println("upload host session disconnected.");
		}
		return resp;
	}

	public static void Idle() throws Exception {
		while (1 == 1) {
			File sourceFile = new File(
					"C:\\Users\\aojinadu\\Documents\\Documents\\SFTPClient\\upload");

			if (!sourceFile.exists()) {
				System.out.println("Source Folder Not Found!");
			}

			File[] directoryListing = sourceFile.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					// Do something with child

					File destFile = new File(
							"C:\\Users\\aojinadu\\Documents\\Documents\\SFTPClient\\download\\" + child.getName());

					if (!destFile.exists()) {
						try {
							destFile.createNewFile();
							System.out.println("Creating Files...");

							FileChannel source = null;
							FileChannel destination = null;

							try {
								source = new FileInputStream(child).getChannel();

								destination = new FileOutputStream(destFile).getChannel();

								if (destination != null && source != null) {
									destination.transferFrom(source, 0, source.size());
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								if (source != null) {
									try {
										source.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								if (destination != null) {
									try {
										destination.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						child.delete();
					}
				}
			} else {
				System.out.println("No files in directory at the moment.");
			}
			Thread.sleep(5000);
		}
	}

	public static void mains() {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<GefuData> listed = new ArrayList<GefuData>();
		try {
			conn = DBConnect.getConn();
//			pst = conn.prepareStatement("SELECT * FROM apps.xxubn_gefu_files where FILE_ID = '283'");
			pst = conn.prepareStatement("SELECT * FROM apps.xxubn_gefu_files where PROC_FLAG = 'N'");
			rs = pst.executeQuery();

			while (rs.next()) {
				GefuData gefu = new GefuData();
				gefu.setFullpath(rs.getString("FULL_PATH"));
				gefu.setFilepath(rs.getString("FILE_PATH"));
				gefu.setFilename(rs.getString("FILE_NAME"));
				gefu.setDestname(rs.getString("DEST_FILE_NAME"));

				System.out.println(gefu.getFullpath());
				System.out.println(gefu.getFilepath());
				System.out.println(gefu.getFilename());
				System.out.println(gefu.getDestname());

				listed.add(gefu);
			}
			if(listed.size() > 0){
				System.out.println(LocalTime.now() +" >> " +listed.size() + "  records to be processed");
			for (GefuData gf : listed) {
				System.out.println("Downloading files from server on .103");
				process.DownloadFiles(gf.getFullpath(), gf.getDestname());

				System.out.println("Uploading files to server on .55");
				if (process.UploadFiles(gf.getDestname())) {
					pst = conn.prepareStatement("UPDATE apps.xxubn_gefu_files SET PROC_FLAG = 'P' WHERE DEST_FILE_NAME = '" + gf.getDestname()+"'");
					System.out.println("UPDATE apps.xxubn_gefu_files SET PROC_FLAG = 'P' WHERE DEST_FILE_NAME = '" + gf.getDestname()+"'");
					if (pst.executeUpdate() != 0) {
						System.out.println("Database table successfully update..");
						System.out.println("Process successfully done..");
						System.out.println("Database connection successfully closed..");
						File file = new File(DBConnect.getPropertiesValue("ROOT_DIR")+gf.getDestname());
						file.delete();
					}
				}
			}
		}else{
				System.out.println(LocalTime.now() +" >> No record in database at this time");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();

				e.printStackTrace();
			}
		}
	}
}

class GefuData {

	String filepath;
	String fullpath;
	String filename;
	String destname;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFullpath() {
		return fullpath;
	}

	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDestname() {
		return destname;
	}

	public void setDestname(String destname) {
		this.destname = destname;
	}

}

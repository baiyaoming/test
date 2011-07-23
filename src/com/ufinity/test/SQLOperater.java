//-------------------------------------------------------------------------
// Copyright (c) 2000-2006 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author: bym
//
//-------------------------------------------------------------------------
// UFINITY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
// THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UFINITY SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
// MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
// THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
// CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
// PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
// NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
// SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
// SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
// PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). UFINITY
// SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
// HIGH RISK ACTIVITIES.
//-------------------------------------------------------------------------

package com.ufinity.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

public class SQLOperater {

	public String fetchFileByBytes(String path) {
		String sql = "select * from blob_table";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		File file = null;
		OutputStream fos = null;
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			byte[] buffer = null;

			file = new File(path);
			fos = new FileOutputStream(file);
			while (rs.next()) {
				buffer = rs.getBytes("file");
				fos.write(buffer);
			}
			fos.flush();
			fos.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//Util.close(null, ps, conn);
		}
		return null;
	}

	public String fetchFileByBlob(String path) {
		String sql = "select * from blob_table ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		File file = null;
		OutputStream fos = null;
		try {
			conn = getConn();
			conn.setAutoCommit(false);
			// 获取大对象管理器以便进行操作
			LargeObjectManager lobj = ((org.postgresql.PGConnection) conn)
					.getLargeObjectAPI();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			byte[] buffer = null;

			file = new File(path);
			fos = new FileOutputStream(file);
			while (rs.next()) {
				Long oid = rs.getLong("file");
				LargeObject obj = lobj.open(oid, LargeObjectManager.READ);

				buffer = new byte[obj.size()];
				obj.read(buffer, 0, obj.size());
				fos.write(buffer);
				obj.close();
			}
			fos.flush();
			fos.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//Util.close(null, ps, conn);

		}

		return null;
	}

	public String saveFileByBlob(String path) {
		Connection conn = null;
		PreparedStatement ps = null;
		File file = null;
		FileInputStream fis = null;
		try {
			int id = 33;
			String patht = "c:\\pbs\\avata\\"+ id +".jpg";
			file = new File(patht);
			fis = new FileInputStream(file);
			System.out.println(file.length());
			String sql = "insert into SPHPBSImage(id,imageblob,imagetype) values(?,?,?)";
			conn = getConn();
			conn.setAutoCommit(false);
			PGConnection pgCon = (PGConnection) conn;
			// 获取大对象管理器以便进行操作
			LargeObjectManager lobj = pgCon.getLargeObjectAPI();
			// LargeObjectManager lobj =new LargeObjectManager((BaseConnection)
			// conn);
			// // 创建一个新的大对象
			int oid = lobj.create(LargeObjectManager.READ | LargeObjectManager.WRITE);
			System.out.println("oid:" + oid);
			// 打开一个大对象进行写
			LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
			byte buf[] = new byte[(int) file.length()];
			int s, tl = 0;
			while ((s = fis.read(buf, 0, (int) file.length())) > 0) {
				obj.write(buf, 0, s);
				tl += s;
			}

			obj.close();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, oid);
			ps.setString(3, "icon");
			//ps.setInt(4, oid);
			ps.executeUpdate();
			conn.commit();
			System.out.println(ps.executeUpdate());
			System.out.println("插入成功！");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//Util.close(null, ps, conn);
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String saveFileByByteas(String path) {
		Connection conn = null;
		PreparedStatement ps = null;
		File file = null;
		FileInputStream fis = null;
		InputStream is = null;
		try {
			int id = 36;
			String patht = "c:\\pbs\\avata\\"+ id +".jpg";
			file = new File(patht);
			fis = new FileInputStream(file);
			is = new BufferedInputStream(fis);
			System.out.println(file.length());
			String sql = "insert into SPHPBSImage(id,imageblob,imagetype) values(?,?,?)";
			conn = getConn();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, file.getName());
			ps.setString(3, file.getAbsolutePath());
			ps.setBinaryStream(4, fis, (int) file.length());
			ps.executeUpdate();
			System.out.println("插入成功！");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//close(null, ps, conn);
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static void main(String[] args) {
		SQLOperater op = new SQLOperater();
		String path = "c:\\pbs\\template6.jpg";
		//String path2 = "e://zip//myzipfromdb.zip";
		// op.saveFileByByteas(path);
		// op.fetchFileByBytes(path2);
		op.saveFileByBlob(path);
		//op.fetchFileByBlob(path2);

	}

	public static Connection getConn() {
		String url = "jdbc:postgresql://192.168.1.14/RTV";
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, "corem", "corem");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}

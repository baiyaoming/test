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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description of the class
 * 
 * @author bym
 * @version 1.0
 * @since Jul 20, 2011
 */

public class Main {
	public static void main(String argv[]) {
				
		String imagePath = "c:\\pbs\\template1.jpg";
		String url = "jdbc:postgresql://192.168.1.14/RTV";
			try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(url, "corem", "corem");
			//String sql = "insert into SPHPBSImage(id,imageblob,imagetype) values (?,?) ";
			String sql = "insert into SPHPBSImage(id,imagetype) values (?,?) ";
			PreparedStatement ps = con.prepareStatement(sql);
			byte[] image = read(imagePath);
			//is = new FileInputStream(imagePath);
			ps.setInt(1, 12);
			//ps.setBytes(2, image);
			ps.setString(2, "template");
			ps.executeUpdate();
			ps.close();
			//ps.setBlob(1, image);
			//ps.setBinaryStream(1, image, l2);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*con.setCatalog("mybase ");
		String sqlin = "insert into picture(picname) values (?) ";
		File file = new File(fname);
		long l1 = file.length();
		int l2 = (int) l1;
		try {
			FileInputStream fis = new FileInputStream(file);
			PreparedStatement ps = con.prepareStatement(sqlin);

			ps.setBinaryStream(1, fis, l2);
			ps.executeUpdate();
			ps.close();
			fis.close();
		} catch (SQLException e) {
			System.out.println(e);
		}*/
	}
	
	public static byte[] read(String pathStr){
		InputStream is = null;
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       try {
	           is = new FileInputStream(pathStr);// pathStr 文件路径
	           byte[] b = new byte[1024];
	           int n;
	           while ((n = is.read(b)) != -1) {
	               out.write(b, 0, n);
	           }// end while
	       } catch (Exception e) {
	           //log.error(getText("TimingMmsService.error") + e.getMessage());
	           //throw new Exception("System error,SendTimingMms.getBytesFromFile", e);
	       } finally {
	           if (is != null) {
	               try {
	                   is.close();
	               } catch (Exception e) {
	                   //log.error(e);// TODO
	               }// end try
	           }// end if
	       }// end try

	       return out.toByteArray();

	}
}

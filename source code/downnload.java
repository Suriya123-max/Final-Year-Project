/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * A servlet that retrieves a file from MySQL database and lets the client
 * downloads the file.
 * @author www.codejava.net
 */
@WebServlet("/download")
public class downnload extends HttpServlet {
 
    // size of byte buffer to send file
    private static final int BUFFER_SIZE = 4096;   
     
    // database connection settings
   
     
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // get upload id from URL's parameters\
         HttpSession session = request.getSession();
         String fid = session.getAttribute("dowfileid").toString();
         System.out.println("RRRRRRRRRR"+fid);
//          String usernmae = session.getAttribute("unames").toString();
//           String uidd = session.getAttribute("uidd").toString();
         Integer id=Integer.parseInt(fid);
         
        Connection conn = null; // connection to the database
         Statement st = null;
        try {
            // connects to the database
           
            conn =  Dbconnection.getConnection();;
                     st = conn.createStatement();
            // queries the database
            String sql = "SELECT * FROM ownerfilee WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            String fileName =null;
            String encry =null;
 
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                // gets file name and file blob data
                fileName = result.getString("filename");
                encry = result.getString("encryptf");
                Blob blob = result.getBlob("files");
                InputStream inputStream = blob.getBinaryStream();
                int fileLength = inputStream.available();
                 
                System.out.println("fileLength = " + fileLength);
                    String decstr = new BlowfishAlgorithm().decrypt(encry);
                    System.out.println("*****"+decstr);
                ServletContext context = getServletContext();
 
                // sets MIME type for the file download
                String mimeType = context.getMimeType(fileName);
                if (mimeType == null) {        
                    mimeType = "application/octet-stream";
                } int i=3;
//               if(i>=1){
//                   Calendar cal = Calendar.getInstance();
//            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
//		String report= format.format(cal.getTime());
//                int j = st.executeUpdate("insert into downloaduser(usreid,username,filename,date)values('"+uidd+"','"+usernmae+"','"+fileName+"','"+report+"')"); 
//            }
                 
                // set content properties and header attributes for the response
                response.setContentType(mimeType);
                response.setContentLength(fileLength);
               response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".txt\"");
               
 
                // writes the file to the client
                OutputStream outStream = response.getOutputStream();
                 
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                 
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                 
                inputStream.close();
                outStream.close();             
            } else {
                // no file found
                response.getWriter().print("File not found for the id: " + id);  
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.getWriter().print("SQL Error: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            response.getWriter().print("IO Error: " + ex.getMessage());
        } finally {
            if (conn != null) {
                // closes the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }          
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
 
/**
 *
 * @author welcome
 */
@WebServlet("/owneereg")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class ownereg extends HttpServlet {
     
    // database connection settings
  
     
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    try {                
        
    
    //String id = request.getParameter("id");
    String usernamee = request.getParameter("username");
    String passs = request.getParameter("pass");
   
    String emaild = request.getParameter("email");
    
    String gender = request.getParameter("gen");
    String phone = request.getParameter("phone");
    String location = request.getParameter("location");
   
  
  
InputStream inputStream = null;

Part filePart = request.getPart("profile");
if (filePart != null) {

    System.out.println(filePart.getName());
    System.out.println(filePart.getSize());
    System.out.println(filePart.getContentType());

    inputStream = filePart.getInputStream();
}
 
Connection con = Dbconnection.getConnection();
    Statement st3 = con.createStatement();
  

 
try {

           
    
    int i =3;
if (i>=1){
    
        String sql = "Insert into ownreg(usernmae, password, mailid, mobilenumber, gender, location, status,keyss,uimage) values (?,?,?,?,?,?,?,?,?)";
    PreparedStatement statement = con.prepareStatement(sql);
   
    
    statement.setString(1, usernamee);
    statement.setString(2, passs);
    statement.setString(3, emaild);
    statement.setString(4, phone);
    statement.setString(5,gender);
    statement.setString(6, location);
    statement.setString(7, "waiting");
    statement.setString(8, "Nogenerated");
   
            
   
     
    if (inputStream != null) {
        statement.setBlob(9, inputStream);
    }
    
    int row = statement.executeUpdate();
    if (row > 0) {
        
         response.sendRedirect("Owner_reg.jsp?umssuc=userregister");
        
        
    }
    else{
      response.sendRedirect("Owner_reg.jsp?umsfail=failed");
                
       
        
    }
}
        
} catch (SQLException ex) {
    ex.printStackTrace();
} 
} catch (SQLException ex) {
            Logger.getLogger(ownereg.class.getName()).log(Level.SEVERE, null, ex);
    } 
    }
}
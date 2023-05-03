/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.oreilly.servlet.MultipartRequest;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author java4
 */
public class fileupload extends HttpServlet {

    File file;
    final String filepath = "E:/encfiles/";
PreparedStatement pstm = null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            MultipartRequest m = new MultipartRequest(request, filepath);
                  HttpSession session = request.getSession(true);
            File file = m.getFile("file");
            String filename = file.getName().toLowerCase();
           //String test = "http://www.example.com/abc?page=6";
           
            Connection con = Dbconnection.getConnection();
                      BufferedReader br = new BufferedReader(new FileReader(filepath + filename));
            StringBuffer sb = new StringBuffer();
            String temp = null;

            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            String str = sb.toString();
            System.out.println("File in SB :" + sb.toString());
            int size = (int) file.length();
            System.out.println("File Size :" + size);
             String ownernmae = (String) request.getSession().getAttribute("ownername");
             String oidd = (String) request.getSession().getAttribute("ownerid");
            
            String fname=m.getParameter("fname");
            String descrip=m.getParameter("descrip");
            String akey1=m.getParameter("akey1");
            String akey2=m.getParameter("akey2");
            String akey3=m.getParameter("akey3");
            String filetypes = m.getParameter("ftype");

            
            
            
              
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		String report= format.format(cal.getTime());
           
          
            System.out.println("File Size :" + size);
                  FileInputStream fiss = null;
                  fiss = new FileInputStream(file);
                                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                                keyGen.init(128);
                                SecretKey secretKey = keyGen.generateKey();
                                System.out.println("secret key:" + secretKey);
                                //converting secretkey to String
                                byte[] be = secretKey.getEncoded();//encoding secretkey
                                String skey = Base64.encode(be);
                                System.out.println("converted secretkey to string:" + skey);
                                String encstr = new BlowfishAlgorithm().encrypt(str);
                                System.out.println(str);
//                                String decry = new BlowfishAlgorithm().decrypt(str);
//                                 System.out.println("decc Text :" + decry);
                                
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss ");
            Date date = new Date();
            String time = dateFormat.format(date);
            System.out.println("current Date " + time);
            Random ra = new Random();
            
                    int range=ra.nextInt(3)+1;
                    Random skeyy = new SecureRandom();
                int skey2 = 25;
                String subauthkey1 = "1010010101001100111010010100101001001001010010";
                String subautk1 = "";
                
                String subautk2 = "";
                for (int i = 0; i < skey2; i++) {
                    int index = (int) (skeyy.nextDouble() * subauthkey1.length());
                    subautk1 += subauthkey1.substring(index, index + 1);
                }
                 subautk2 = new BlowfishAlgorithm().encrypt(subautk1);
          
//int range = 0 - 3 + 1;
String folder;
System.out.println("***************"+range);
if(range==1){
    folder="server1";
}
else if(range==2){
    folder="server2";
}else if(range==3)
{
    folder="server3";
}else{
    folder="server2";
}
    System.out.println("***************"+folder);
        
             Integer n=5 ;
            if (n>3) {
                      con = Dbconnection.getConnection();
                                pstm = con.prepareStatement("insert into ownerfilee (ownername,ownerid,filename,descrip,akey1,akey2,akey3,files,encryptf,privatekey,publickey,udate,status,filetypes,folders)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                Statement st = con.createStatement();
                                
                                pstm.setString(1, ownernmae);
                                pstm.setString(2, oidd);
                                pstm.setString(3, fname);
                                pstm.setString(4, descrip);
                                pstm.setString(5, akey1);
                                pstm.setString(6, akey2);
                                pstm.setString(7, akey3);
                                pstm.setBinaryStream(8,fiss);
                                pstm.setString(9, encstr);
                                
    pstm.setString(10, subautk1);
                                pstm.setString(11, subautk2);
                                pstm.setString(12, report);
                                pstm.setString(13, "waiting");
                                pstm.setString(14, filetypes);
                                pstm.setString(15, folder);
                                
                int i = pstm.executeUpdate();
                //session.setAttribute("ofilen", fiename);
                //session.setAttribute("trapp", skey);
                //session.setAttribute("oencc", encstr);
                //cloud storing encrypted file
            FileWriter fw = new FileWriter(file);
            fw.write(encstr);
            fw.close();
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            boolean status = new Ftpcon().upload(file,filetypes,folder);
                                if (status) {
                                    response.sendRedirect("Owner_fileup1.jsp?umssuc=success");
                                } else {
                                    response.sendRedirect("Owner_fileup1.jsp?umsfail=failed");
                                }
            } else {
                out.println("Error in FTP Connection");
            }
        } catch (Exception e) {
            out.println(e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
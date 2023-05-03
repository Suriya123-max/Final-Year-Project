/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author welcome
 */
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.net.ftp.FTPClient;

public class Ftpcon {

    FTPClient client = new FTPClient();
    FileInputStream fis = null;
    boolean status;

    public boolean upload(File file,String filetyp ,String foldername) {
        try {
            String nnn="PPPPPPPPPPPPPP"+filetyp;
            String mmm="LLLLLLLLLLLLL"+foldername;
            System.out.println(nnn);
            System.out.println(mmm);
            client.connect("ftp.drivehq.com");
            client.login("cloudclouds24", "cloudcloud1994");
            client.enterLocalPassiveMode();
            fis = new FileInputStream(file);
            status = client.storeFile(" /"+filetyp+"/"+foldername+"/" + file.getName(), fis);
            //status = client.storeFile(" /textfile/server3/" + file.getName(), fis);
            
            client.logout();
            fis.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        if (status) {
            System.out.println("success");
            return true;
        } else {
            System.out.println("failed");
            return false;

        }

    }
}

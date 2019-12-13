package Controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Path("client/")
public class Client {

    @GET
    @Path("js/{path}")
    @Produces({"image/jpeg,image/png"})
    public byte[] getImageFile(@PathParam("path") String path){
        return getFile("client/js/" + path);
    }

    @GET
    @Path("css/{path}")
    @Produces({"text/ccs"})
    public byte[] getCSSFile(@PathParam("path") String path){
        return getFile("client/css" + path);
    }

    @GET
    @Path("{path}")
    @Produces({"text/html"})
    public byte[] getHTMLFile(@PathParam("path") String path){
        return getFile("client/" + path);
    }

    @GET
    @Path("favicon.ico")
    @Produces({"images/x-icon"})
    public byte[] getFavicon(){
        return getFile("client/favicon.ico");
    }

    private static byte[] getFile(String filename){
        try {
            File file = new File("resources/" + filename);
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
            System.out.println("Sending: " + filename);
            return fileData;
        } catch (IOException e){
            System.out.println("File IO error:" + e.getMessage());
        }
        return null;
    }
}
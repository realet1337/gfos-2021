package com.realet.sip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/images")
public class bro {

    public class FileUploadForm {
        private byte[] filedata;
    
        public FileUploadForm() {}
    
        public byte[] getFileData() {
            return filedata;
        }
    
        @FormParam("filedata")
        @PartType("application/octet-stream")
        public void setFileData(final byte[] filedata) {
            this.filedata = filedata;
        }
    }
    
    @POST
    @Path("/users/pictures")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadUserPicture(@MultipartForm FileUploadForm form){
        InputStream inputStream = new ByteArrayInputStream(form.getFileData());
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            return Response.status(400).build();
        }
        
        String outName = "";
        File outputFile;
        try {
            do{

                byte[] imgBytes = Arrays.copyOfRange(form.getFileData(), 0, 16);
                byte[] timeBytes = String.valueOf(System.nanoTime()).getBytes(StandardCharsets.UTF_8);
                byte[] both = Arrays.copyOf(imgBytes, imgBytes.length + timeBytes.length);
                System.arraycopy(timeBytes, 0, both, imgBytes.length, timeBytes.length);
                byte[] outNameBytes = MessageDigest.getInstance("MD5").digest(both);

                outName = String.format("%0" + (outNameBytes.length << 1) + "x", new BigInteger(1, outNameBytes));

                String path = System.getProperty("com.realet.sip.uploadDir");
                if(path.charAt(path.length()-1) != '/'){
                    path = path + "/";
                }
                path = path + outName + ".jpg";

                outputFile = new File(path);

            }while(outputFile.exists()); //Just keep going until we get one.

        } catch (NoSuchAlgorithmException e) {
            // this wont ever happen
            return Response.status(500).build();
        }


        try (OutputStream os = new FileOutputStream(outputFile)) {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return Response.status(500).entity("Couldn't create image.").build();
        }

        return Response.status(201).entity(
            new JSONObject().put("picture", outName).toString()
        ).build();
    }

    @GET
    public Response getGet(){
        return Response.ok("bruh").build();
    }
}

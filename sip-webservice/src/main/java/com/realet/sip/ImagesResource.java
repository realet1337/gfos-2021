package com.realet.sip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.image.BufferedImage;

import javax.ws.rs.Consumes;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RestEasy Resource-Klasse für den Upload von Bildern.
 */
@Path("/images")
public class ImagesResource {
    
    /**
     * Speichert ein Bild im durch die System-Property "com.realet.sip.uploadDir" beschriebenen Upload-Verzeichnis unter "pic/user/".
     * Generiert einen einzigartigen MD5-Hash als Dateinamen und konvertiert das Bild zu einer JPG-Datei.
     * @param form
     * @return Status Code 201 mit einem JSON-Objekt {"picture":<Neuer Dateiname ohne Endung>}, 
     * Status Code 500, falls das Schreiben der Datei fehlschlägt. 
     * Status Code 415, falls der jeweilige Bild-Typ nicht von ImageIO unterstützt wird.
     * Status Code 400, falls das Bild größer als 30MB ist oder das Bild leer ist.
     */
    @POST
    @Path("/users/pictures")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadUserPicture(@MultipartForm FileUploadForm form){
        if(form.getData().length > 30000000){
            return Response.status(400).entity("Image can't be larger than 30 MB").build();
        }
        ImageReader reader = null;
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(new ByteArrayInputStream(form.getData()));
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if(!readers.hasNext()){
                return Response.status(415).entity("Unsupported image format.").build();
            }
            reader = readers.next();
        } catch (IOException e) {
            return Response.status(400).build();
        }
        
        BufferedImage image = null;
        try {
            reader.setInput(iis);
            image = reader.read(0);
        } catch (IOException e) {
            return Response.status(400).build();
        }

        if(image == null){
            return Response.status(400).build();
        }
        
        String outName = "";
        File outputFile;
        try {
            do{

                byte[] imgBytes = Arrays.copyOfRange(form.getData(), 0, 16);
                byte[] timeBytes = String.valueOf(System.nanoTime()).getBytes(StandardCharsets.UTF_8);
                byte[] both = Arrays.copyOf(imgBytes, imgBytes.length + timeBytes.length);
                System.arraycopy(timeBytes, 0, both, imgBytes.length, timeBytes.length);
                byte[] outNameBytes = MessageDigest.getInstance("MD5").digest(both);

                outName = String.format("%0" + (outNameBytes.length << 1) + "x", new BigInteger(1, outNameBytes));

                String path = System.getProperty("com.realet.sip.uploadDir");
                if(path.charAt(path.length()-1) != '/'){
                    path = path + "/";
                }
                path = path + "/pic/user/" + outName + ".jpg";

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

    /**
     * Speichert ein Bild im durch die System-Property "com.realet.sip.uploadDir" beschriebenen Upload-Verzeichnis unter "pic/group/".
     * Generiert einen einzigartigen MD5-Hash als Dateinamen und konvertiert das Bild zu einer JPG-Datei.
     * @param form
     * @return Status Code 201 mit einem JSON-Objekt {"picture":<Neuer Dateiname ohne Endung>}, 
     * Status Code 500, falls das Schreiben der Datei fehlschlägt. 
     * Status Code 415, falls der jeweilige Bild-Typ nicht von ImageIO unterstützt wird.
     * Status Code 400, falls das Bild größer als 30MB ist oder das Bild leer ist.
     */
    @POST
    @Path("/groups/pictures")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadGroupPicture(@MultipartForm FileUploadForm form){
        if(form.getData().length > 30000000){
            return Response.status(400).entity("Image can't be larger than 30 MB").build();
        }
        ImageReader reader = null;
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(new ByteArrayInputStream(form.getData()));
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if(!readers.hasNext()){
                return Response.status(415).entity("Unsupported image format.").build();
            }
            reader = readers.next();
        } catch (IOException e) {
            return Response.status(400).build();
        }
        
        BufferedImage image = null;
        try {
            reader.setInput(iis);
            image = reader.read(0);
        } catch (IOException e) {
            return Response.status(400).build();
        }

        if(image == null){
            return Response.status(400).build();
        }
        
        String outName = "";
        File outputFile;
        try {
            do{

                byte[] imgBytes = Arrays.copyOfRange(form.getData(), 0, 16);
                byte[] timeBytes = String.valueOf(System.nanoTime()).getBytes(StandardCharsets.UTF_8);
                byte[] both = Arrays.copyOf(imgBytes, imgBytes.length + timeBytes.length);
                System.arraycopy(timeBytes, 0, both, imgBytes.length, timeBytes.length);
                byte[] outNameBytes = MessageDigest.getInstance("MD5").digest(both);

                outName = String.format("%0" + (outNameBytes.length << 1) + "x", new BigInteger(1, outNameBytes));

                String path = System.getProperty("com.realet.sip.uploadDir");
                if(path.charAt(path.length()-1) != '/'){
                    path = path + "/";
                }
                path = path + "/pic/group/" + outName + ".jpg";

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
}

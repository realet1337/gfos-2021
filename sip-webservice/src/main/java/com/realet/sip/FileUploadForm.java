package com.realet.sip;

import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * Behälter-Klasse, die den "file"-Parameter eines multipart/form-data Requests aufnimmt. 
 */
public class FileUploadForm {

    public FileUploadForm() {
    }
    
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    @FormParam("file")
    @PartType("application/octet-stream")
    public void setData(byte[] data) {
        this.data = data;
    }
    
}
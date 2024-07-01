package com.thesensei.MEEP.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Value("${images.extensions}")
    private String[] imageExtensions;

    /**
     * checkImageSize() -> dato un file passato come parametro, il metodo restituisce true se il file non è vuoto e se rispetta le dimensioni consentite
     * @param file
     * @param imageSize
     * @return boolean
     */
    public boolean checkImageSize(MultipartFile file, int imageSize){
        if(!file.isEmpty() && file.getSize() <= imageSize)
            return true;
        return false;
    }

    /**
     * checkExtensions() -> dato un file passato come parametro, il metodo restituisce true se il file rispetta le estensioni consentite
     * @param file
     * @return boolean
     */
    public boolean checkExtensions(MultipartFile file){
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf('.')+1);
        for(String ext : imageExtensions){
            if(ext.equals(extension))
                return true;
        }
        return false;
    }

    /**
     * fromMultipartFileToBufferedImage() -> dato un Multipartfile passato come parametro, il metodo lo ritorna convertito in Bufferedimage
     * @param file
     * @return BufferedImage (file convertito)
     */
    public BufferedImage fromMultipartFileToBufferedImage(MultipartFile file){
        BufferedImage bf = null;
        try{
            bf= ImageIO.read(file.getInputStream());
            return bf;
        }   catch (IOException e){
            System.out.println("Errore: Trasformazione in BufferedImage non riuscita");
        }
        return bf;
    }

    /**
     * checkDimensioni() -> data un immagine, height e width consentite, restituisce true se l'immagine rispetta le dimensioni
     * @param image
     * @param height
     * @param width
     * @return boolean
     */
    public boolean checkDimensioni(BufferedImage image, int height, int width){
        if(image == null)
            return false;
        return image.getWidth() <= width && image.getHeight() <= height;
    }

    /**
     * uplaodfile() -> dato un file, il nome e il path di destinazione, permette di memorizzarlo
     * @param file
     * @param nomeFile
     * @param imagePath
     * @return
     */
    public boolean uploadFile(MultipartFile file, String nomeFile, String imagePath){
        try{
            Path percorso = Paths.get(imagePath+nomeFile);
            Files.write(percorso,file.getBytes());
            return true;
        } catch (IOException e){
            System.out.println("Errore nell'upload del file: ");
        }
        return false;
    }


}

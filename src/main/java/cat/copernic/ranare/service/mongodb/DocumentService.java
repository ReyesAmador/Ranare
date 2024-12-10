/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.IOException;
import java.io.InputStream;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngall
 */
@Service
public class DocumentService {
    
    @Autowired
    private GridFSBucket gridFSBucket;
    
    public String saveDocument(MultipartFile file) throws IOException{
        InputStream inputStream = file.getInputStream();
        GridFSUploadOptions options = new GridFSUploadOptions().metadata(null);
        ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);
        return fileId.toString();
    }
}

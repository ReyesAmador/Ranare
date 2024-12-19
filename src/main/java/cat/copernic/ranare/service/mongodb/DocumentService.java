/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.ranare.service.mongodb;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
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
    
    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    public String saveDocument(MultipartFile file) throws IOException{
        InputStream inputStream = file.getInputStream();
        GridFSUploadOptions options = new GridFSUploadOptions().metadata(null);
        ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), inputStream, options);
        return fileId.toString();
    }
    
    public byte[] getPdfById(String id){
        try{
            GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
            if(file != null){
                InputStream inputStream = gridFsTemplate.getResource(file).getInputStream();
                return inputStream.readAllBytes();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public byte[] getPdfsAsZip(List<String> pdfIds){
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)){
            
            for(String pdfId : pdfIds){
                GridFSFile file =gridFsTemplate.findOne(new Query(Criteria.where("_id").is(pdfId)));
                if (file != null) {
                    InputStream inputStream = gridFsTemplate.getResource(file).getInputStream();
                    ZipEntry zipEntry = new ZipEntry(file.getFilename());
                    zipOutputStream.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) >= 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    zipOutputStream.closeEntry();
                }
            }
            
            
            zipOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package edu.byu.cs.tweeter.server.dao.S3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IImageDAO;

public class S3DAO implements IImageDAO {

    private static final String BUCKET_NAME = "manuel-gordillo-tweeter-bucket";
    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

    public String uploadImage(String image, String username) {
        byte[] bI = Base64.getDecoder().decode(image);
        InputStream fis = new ByteArrayInputStream(bI);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bI.length);
        metadata.setContentType("image/png");
        metadata.setCacheControl("public, max-age=31536000");

        s3.putObject(BUCKET_NAME, username, fis, metadata);
        s3.setObjectAcl(BUCKET_NAME, username, CannedAccessControlList.PublicRead);

        return s3.getUrl(BUCKET_NAME, username).toString();
    }
}

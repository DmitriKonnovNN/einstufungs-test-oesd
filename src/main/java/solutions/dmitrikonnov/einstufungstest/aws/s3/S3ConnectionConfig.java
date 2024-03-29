package solutions.dmitrikonnov.einstufungstest.aws.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@AllArgsConstructor(onConstructor_ ={@Autowired})
public class S3ConnectionConfig {

    private final static String fileName = "iamadmin_exam-general-accessKeys.csv";

    //@Value("${aws.s3.access.key.id}")

    //@Value("${aws.s3.access.key.secret}")



    @Value("${aws.s3.region.name}")
    private final String s3RegionName;
    //private final SystemPropertiesCredentialsProvider credPrvdr;

    @Bean
    public List<String> credentials (){

        List<String> keyIdandSecret = new ArrayList<>();
        keyIdandSecret.add("");
        keyIdandSecret.add("");
      /*  try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
              keyIdandSecret = reader.readAll().stream()
                    .skip(1)
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toUnmodifiableList());
            System.out.println(keyIdandSecret.toString());


        } catch (FileNotFoundException e) {
            log.error("Could not read credentials from CSV. Process failed!");
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        } catch (CsvException e) {
            log.error(e.toString());
        }*/
        return keyIdandSecret;
    }


    @Bean
    public AmazonS3 getAmazonS3Client() {


        System.out.println("credentials = " + credentials().get(0));
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(credentials().get(0), credentials().get(1));
        // Get Amazon S3 client and return the S3 client object
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(s3RegionName)
                .build();
    }

}

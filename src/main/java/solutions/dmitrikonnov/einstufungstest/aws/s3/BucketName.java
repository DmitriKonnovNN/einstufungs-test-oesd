package solutions.dmitrikonnov.einstufungstest.aws.s3;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BucketName {
    AUFGABE_MEDIADATA ("et-s3-mediadata-storage");
    private final String bucketName;
}

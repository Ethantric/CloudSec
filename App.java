package com.example.assignment1_2;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
    	//Create instance of AWSCredentials
        AWSCredentials credentials = null;
        try {
        	credentials = new ProfileCredentialsProvider("default").getCredentials();
        }catch (Exception e) {
        	throw new AmazonClientException(
        			"Cannot load the credentials from the credential profiles file. " +
        			"Please make sure that your credentials file is at the correct " +
        			"location (C:/Users/%USERPROFILE%/.aws/credentials), and is in valid format.", e);
        }
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        while(userChoice != 3) {
        	//Give user choice on creating bucket, uploading file to bucket, or ending the program 
        	System.out.print("Enter 1 to create a new bucket, "
        			+ "2 to add a file to an existing bucket, "
        			+ "or 3 to end the program: ");
        	userChoice = scanner.nextInt();
        	scanner.nextLine();
        	
        	if(userChoice == 1) {
        		//Obtain information to create bucket
        		System.out.print("The following information is needed to create an S3 bucket");
                System.out.print("\nWhat region do you want this bucket to be located in?: ");
                System.out.flush();
                String region = scanner.nextLine();
                System.out.print("What is the name of the bucket? (Must be unique): ");
                System.out.flush();
                String bucketName = scanner.nextLine();
                //Attempt to create s3 bucket
                try {
                    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
                    if (!s3Client.doesBucketExistV2(bucketName)) {
                        s3Client.createBucket(new CreateBucketRequest(bucketName));
                        String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
                        System.out.println("Bucket location: " + bucketLocation);
                    }
                    s3Client.shutdown();
                //In case exceptions are thrown during creation
                } catch (AmazonServiceException e) {
                    e.printStackTrace();
                } catch (SdkClientException e) {
                    e.printStackTrace();
                }
                System.out.println("Bucket created with name: " + bucketName + "!");
        	}else if(userChoice == 2) {
        		//Obtain information about existing bucket and file info
        		System.out.print("Enter the name of the S3 bucket: ");
        	    System.out.flush();
        	    String bucketName = scanner.nextLine();
        	    System.out.print("Please state the region the bucket is located in: ");
        	    String region = scanner.nextLine();
        	    System.out.print("Enter the full path of the object you would like to upload: ");
        	    System.out.flush();
        	    String filePath = scanner.nextLine();
        	    File file = new File(filePath);
        	    //Check if file exists
        	    if (!file.exists()) {
        	        System.out.println("Error: File does not exist. Please check the path.");
        	    } else {
        	    	//Attempt to upload file
        	        try {
        	            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        	                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
        	                    .withRegion(region)
        	                    .build();
        	            String objectKey = file.getName();
        	            System.out.println("Uploading file...");
        	            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, file));
        	            System.out.println("File uploaded successfully to bucket: " + bucketName);
        	            s3Client.shutdown();
        	        //Catch any exceptions
        	        } catch (AmazonServiceException e) {
        	            e.printStackTrace();
        	        } catch (SdkClientException e) {
        	            e.printStackTrace();
        	        }
        	    }
        	//User wants to exit program
        	}else if (userChoice == 3){
        		break;
        	//User enters invalid option
        	}else {
        		System.out.println("Invalid option. Please enter 1, 2, or 3");
        	}
        }
        scanner.close();
    }
}

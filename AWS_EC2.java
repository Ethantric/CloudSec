package com.example.assignment1_4590;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import java.util.Scanner;



public class AWS_EC2 {
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
        
    	//Ask user parameters
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("\nThe following is a series of parameters needed to create an EC2 instance:");
    	System.out.print("Region should the instance be created in: ");
    	String region = scanner.nextLine();
    	System.out.print("Name of the security group: ");
    	String secGroup = scanner.nextLine();
    	System.out.print("Name of the instance type: ");
    	String insType = scanner.nextLine();
    	System.out.print("What is the image ID: ");
    	String imageID = scanner.nextLine();
    	System.out.print("Name of the key pair for login: ");
    	String keyName = scanner.nextLine();
    	scanner.close();
    	
        //Create EC2 client
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
        
        //Create request to EC2 client
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        runInstancesRequest.withImageId(imageID).withInstanceType(insType).withMinCount(1).withMaxCount(1).withKeyName(keyName).withSecurityGroups(secGroup);
        RunInstancesResult result = ec2.runInstances(runInstancesRequest);
        System.out.println("EC2 Instance created!");
        ec2.shutdown();
    }
}
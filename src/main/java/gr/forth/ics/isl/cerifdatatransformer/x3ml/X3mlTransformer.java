package gr.forth.ics.isl.cerifdatatransformer.x3ml;

import java.io.File;
import eu.delving.x3ml.X3MLEngineFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
 
/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class X3mlTransformer {
    private static final Logger logger=Logger.getLogger(X3mlTransformer.class);
    private final int inputFilesBucketSize;
    private final File mappingsFile;
    private final File generatorPolicyFile;
    private final File inputFolder;
    private final File outputFolder;
            
    public X3mlTransformer(File mappingsFile, File generatorPolicyFile, File inputFolder, File outputFolder, int inputFilesBucketSize){
        this.mappingsFile=mappingsFile;
        this.generatorPolicyFile=generatorPolicyFile;
        this.inputFolder=inputFolder;
        this.outputFolder=outputFolder;
        this.inputFilesBucketSize=inputFilesBucketSize;
    }
    
    public void transformData(String filename){
        int outputCnt=1;
        for(List<File> filesBucket : this.createBucketsOfFiles()){
            X3MLEngineFactory x3mlEngineFactory=X3MLEngineFactory.create()
                                                                 .withMappings(mappingsFile)
                                                                 .withGeneratorPolicy(generatorPolicyFile)
                                                                 .withInputFiles(filesBucket)
                                                                 .withUuidSize(-1)
                                                                 .withOutput(this.outputFolder+"/"+filename+outputCnt+".n3", X3MLEngineFactory.OutputFormat.NTRIPLES);
            x3mlEngineFactory.execute();
            logger.info("Exported file "+this.outputFolder+"/"+filename+outputCnt+".ntriples");
            System.gc();
            outputCnt+=1;
        }
    }
    
    private List<List<File>> createBucketsOfFiles(){
        List<List<File>> masterList=new ArrayList<>();
        int filesCounter=1;
        List<File> bucketOfFiles=new ArrayList<>();
        for(File currentFile : inputFolder.listFiles()){
            if(filesCounter>this.inputFilesBucketSize){
                masterList.add(bucketOfFiles);
                bucketOfFiles=new ArrayList<>();
                filesCounter=1;
            }else{
                bucketOfFiles.add(currentFile);
                filesCounter+=1;
            }
        }
        masterList.add(bucketOfFiles);
        return masterList;
    }   
    
    private static void transformEKTData(){
        X3mlTransformer.transformEKTPersonsData();
        X3mlTransformer.transformEKTProjectsData();
        X3mlTransformer.transformEKTPublicationsData();
        X3mlTransformer.transformEKTeAddressData();
        X3mlTransformer.transformEKTpAddressData();
        X3mlTransformer.transformEKTOrganizationsData();
        X3mlTransformer.transformEKTFundingsData();
    }
    
    private static void transformEKTPersonsData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-persons.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/persons"),
                            new File("output-data/EKT/persons/"),10).transformData("persons");
    }
    
    private static void transformEKTProjectsData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-projects.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/projects/"),
                            new File("output-data/EKT/projects/"),5).transformData("projects");
    }
    
    private static void transformEKTPublicationsData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-publications.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/publications/"),
                            new File("output-data/EKT/publications/"),10).transformData("publications");
    }
    
    private static void transformEKTeAddressData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-eaddress.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/eaddress/"),
                            new File("output-data/EKT/eaddress/"),10).transformData("eaddress");
    }
        
    private static void transformEKTpAddressData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-postalAddress.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/postalAddresses/"),
                            new File("output-data/EKT/postalAddresses/"),10).transformData("postalAddresses");
    }
    
    private static void transformEKTOrganizationsData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-organizations.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/organizationUnits/"),
                            new File("output-data/EKT/organizationUnits/"),5).transformData("organizationUnits");
    }
    
    private static void transformEKTFundingsData(){
        new X3mlTransformer(new File("mappings/EKT/EKT-mappings-fundings.x3ml"),
                            new File("mappings/EKT/EKT-generator-policy.xml"),
                            new File("input-data/EKT/fundings/"),
                            new File("output-data/EKT/fundings/"),5).transformData("fundings");
    }
    
    private static void transformRCUKData(){
        X3mlTransformer.transformRCUKPersonsData();
        X3mlTransformer.transformRCUKProjectsData();
        X3mlTransformer.transformRCUKPublicationsData();
        X3mlTransformer.transformRCUKOrganizationsData();
    }
    
    private static void transformRCUKPersonsData(){
        new X3mlTransformer(new File("mappings/RCUK/RCUK-mappings-persons.x3ml"),
                            new File("mappings/RCUK/RCUK-generator-policy.xml"),
                            new File("input-data/RCUK/persons/"),
                            new File("output-data/RCUK/persons/"),5).transformData("persons");
    }
    
    private static void transformRCUKProjectsData(){
        new X3mlTransformer(new File("mappings/RCUK/RCUK-mappings-projects.x3ml"),
                            new File("mappings/RCUK/RCUK-generator-policy.xml"),
                            new File("input-data/RCUK/projects/"),
                            new File("output-data/RCUK/projects/"),5).transformData("projects");
    }
    
    private static void transformRCUKPublicationsData(){
        new X3mlTransformer(new File("mappings/RCUK/RCUK-mappings-publications.x3ml"),
                            new File("mappings/RCUK/RCUK-generator-policy.xml"),
                            new File("input-data/RCUK/publications/"),
                            new File("output-data/RCUK/publications/"),10).transformData("publications");
    }
    
    private static void transformRCUKOrganizationsData(){
        new X3mlTransformer(new File("mappings/RCUK/RCUK-mappings-organizations.x3ml"),
                            new File("mappings/RCUK/RCUK-generator-policy.xml"),
                            new File("input-data/RCUK/organizations/"),
                            new File("output-data/RCUK/organizations/"),10).transformData("organizations");
    }
    
    private static void transformFRISData(){
        X3mlTransformer.transformFRISPersonsData();
        X3mlTransformer.transformFRISProjectsData();
        X3mlTransformer.transformFRISPublicationsData();
        X3mlTransformer.transformFRISOrganizationsData();
    }
    
    private static void transformFRISPersonsData(){
        new X3mlTransformer(new File("mappings/FRIS/FRIS-mappings-persons.x3ml"),
                            new File("mappings/FRIS/FRIS-generator-policy.xml"),
                            new File("input-data/FRIS/persons/"),
                            new File("output-data/FRIS/persons/"),50).transformData("persons");
    }
    
    private static void transformFRISProjectsData(){
        new X3mlTransformer(new File("mappings/FRIS/FRIS-mappings-projects.x3ml"),
                            new File("mappings/FRIS/FRIS-generator-policy.xml"),
                            new File("input-data/FRIS/projects/"),
                            new File("output-data/FRIS/projects/"),10).transformData("projects");
    }
    
    private static void transformFRISPublicationsData(){
        new X3mlTransformer(new File("mappings/FRIS/FRIS-mappings-publications.x3ml"),
                            new File("mappings/FRIS/FRIS-generator-policy.xml"),
                            new File("input-data/FRIS/publications/"),
                            new File("output-data/FRIS/publications/"),10).transformData("publications");
    }
    
    private static void transformFRISOrganizationsData(){
        new X3mlTransformer(new File("mappings/FRIS/FRIS-mappings-organizations.x3ml"),
                            new File("mappings/FRIS/FRIS-generator-policy.xml"),
                            new File("input-data/FRIS/organizations/"),
                            new File("output-data/FRIS/organizations/"),10).transformData("organizations");
    }
    
    private static void transformClassificationData(){
        new X3mlTransformer(new File("mappings/classifications/classifications-mappings.x3ml"),
                            new File("mappings/classifications/classifications-generator-policy.xml"),
                            new File("input-data/classification/"),
                            new File("output-data/classification/"),10).transformData("classification");
    }
    
    public static void main(String []args){
        X3mlTransformer.transformEKTData();
        X3mlTransformer.transformRCUKData();
        X3mlTransformer.transformFRISData();
        X3mlTransformer.transformClassificationData();
    }
}
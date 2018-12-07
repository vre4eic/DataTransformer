package gr.forth.ics.isl.cerifdatatransformer.utils.geoFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GeoLocationAppender is a utility class which is responsible for generating synthetic 
 * geographic coordinates for various resources (i.e. Organizations, Products, etc.).
 * The tool creates a small bounding box (that somehow resembles a pin in the map).
 * The coordinates are generated with respect to EPSG:4326 standard.
 * 
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class GeoLocationAppender {
    private static final String WEST_LONGITUDE="west_longitude";
    private static final String EAST_LONGITUDE="east_longitude";
    private static final String SOUTH_LATITUDE="south_latitude";
    private static final String NORTH_LATITUDE="north_latitude";
    private static final String GEO_TAG_NAME="geo";
    private static final float LONGITUDE_MIN=-153;
    private static final float LONGITUDE_MAX=172;
    private static final float LATITUDE_MIN=-53;
    private static final float LATITUDE_MAX=76;
    
    private static void appendGeoLocation(String elementName, File file, File outputFile) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8"));
        String line;
        while((line=br.readLine())!=null){
            if(line.contains(toXmlElementOpen(elementName))){
                List<Float> geoCoord=getCoordinates();
                bw.write(toXmlElementOpen(GEO_TAG_NAME)+"\n"+
                         "\t"+toXmlElementOpen(WEST_LONGITUDE)+geoCoord.get(0)+""+toXmlElementClose(WEST_LONGITUDE)+"\n"+
                         "\t"+toXmlElementOpen(EAST_LONGITUDE)+geoCoord.get(1)+""+toXmlElementClose(EAST_LONGITUDE)+"\n"+
                         "\t"+toXmlElementOpen(SOUTH_LATITUDE)+geoCoord.get(2)+""+toXmlElementClose(SOUTH_LATITUDE)+"\n"+
                         "\t"+toXmlElementOpen(NORTH_LATITUDE)+geoCoord.get(3)+""+toXmlElementClose(NORTH_LATITUDE)+"\n"+
                        toXmlElementClose(GEO_TAG_NAME)+"\n");
            }
            bw.write(line);
            bw.write("\n");
        }
        
        bw.flush();
        bw.close();
        br.close();
    }
    
    private static List<Float> getCoordinates(){
        List<Float> retCoordinates=new ArrayList<>();
        Random r=new Random();
        float longitude=r.nextFloat()* (LONGITUDE_MAX-LONGITUDE_MIN)+LONGITUDE_MIN;
        float latitude=r.nextFloat()* (LATITUDE_MAX-LATITUDE_MIN)+LATITUDE_MIN;
        retCoordinates.add(0, longitude);
        retCoordinates.add(1, new Float(longitude+0.001));
        retCoordinates.add(2, latitude);
        retCoordinates.add(3, new Float(latitude+0.001));
        return retCoordinates;
    }
    
    private static String toXmlElementOpen(String tagName){
        return "<"+tagName+">";
    }
    
    private static String toXmlElementClose(String tagName){
        return "</"+tagName+">";
    }
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String inputFolder="input-data/EKT/organizationUnits/";
        String outputFolder="input-data/EKT/organizationUnits_with_synthetic_geo_coordinates/";
        
        for(File f : new File(inputFolder).listFiles()){
            appendGeoLocation("cfOrgUnit", f, new File(outputFolder+f.getName()));
        }
    }
}
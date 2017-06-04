/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author tim
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.faces.model.ListDataModel;

public class Kmeans
{
    private static final int NUM_CLUSTERS = 2;    // Total clusters.
    private static final int TOTAL_DATA = 6;// Total data points.
    //Check size of data fetched from database
    
    
    private static ArrayList<Data> dataSet = new ArrayList<Data>();
    private static ArrayList<Centroid> centroids = new ArrayList<Centroid>();
    
    private static void initialize()
    {
        System.out.println("Centroids initialized at:");
        //this k will be selected
        RetrieveInfo info= new RetrieveInfo();
        double[][]infoOne = info.data();
        List listOne = new ArrayList();
        //System.out.println("============= "+ infoOne[1][1]);
        int a = 0;
        int b = 0;
        for(a = 0; a<infoOne.length; a++){
           
          
                //System.out.println("============= "+ infoOne[a][0]);
                 //System.out.println("####$$$$$$=== "+ infoOne[a][1]);
                 
                 double diff = infoOne[a][1]-infoOne[a][0];
                 
              System.out.println("Value pair diff "+ infoOne[a][0]+","+infoOne[a][1]+ " Difference"+diff);
                 listOne.add(a, diff);
                 
            
            
        }
        double min = (double) Collections.min(listOne);
        double max = (double) Collections.max(listOne);
        System.out.println("Max value in town "+max);
        double val1 = 0.0;
        double val2 = 0.0;
        double val3 = 0.0;
        double val4 = 0.0;
        
        //start another for loop
        for(a = 0; a<infoOne.length; a++){
           
          
                //System.out.println("============= "+ infoOne[a][0]);
                 //System.out.println("####$$$$$$=== "+ infoOne[a][1]);
                 
                 double diff = infoOne[a][1]-infoOne[a][0];
                 if(diff == min){
                     System.out.println("Passing this value pair as min centroid "+ infoOne[a][0]+","+infoOne[a][1]);
                     val1 = infoOne[a][0];
                     val2 = infoOne[a][1];
                     
                 }
                 
                  if(diff == max){
                     System.out.println("Passing this value pair as max centroid "+ infoOne[a][0]+","+infoOne[a][1]);
                     val3 = infoOne[a][0];
                     val4 = infoOne[a][1];
                 }
                 
                 
            
            
        }
        centroids.add(new Centroid(val1, val2)); // lowest set.
        centroids.add(new Centroid(val3, val4)); // highest set.
        System.out.println("     (" + centroids.get(0).X() + ", " + centroids.get(0).Y() + ")");
        System.out.println("     (" + centroids.get(1).X() + ", " + centroids.get(1).Y() + ")");
        System.out.print("\n");
        return;
    }
    
    private static void kMeanCluster()
    {
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Data newData = null;
        RetrieveInfo info= new RetrieveInfo();
        double[][]infoOne = info.data();
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(dataSet.size() < infoOne.length)
        {
            newData = new Data(infoOne[sampleNumber][0], infoOne[sampleNumber][1]);
            dataSet.add(newData);
            minimum = bigNumber;
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                distance = dist(newData, centroids.get(i));
                if(distance < minimum){
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.cluster(cluster);
            
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).cluster() == i){
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }
            sampleNumber++;
        }
        
        // Now, keep shifting centroids until equilibrium occurs.
        while(isStillMoving)
        {
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).cluster() == i){
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }
            
            // Assign all data to the new centroids
            isStillMoving = false;
            
            for(int i = 0; i < dataSet.size(); i++)
            {
                Data tempData = dataSet.get(i);
                minimum = bigNumber;
                for(int j = 0; j < NUM_CLUSTERS; j++)
                {
                    distance = dist(tempData, centroids.get(j));
                    if(distance < minimum){
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.cluster(cluster);
                if(tempData.cluster() != cluster){
                    tempData.cluster(cluster);
                    isStillMoving = true;
                }
            }
        }
        return;
    }
    
    /**
     * // Calculate Euclidean distance.
     * @param d - Data object.
     * @param c - Centroid object.
     * @return - double value.
     */
    private static double dist(Data d, Centroid c)
    {
        return Math.sqrt(Math.pow((c.Y() - d.Y()), 2) + Math.pow((c.X() - d.X()), 2));
    }
    
    private static class Data
    {
        private double mX = 0;
        private double mY = 0;
        private int mCluster = 0;
        
        public Data()
        {
            return;
        }
        
        public Data(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }
        
        public void X(double x)
        {
            this.mX = x;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double y)
        {
            this.mY = y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
        
        public void cluster(int clusterNumber)
        {
            this.mCluster = clusterNumber;
            return;
        }
        
        public int cluster()
        {
            return this.mCluster;
        }
    }
    
    private static class Centroid
    {
        private double mX = 0.0;
        private double mY = 0.0;
        
        public Centroid()
        {
            return;
        }
        
        public Centroid(double newX, double newY)
        {
            this.mX = newX;
            this.mY = newY;
            return;
        }
        
        public void X(double newX)
        {
            this.mX = newX;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double newY)
        {
            this.mY = newY;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
    }
    
    public static void Classes() throws IOException
    {
        //List classes = new ArrayList<>();
        String cluster = "";
        String set ="";
        initialize();
        kMeanCluster();
        RetrieveInfo info= new RetrieveInfo();
        double[][]infoOne = info.data();
        BufferedWriter bw = null;
         FileWriter fw;
         fw = new FileWriter("/home/tim/Documents/NetBeansProjects/KmeansClustering/result.txt");
        bw = new BufferedWriter(fw);
        // Print out clustering results.
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("Cluster " + i + " includes:");
            cluster = "Cluster " + i + " includes: ";
                //byte[] contentInBytes = cluster.getBytes();
                bw.write(cluster);
                bw.write("\n");
                
            for(int j = 0; j < infoOne.length; j++)
            {
                if(dataSet.get(j).cluster() == i){
                    System.out.println("     (" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")");
                    
                     set= "(" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")";
                     //byte[] contentInBytes2 = set.getBytes();
                     bw.write("\n");
                     bw.write(set);
                     bw.write("\n");
                }
            } // j
            System.out.println();
        } // i
        bw.flush();
        bw.close();
        // Print out centroid results.
        System.out.println("Centroids finalized at:");
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("     (" + centroids.get(i).X() + ", " + centroids.get(i).Y());
        }
        System.out.print("\n");
        return;
    }
}

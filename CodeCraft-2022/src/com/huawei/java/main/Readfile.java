package com.huawei.java.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Readfile {
    public String[][] sitedot;
    public int dotnum;
    public ArrayList<String> sitename =new ArrayList<>();
    public ArrayList<Integer> siteband =new ArrayList<>();
    public ArrayList<String> dotlist =new ArrayList<>();
    public ArrayList<ArrayList<Integer>> dotdemandlist =new ArrayList<>();
    public ArrayList<String> timedot =new ArrayList<>();
    public int[][] dotdemandtable;
    public int[][] qosttabe;
    public int qos_constraint;
    public static void main(String[] args){
        Readfile readfile = new Readfile();
        readfile.readsite();
        readfile.readdot();
        readfile.readqos();
    }
    public void readsite(){
        int sitek=0;
        String site_band = "../data/site_bandwidth.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(site_band));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] site = line.split(cvsSplitBy);
                if(sitek==0){
                    sitek++;
                    continue;
                }
                sitek++;
                sitename.add(site[0]);
                siteband.add(Integer.parseInt(site[1]));
                //System.out.println("");
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void readdot(){
        int dotk=0;
        String demandfile = "../data/demand.csv";
        BufferedReader br = null;
        String line = "";//
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(demandfile));
            while ((line = br.readLine()) != null) {
                String[] dotmand = line.split(cvsSplitBy);
                //donum=d
                if(dotk==0){
                    dotnum=dotmand.length;
                    for(int i=1;i<dotmand.length;i++){
                        dotlist.add(dotmand[i]);
                    }
                    dotk++;
                    continue;
                }
                timedot.add(dotmand[0]);
                ArrayList<Integer> dotmandtemp=new ArrayList<>();
                for(int j=1;j<dotmand.length;j++){
                    dotmandtemp.add(Integer.parseInt(dotmand[j]));
                }
                dotdemandlist.add(dotmandtemp);
                dotk++;
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int timedotnum=timedot.size();
        int dotnums=dotnum-1;
        dotdemandtable=new int[timedotnum][dotnums];
        int n=0;
        int m=0;
        for(ArrayList<Integer> e:dotdemandlist){
            for(Integer f:e){
                dotdemandtable[m][n]=f;
                n++;
            }
            n=0;
            m++;
        }
    }
    public void readqos(){
        int qosk=0;
        String qosfile = "../data/qos.csv";
        BufferedReader br = null;
        String line = "";////qos
        String cvsSplitBy = ",";
        int dotsize=dotlist.size();
        int sitesize=sitename.size();
        qosttabe=new int[sitesize][dotsize];
        int row=0;int lin=0;
        try {
            br = new BufferedReader(new FileReader(qosfile));
            while ((line = br.readLine()) != null) {
                String[] qoslist = line.split(cvsSplitBy);
                if(qosk==0){
                    qosk++;
                    continue;
                }
                for(int j=1;j<qoslist.length;j++){
                    qosttabe[row][lin++]=Integer.parseInt(qoslist[j]);
                }
                lin=0;
                row++;
                qosk++;
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void readqoscon(){
        int word=0;
        String qosconfile = "../data/config.ini";
        BufferedReader br = null;
        String line = "";////qos
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(qosconfile));
            while ((line = br.readLine()) != null) {
                String[] qosconlist = line.split("=");
                if(word==0){
                    word++;
                    continue;
                }
                for(String e:qosconlist){
                    //System.out.println(e);
                }
                qos_constraint=Integer.parseInt(qosconlist[1]);
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

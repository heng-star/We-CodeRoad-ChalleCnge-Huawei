package com.huawei.java.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    //文件原始数据和定义全局变量
    static ArrayList<String> usernodelist=new ArrayList<>();
    static ArrayList<String> timelist=new ArrayList<>();
    static ArrayList<ArrayList<Integer>> usertimedemandlist=new ArrayList<>();
    static HashMap<Integer,String> userindex_namemap=new HashMap<>();
    static HashMap<String,Integer> username_indexmap=new HashMap<>();
    static HashMap<Integer,String> siteindex_namemap=new HashMap<>();
    static HashMap<String,Integer> sitename_indexmap=new HashMap<>();
    static ArrayList<String> sitenamelist=new ArrayList<>();
    static ArrayList<Integer> sitebandlist=new ArrayList<>();//边缘节点得带宽
    static int qos_constraint;



    static User[] users;
    static Site[] sites;


    public static void main(String[] args) {
        //读取数据
        readdemand();
        readsite();
        readqoscon();
        readqos();
        int timesize=timelist.size();
		int usernodenum= usernodelist.size();
		int sitenodenum=sitebandlist.size();
		String resstring="";
		for(int useri=0;useri<usernodenum;useri++){
		    users[useri].user_siteList=new ArrayList[timesize];
            users[useri].usersitedeliver=new HashMap[timesize];
            for(int userj=0;userj<timesize;userj++){
                users[useri].user_siteList[userj]=new ArrayList<>();
                users[useri].usersitedeliver[userj]=new HashMap<>();
            }
        }
		for(int sitei=0;sitei<sitenodenum;sitei++){
            sites[sitei].sitedemanremain =new int[timesize];
            sites[sitei].siteuserdeliver=new HashMap[timesize];
		    for(int sitej=0;sitej<timesize;sitej++){
                sites[sitei].sitedemanremain[sitej] =sitebandlist.get(sitei);
                sites[sitei].siteuserdeliver[sitej] = new HashMap<>();
            }
        }
        for(int i=0;i<usernodenum;i++){
            for(int j=0;j<sitenodenum;j++){
                if(users[i].usersiteqosmap.get(j)<qos_constraint){
                    users[i].usernodevailbleqossite.add(j);
                }

            }
        }
		String resultstring="";
		String outres="";
		for(int i=0;i<timesize;i++){
			//
			String res="";
			String out="";
			int uesri=0;

            Queue<User> queue1 = new LinkedList<>();
            Queue<User> queue2 = new LinkedList<>();
            for(int j=0;j<usernodenum;j++){
                queue1.offer(users[j]);
            }
            while (!queue1.isEmpty()){
                if(queue1.peek().usernoderemainlist.get(i)>0){
                    int curj=username_indexmap.get(queue1.peek().usernodename);
                    String outstring=deliver(i,curj);
                    if(outstring!="false"){
                        queue2.offer(queue1.poll());
                        out=out+outstring;
                    }else{
                        for(int ii=0;ii<sitenodenum;ii++){
                            sites[ii].init(i);//
                        }
                        while (!queue2.isEmpty()){
                            users[username_indexmap.get(queue2.peek().usernodename)].init(i);
                            queue1.offer(queue2.poll());
                        }
                        out="";
                        continue;
                    }
                }else {
                    out=queue1.peek().usernodename+":\n";
                    queue1.poll();
                }
            }

//			for(int j=0;j<usernodenum;j++){
//
//			}
            //System.out.println(res);
			resultstring=resultstring+res;
			outres=outres+out;
		}
		//System.out.println(resultstring);
//        for(int i=0;i)
		//System.out.println(outres);
		Out out1 = new Out();
		out1.output(outres);
		int ok=0;
	}
	static String deliver(int i, int j){
        String res="";
        String out="";
        //users[j].usersitedeliver[j]=new HashMap<>();
        res=res+users[j].usernodename+":";
        out=out+users[j].usernodename+":";
        //该时刻下当前节点的需求值
        int usernodevaluetemp=users[j].usernodedemandlist.get(i);
        if(usernodevaluetemp>0){
            ArrayList<Integer> vailbleqossitetemp=users[j].usernodevailbleqossite;
            int tempsitenum=vailbleqossitetemp.size();
            int nodedemandttemp=users[j].usernodedemandlist.get(i);
            //均分；
            int avergetemp=nodedemandttemp/tempsitenum;
            int avergetemprem=nodedemandttemp%tempsitenum;//剩余的
            int k=0;
            int sitetempk=0;
            int delivercout=0;
            while (usernodevaluetemp>0){
                int siteindextemp=vailbleqossitetemp.get(k);
                if(usernodevaluetemp<avergetemp){
                    avergetemp=usernodevaluetemp;
                }
                if(sites[siteindextemp].sitedemanremain[i]>avergetemp){//可用
                    if(sitetempk==0){
                        //
                        //users[j].usernodesite_delivermap[i][siteindextemp]+=avergetemp+avergetemprem;
                        users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
                                getOrDefault(siteindextemp,0)+avergetemp+avergetemprem);
                        sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
                                getOrDefault(j,0)+avergetemp+avergetemprem);
                        users[j].usernoderemainlist.set(i,users[j].usernoderemainlist.get(i)-avergetemp-avergetemprem);
                        sites[siteindextemp].sitedemanremain[i]-=avergetemp+avergetemprem;
                        usernodevaluetemp-=avergetemp+avergetemprem;
                        res=res+"<"+sites[siteindextemp].sitenodename+","+(avergetemp+avergetemprem)+">";
                        sitetempk++;
                    }else{
                        //
                        //users[j].usernodesite_delivermap[i][siteindextemp]+=avergetemp;
                        users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
                                getOrDefault(siteindextemp,0)+avergetemp);
                        sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
                                getOrDefault(j,0)+avergetemp);
                        sites[siteindextemp].sitedemanremain[i]-=avergetemp;
                        users[j].usernoderemainlist.set(i,users[j].usernoderemainlist.get(i)-avergetemp);
                        usernodevaluetemp-=avergetemp;
                        res=res+"<"+sites[siteindextemp].sitenodename+","+avergetemp+">";
                    }
                }else if(sites[siteindextemp].sitedemanremain[i]<=avergetemp &&sites[siteindextemp].sitedemanremain[i]>0){
                    //
                    //users[j].usernodesite_delivermap[i][siteindextemp]+=sites[siteindextemp].sitedemanremain[i];
                    users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
                            getOrDefault(siteindextemp,0)+sites[siteindextemp].sitedemanremain[i]);
                    sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
                            getOrDefault(j,0)+sites[siteindextemp].sitedemanremain[i]);
                    users[j].usernoderemainlist.set(i,users[j].usernoderemainlist.get(i)-sites[siteindextemp].sitedemanremain[i]);
                    res=res+"<"+sites[siteindextemp].sitenodename+","+sites[siteindextemp].sitedemanremain[i]+">";
                    usernodevaluetemp-=sites[siteindextemp].sitedemanremain[i];
                    sites[siteindextemp].sitedemanremain[i]-=sites[siteindextemp].sitedemanremain[i];
                }
                k++;
                delivercout++;
                if(delivercout>tempsitenum*tempsitenum){
                    return "false";
                }
                if(k==tempsitenum){
                    k=0;
                }
                if(usernodevaluetemp!=0 && sitetempk!=0&&k!=0){
                    res=res+",";
                }
            }
            res=res+"\n";
        }else{
            res=res+"\n";
        }
        Set<Integer> integers = users[j].usersitedeliver[i].keySet();
        int ecount=1;
        users[j].usersitedeliver[i].size();
        for(int e:integers){
            out=out+"<"+siteindex_namemap.get(e)+","+users[j].usersitedeliver[i].get(e)+">";
            if(ecount!=users[j].usersitedeliver[i].size()){
                out=out+",";
            }
            ecount++;
        }
        out=out+"\n";
        return out;
    }
	public void solution(){
	}
	static class User{
	    public String usernodename;
	    ArrayList<Integer> usernodedemandlist=new ArrayList<>();//在每个时刻得需求
	    ArrayList<Integer> usernoderemainlist=new ArrayList<>();//在每个时刻得剩余
        ArrayList<Integer>[] user_siteList;//
        ArrayList<Integer> usernodevailbleqossite=new ArrayList<>();
		Map<Integer,Integer>[] usersitedeliver=new HashMap[timelist.size()];//用户节点在每个时刻下的分的边缘节点的值。
        HashMap<Integer,Integer> usersiteqosmap=new HashMap<>();//用户结点与边缘结点对应的qos值。
        void init(int timei){
            users[username_indexmap.get(usernodename)].usernoderemainlist.set(timei,users[username_indexmap.
                    get(usernodename)].usernodedemandlist.get(timei));
            users[username_indexmap.get(usernodename)].usersitedeliver[timei]=new HashMap<>();
        }
    }
    static class Site{
		String sitenodename;
		Integer sitenoedbandsum;//
		int[] sitedemanremain=new int[timelist.size()];//该结点在每一个时刻里的带宽剩余，数组下标为时刻。
		HashMap<Integer,Integer>[] siteuserdeliver=new HashMap[timelist.size()];//边缘结点在每个时刻下给每个用户结点分配的带宽的值。
        void init (int timei){
            sites[sitename_indexmap.get(sitenodename)].siteuserdeliver[timei]=new HashMap<>();
            sites[sitename_indexmap.get(sitenodename)].sitedemanremain[timei]= sites[sitename_indexmap.get(sitenodename)].sitenoedbandsum;
        }


	}
	class Timeble{
		Site site;
		User user;
	}
	public static void readdemand(){
        int dotk=0;
        String demandfile = "/data/demand.csv";
        BufferedReader br = null;
        String line = "";//
        try {
            br = new BufferedReader(new FileReader(demandfile));
            while ((line = br.readLine()) != null) {
                String[] dotmand = line.split(",");
                if(dotk==0){
                    users=new User[dotmand.length-1];
                    for(int i=1;i<dotmand.length;i++){
                        users[i-1]=new User();
                        users[i-1].usernodename=dotmand[i];
                        users[i-1].usersiteqosmap=new HashMap<>();
                        usernodelist.add(dotmand[i]);
                        userindex_namemap.put(i-1,dotmand[i]);
                        username_indexmap.put(dotmand[i],i-1);
                    }
                    dotk++;
                    continue;
                }
                timelist.add(dotmand[0]);
                int k=0;
                for(int j=1;j<dotmand.length;j++){
                    users[k].usernodedemandlist.add(Integer.parseInt(dotmand[j]));
                    users[k].usernoderemainlist.add(Integer.parseInt(dotmand[j]));
                    k++;
                }
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
//        int timedotnum=timedot.size();
//        int dotnums=dotnum-1;
//        dotdemandtable=new int[timedotnum][dotnums];
//        int n=0;
//        int m=0;
//        for(ArrayList<Integer> e:dotdemandlist){
//            for(Integer f:e){
//                dotdemandtable[m][n]=f;
//                n++;
//            }
//            n=0;
//            m++;
//        }
    }
    public static void readsite(){
        int sitek=0;
        String site_band = "/data/site_bandwidth.csv";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(site_band));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] site = line.split(",");
                if(sitek==0){
                    //sites=new Site[site.length];
                    sitek++;
                    continue;
                }
                sitek++;
                sitenamelist.add(site[0]);
                sitebandlist.add(Integer.parseInt(site[1]));
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
        sites=new Site[sitenamelist.size()];
        for(int i=0;i<sitenamelist.size();i++){
            siteindex_namemap.put(i,sitenamelist.get(i));
            sitename_indexmap.put(sitenamelist.get(i),i);
            sites[i]=new Site();
            sites[i].sitenodename=sitenamelist.get(i);
            sites[i].sitenoedbandsum=sitebandlist.get(i);
            sites[i].sitedemanremain=new int[timelist.size()];
            //sites[i].
            //sites[i].sitedemanremain=sitebandlist.get(i);
        }
    }
    public static void readqos(){
        int qosk=0;
        String qosfile = "/data/qos.csv";
        BufferedReader br = null;
        String line = "";
        ArrayList<String> qosuserlist=new ArrayList();
        try {
            br = new BufferedReader(new FileReader(qosfile));
            while ((line = br.readLine()) != null) {
                String[] qoslist = line.split(",");
                if(qosk==0){
                    for(int i=1;i< qoslist.length;i++){
                        qosuserlist.add(qoslist[i]);
                    }
                    qosk++;
                    continue;
                }
                for(int j=1;j<qoslist.length;j++){
                    users[username_indexmap.get(qosuserlist.get(j-1))].usersiteqosmap.
                            put(sitename_indexmap.get(qoslist[0]),Integer.parseInt(qoslist[j]));
                }
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
    public static void readqoscon(){
        int word=0;
        String qosconfile = "/data/config.ini";
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
//	//初始版
//	 for(int i=0;i<timesize;i++){//i为时刻表的下标，也即对应时刻,这里与site的下标一样
//	 String res="";
//	 int itemp=0;//itemp为需要找的网络节点，一般以0位置开头
//	 ArrayList<Integer> bandsitetemp=new ArrayList<>();
//	 bandsitetemp.addAll(bandsite);
//	 for(int j=0;j<dotlist.size();j++){//j为这一时刻的用户节点下标
//	 String dotnametemp=dotlist.get(j);
//	 res=res+dotnametemp+":";
//	 if(dotdemandtable[i][j]==0){//i，j即为当前时刻下的用户节点需要带宽的位置下标
//	 res=res+":";
//	 }else{
//	 while(dotdemandtable[i][j]>0){
//	 int bandvalue=dotdemandtable[i][j];
//	 //若qos>阈值，则寻找下一个可用边缘节点
//
//	 while(qostable[itemp][j]>qosvalue || bandsitetemp.get(itemp)<=0){
//	 if(itemp==bandsite.size()-1){
//	 itemp=0;
//	 }else {
//	 itemp=itemp+1;//对应的qos表，如不符合，则找下一个site
//	 }
//
//	 }                            //
//	 int index=itemp;//这个不固定
//	 String sitenametemp=bandname.get(index);
//	 ArrayList<Integer> nu=new ArrayList<>();
//
//	 if(bandvalue<bandsitetemp.get(index)){
//	 if(sitebandlinetime.containsKey(sitenametemp)){
//	 nu=sitebandlinetime.get(sitenametemp);
//	 }
//	 nu.add(bandvalue);
//	 bandsitetemp.set(index,bandsitetemp.get(index)-bandvalue);
//	 dotdemandtable[i][j]-=bandvalue;
//	 sitebandlinetime.put(sitenametemp,nu);
//	 res=res+"<"+sitenametemp+","+bandvalue+">"+",";
//
//	 }else {
//	 if(sitebandlinetime.containsKey(sitenametemp)){
//	 nu=sitebandlinetime.get(sitenametemp);
//	 }
//	 nu.add(bandsitetemp.get(index));
//	 dotdemandtable[i][j]-=bandsitetemp.get(index);
//	 sitebandlinetime.put(sitenametemp,nu);
//	 res=res+"<"+sitenametemp+","+bandsitetemp.get(index)+">"+",";
//	 bandsitetemp.set(index,0);
//	 }
//	 }
//	 }
//	 res=res.substring(0,res.length()-1);
//	 res=res+"\r\n";
//	 }
//	 resstring=resstring+res;
//	 }
//	 Out out1 = new Out();
//	 out1.output(resstring);
//
//	 Excul excul = new Excul(sitebandlinetime);
//
//
//	 Set<String> e=sitebandlinetime.keySet();
//	 for(String e1:e){
//	 System.out.print("网络节点"+e1+":");
//	 ArrayList<Integer> listtemp=sitebandlinetime.get(e1);
//	 for(Integer e2:listtemp){
//	 System.out.print(","+e2);
//	 }
//	 System.out.println();
//	 }
//
//	 double a=4.9;
//	 int b=0;
//	 if(a>(int)a){
//	 b=(int)a+1;
//	 }else{
//	 b=(int)a;
//	 }//System.out.println(b);向上取整
//
}


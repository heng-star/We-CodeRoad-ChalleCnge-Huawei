package com.huawei.java.main;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		//System.out.println("Hello World");
		Readfile readfile = new Readfile();
		readfile.readsite();
		ArrayList<String> sitenamelist=readfile.sitename;//边缘节点名字
		ArrayList<Integer> sitebandlist=readfile.siteband;//边缘节点得带宽
		readfile.readdot();//该方法得到用户宽带需求表
		ArrayList<String> dottimelist=readfile.timedot;
		readfile.readqos();//该方法得到对应的qos表，用二维数组表示
		int[][] qos=readfile.qosttabe;
		readfile.readqoscon();
		int qoscon=readfile.qos_constraint;
		ArrayList<String> usernodelist=readfile.dotlist;
		int[][] dotdemandtable=readfile.dotdemandtable;
		String resstring="";
		int timesize=dottimelist.size();//多少个时刻
		int usernodenum= usernodelist.size();
		int sitenodenum=sitebandlist.size();
		User[] users = new User[usernodenum];
		for(int i=0;i< usernodelist.size();i++){
			users[i]=new User();
			users[i].usernodename=usernodelist.get(i);
			users[i].usernodeindex=i;
			users[i].usernodeindex_name.put(i,usernodelist.get(i));
			users[i].usernodename_index.put(usernodelist.get(i),i);
			users[i].usernodesite_delivermap=new int[timesize][sitenodenum];
			users[i].user_siteList=new ArrayList[timesize];
			users[i].usersitedeliver=new HashMap[timesize];
		    for(int j=0;j<timesize;j++){
				users[i].usernodedemandlist.add(dotdemandtable[j][i]);
				users[i].user_siteList[j]=new ArrayList<>();
				users[i].usersitedeliver[j]=new HashMap<>();
			}
		}
		Site[] sites=new Site[sitenodenum];
		HashMap<String,Integer> sitename_indexmap=new HashMap<>();
		HashMap<Integer,String> siteindex_namemap=new HashMap<>();
		for(int i=0;i<sitenodenum;i++){
			sites[i]=new Site();
			sitename_indexmap.put(sitenamelist.get(i),i);
			siteindex_namemap.put(i,sitenamelist.get(i));
			sites[i].sitenodeindex=i;
			sites[i].sitenodename=sitenamelist.get(i);
			sites[i].sitenodename_indexmap.put(sitenamelist.get(i),i);
			sites[i].sitenodeindex_namemap.put(i,sitenamelist.get(i));
			sites[i].sitenoedbandsum=sitebandlist.get(i);
			sites[i].sitedemanremain=new int[timesize];//在每个时刻的剩余
			sites[i].siteuserdeliver=new HashMap[timesize];//在每个时刻的分配
			for(int j=0;j<timesize;j++){
				sites[i].sitedemanremain[j]=sitebandlist.get(i);
				sites[i].siteuserdeliver[j]=new HashMap<>();
			}
		}
		for(int m=0;m<qos[0].length;m++){
			for(int n=0;n<qos.length;n++){
				if(qos[n][m]<qoscon){
					users[m].usernodevailbleqossite.add(n);
				}
			}
		}
		String resultstring="";
		String outres="";
		for(int i=0;i<timesize;i++){
			//
			String res="";
			String out="";
			for(int j=0;j<usernodenum;j++){
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
					while (usernodevaluetemp>0){
						int siteindextemp=vailbleqossitetemp.get(k);
						if(usernodevaluetemp<avergetemp){
							avergetemp=usernodevaluetemp;
						}
						if(sites[siteindextemp].sitedemanremain[i]>avergetemp){//可用
							if(sitetempk==0){
								//
								users[j].usernodesite_delivermap[i][siteindextemp]+=avergetemp+avergetemprem;
								users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
										getOrDefault(siteindextemp,0)+avergetemp+avergetemprem);
								sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
										getOrDefault(j,0)+avergetemp+avergetemprem);
								sites[siteindextemp].sitedemanremain[i]-=avergetemp+avergetemprem;
								usernodevaluetemp-=avergetemp+avergetemprem;

								res=res+"<"+sites[siteindextemp].sitenodename+","+(avergetemp+avergetemprem)+">";
								sitetempk++;
							}else{
								//
								users[j].usernodesite_delivermap[i][siteindextemp]+=avergetemp;
								users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
										getOrDefault(siteindextemp,0)+avergetemp);
								sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
										getOrDefault(j,0)+avergetemp);
								sites[siteindextemp].sitedemanremain[i]-=avergetemp;
								usernodevaluetemp-=avergetemp;
								res=res+"<"+sites[siteindextemp].sitenodename+","+avergetemp+">";
							}
						}else if(sites[siteindextemp].sitedemanremain[i]<=avergetemp &&sites[siteindextemp].sitedemanremain[i]>0){
							//
							users[j].usernodesite_delivermap[i][siteindextemp]+=sites[siteindextemp].sitedemanremain[i];
							users[j].usersitedeliver[i].put(siteindextemp,users[j].usersitedeliver[i].
									getOrDefault(siteindextemp,0)+sites[siteindextemp].sitedemanremain[i]);
							sites[siteindextemp].siteuserdeliver[i].put(j,sites[siteindextemp].siteuserdeliver[i].
									getOrDefault(j,0)+sites[siteindextemp].sitedemanremain[i]);
							res=res+"<"+sites[siteindextemp].sitenodename+","+sites[siteindextemp].sitedemanremain[i]+">";
							usernodevaluetemp-=sites[siteindextemp].sitedemanremain[i];
							sites[siteindextemp].sitedemanremain[i]-=sites[siteindextemp].sitedemanremain[i];
						}
						k++;
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
				//System.out.println(res);
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
			}
			resultstring=resultstring+res;
			outres=outres+out;
		}
		//System.out.println(resultstring);
		System.out.println(outres);
		Out out1 = new Out();
		out1.output(outres);
		int ok=0;
	}
	public void solution(){
	}
	static class User{
	    public String usernodename;
	    Integer usernodeindex;
	    ArrayList<Integer> usernodedemandlist=new ArrayList<>();//在每个时刻得需求
	    HashMap<Integer,String> usernodeindex_name=new HashMap<>();
        HashMap<String,Integer> usernodename_index=new HashMap<>();
        ArrayList<Integer>[] user_siteList;
        ArrayList<Integer> usernodevailbleqossite=new ArrayList<>();
        int[][] usernodesite_delivermap;//
		HashMap<Integer,Integer>[] usersitedeliver;//用户节点在每个时刻下的分的边缘节点的值。
    }
    static class Site{
		String sitenodename;
		Integer sitenodeindex;
		Integer sitenoedbandsum;
		int[] sitedemanremain;//该结点在每一个时刻里的带宽剩余，数组下标为时刻。
		HashMap<Integer,String> sitenodeindex_namemap=new HashMap<>();
		HashMap<String,Integer> sitenodename_indexmap=new HashMap<>();
		HashMap<Integer,Integer>[] siteuserdeliver;//边缘结点在每个时刻下给每个用户结点分配的带宽的值。
	}
	class Timeble{
		Site site;
		User user;
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


package com.huawei.java.main;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");
		Readfile readfile = new Readfile();
		readfile.readsite();
		ArrayList<String> sitenamedot=readfile.sitename;
		ArrayList<Integer> sitebanddot=readfile.siteband;
		//时间边缘节点序列
		HashMap<String,ArrayList<Integer>> sitebandlinetime = new HashMap<>();

		int size=sitenamedot.size();//该方法得到边缘节点的名称和带宽
//		for(int i=0;i<size;i++){
//			//System.out.println(sitenamedot.get(i)+","+sitebanddot.get(i));
//		}
		readfile.readdot();//该方法得到用户宽带需求表
		ArrayList<String> dottimelist=readfile.timedot;
		int[][] dot=readfile.dotdemandtable;
//		for(int i=0;i<dot.length;i++){
//			for(int j=0;j<dot[0].length;j++){
//				//System.out.print(dot[i][j]+",");
//			}
//			//System.out.println();
//		}
		readfile.readqos();//该方法得到对应的qos表，用二维数组表示
		int[][] qos=readfile.qosttabe;
//		for(int i=0;i<qos.length;i++){
//			for(int j=0;j<qos[0].length;j++){
//				//System.out.print(qos[i][j]+",");
//			}
//			//System.out.println();
//		}
		readfile.readqoscon();
		int qoscon=readfile.qos_constraint;
		System.out.println(qoscon);
		Out out = new Out();
		out.output("测试创建文件");

		//int timesize=
		ArrayList<String> dotlist=readfile.dotlist;
		int[][] dotdemandtable=readfile.dotdemandtable;
		int[][] dotdemandtableuse=readfile.dotdemandtable;
		int[][] qostable=readfile.qosttabe;
		int qosvalue=readfile.qos_constraint;
		int timesize=dottimelist.size();
		ArrayList<String> bandname=readfile.sitename;
		ArrayList<Integer> bandsite=readfile.siteband;
		String resstring="";

		for(int i=0;i<timesize;i++){//i为时刻表的下标，也即对应时刻,这里与site的下标一样
			String res="";
			for(int j=0;j<dotlist.size();j++){//j为用户标，对应的用户节点
				int itemp=i;  //若qos>阈值，则寻找下一个可用边缘节点
				String dotnametemp=dotlist.get(j);
				res=res+dotnametemp+":";

				while(qostable[i][j]>qosvalue){
					i=i+1;//对应的qos表，如不符合，则找下一个site
				}
				//while ()
				if(qostable[i][j]<=qosvalue){
					int index=i;
					String sitenametemp=bandname.get(index);
					int bandvalue=dotdemandtable[itemp][j];
					if(bandsite.get(index)>bandvalue){
						bandsite.set(index,bandsite.get(index)-bandvalue);
						ArrayList<Integer> nu=new ArrayList<>();
						if(sitebandlinetime.containsKey(sitenametemp)){
							nu=sitebandlinetime.get(sitenametemp);
						}
						nu.add(bandvalue);
						sitebandlinetime.put(sitenametemp,nu);
						dotdemandtable[itemp][j]-=bandvalue;
					}

					res=res+"<"+sitenametemp+","+bandvalue+">"+",";
				}
				i=itemp;
				res=res.substring(0,res.length()-1);
				res=res+"\n";
			}
			resstring=resstring+res;
		}
		Out out1 = new Out();
		out1.output(resstring);
		Set<String> e=sitebandlinetime.keySet();
		for(String e1:e){
			System.out.print("网络节点"+e1+":");
			ArrayList<Integer> listtemp=sitebandlinetime.get(e1);
			for(Integer e2:listtemp){
				System.out.print(","+e2);
			}
			System.out.println();
		}

	}
	public void solution(){
	}
}

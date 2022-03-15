package com.huawei.java.main;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");
		Readfile readfile = new Readfile();
		readfile.readsite();
		ArrayList<String> sitenamedot=readfile.sitename;
		ArrayList<Integer> sitebanddot=readfile.siteband;
		int size=sitenamedot.size();//该方法得到边缘节点的名称和带宽
		for(int i=0;i<size;i++){
			//System.out.println(sitenamedot.get(i)+","+sitebanddot.get(i));
		}
		readfile.readdot();//该方法得到用户宽带需求表
		ArrayList<String> dottimelist=readfile.timedot;
		int[][] dot=readfile.dotdemandtable;
		for(int i=0;i<dot.length;i++){
			for(int j=0;j<dot[0].length;j++){
				//System.out.print(dot[i][j]+",");
			}
			//System.out.println();
		}
		readfile.readqos();//该方法得到对应的qos表，用二维数组表示
		int[][] qos=readfile.qosttabe;
		for(int i=0;i<qos.length;i++){
			for(int j=0;j<qos[0].length;j++){
				//System.out.print(qos[i][j]+",");
			}
			//System.out.println();
		}
		readfile.readqoscon();
		int qoscon=readfile.qos_constraint;
		System.out.println(qoscon);
		Out out = new Out();
		out.output("测试创建文件");

		//int timesize=
		ArrayList<String> dotlist=readfile.dotlist;
		int[][] dotdemandtable=readfile.dotdemandtable;
		int[][] qostable=readfile.qosttabe;
		int qosvalue=readfile.qos_constraint;
		int timesize=dottimelist.size();
		ArrayList<String> bandname=readfile.sitename;
		ArrayList<Integer> bandsite=readfile.siteband;
		String resstring="";


		for(int i=0;i<timesize;i++){//i为时间标，也即对应的边缘节点
			String res="";
			//
			for(int j=0;j<dotlist.size();j++){//j为用户标，对应的用户节点
				int itemp=i;  //若qos>阈值，则寻找下一个可用边缘节点
				String dotnametemp=dotlist.get(j);
				res=res+dotnametemp+":";
				String sitenametemp=bandname.get(i);
				while(qostable[i][j]>qosvalue){
					i=i+1;
				}
				if(qostable[i][j]<=qosvalue){
					int index=0;
					int bandvalue=dotdemandtable[itemp][j];
					if(bandsite.get(index)>bandvalue){
						bandsite.set(index,bandsite.get(index)-bandvalue);
						dotdemandtable[itemp][j]-=bandvalue;
					}
					res=res+"<"+sitenametemp+","+bandvalue+">"+",";
					//System.out.println(res);
				}
				i=itemp;

				//if(j==dotlist.size()-1){
					res=res+"\n";
				//}
			}
			resstring=resstring+res;
		}
		//System.out.println(resstring);
		Out out1 = new Out();
		out1.output(resstring);
	}
	public void solution(){
	}
}

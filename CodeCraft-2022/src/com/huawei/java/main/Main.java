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
	}
}

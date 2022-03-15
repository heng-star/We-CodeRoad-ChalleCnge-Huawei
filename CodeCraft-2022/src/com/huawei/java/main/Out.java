package com.huawei.java.main;

import java.io.*;

public class Out {
    public void output(String str){
        String word="";
        word=word+str;
        StringBuffer text = new StringBuffer();
        BufferedOutputStream buff = null;
        File file = new File("../output/solution.txt");
        //推断是否存在
        if (!file.exists() && !file.isDirectory()){
            file.getParentFile().mkdir();//创建文件父目录
            try {
                System.out.println("目录不存在！");
                file.createNewFile();//创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            //System.out.println("目录存在！   ");
        }
        try {
            Writer fw = new FileWriter(file);
            // 3. 进行写操作
            fw.write(word);
            // 4. 关闭输出
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

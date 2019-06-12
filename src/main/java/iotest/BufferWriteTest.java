package iotest;

import java.io.*;

/**
 * BufferWrite使用
 * @Auther: hys
 * @Date: 2019/6/11
 * @Time: 21:05
 * @Package: iotest
 */
public class BufferWriteTest {
    public static void main(String args[])  {
        String filePath="C:\\Users\\hys\\Desktop\\IP封堵\\command.txt";
       test(filePath," BufferWriteTest ");
    }

    /**
     * 按行追加写如文件
     * @param filePath 文件路径
     * @param bf 要写入的行
     */
    public static void test(String filePath,String bf)  {
        //写入相应的文件
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,true),"GBK"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        //循环取出数据
        try {
            if (out != null) {
                out.write(bf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //清除缓存
        try {
            if (out != null) {
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭流
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

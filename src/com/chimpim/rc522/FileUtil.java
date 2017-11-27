package com.chimpim.rc522;


import java.io.*;

public class FileUtil {

    /**
     * 以追加的形式写文件
     *
     * @param fileName：文件名
     * @param content：内容
     */
    public static void fileAppendWrite(String fileName, String content) {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, true);
            pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) pw.close();
            if (fw != null) try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

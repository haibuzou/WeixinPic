
package com.haibuzou.piclibrary.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：(文件工具类)
 *
 * @author fan
 */

public class FileInfoUtil {
    /**
     * 返回自定文件或文件夹的大小
     *
     * @param f
     * @return
     * @throws Exception
     */

    public static long getFileSizes(File f) throws Exception {
        // 取得文件大小
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    // 递归
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getlist(File f) {
        // 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }

    //读取表情配置文件
    public static List<String> getEmojiFile(Context context) {

        List<String> emojiList = new ArrayList<String>();
        if (context != null) {
            try {
                InputStream in = context.getAssets().open("emoji");
                BufferedReader buffRead = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String str = null;
                while ((str = buffRead.readLine()) != null) {
                    emojiList.add(str);
                }
            } catch (Exception e) {

            }
        }

        return emojiList;
    }
}

package com.example.io;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by MintQ on 2018/6/4.
 * 第十二讲、Java有几种文件拷贝方式?哪一种最高效？
 * 总体上来说，NIO transferTo/transferFrom 的方式可能更快，因为它更能利用现在操作系统底层机制，避免不必要拷贝和上下文切换。
 *
 */
public class FileCopy {


    /**
     * 利用java.io 类库，直接为源文件构建一个FileInputStream读取，然后再为目标文件构建一个FileOutputStream，完成写入工作
     * @param source
     * @param dest
     */
    public static void copyFileByStream(File source, File dest) {
        try {
            InputStream is = new FileInputStream(source);
            OutputStream os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer,0,length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 或者，利用java.nio类库提供的 transferTo 或 transferFrom方法实现。
     * @param source
     * @param dest
     */
    public static void copyFileByChannel(File source,File dest) {
        try {
            FileChannel sourceChannel = new FileInputStream(source).getChannel();
            FileChannel targetChannel = new FileOutputStream(dest).getChannel();
            for (long count = sourceChannel.size(); count>0 ; ) {
                long transferred = sourceChannel.transferTo(sourceChannel.position(),count,targetChannel);
                sourceChannel.position(sourceChannel.position() + transferred);
                count -= transferred;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

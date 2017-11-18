package com.ctosb.study.zip;

import java.io.*;
import java.util.zip.*;

/**
 * zip工具
 *
 * @author Alan
 */
public class ZipUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * zip压缩文档
     *
     * @param sourceName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午5:19:22
     */
    public static void zip(String sourceName) throws IOException {
        zip(new File(sourceName));
    }

    /**
     * zip压缩文档
     *
     * @param sourceFile
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午5:19:22
     */
    public static void zip(File sourceFile) throws IOException {
        zip(sourceFile, new File(sourceFile.getParentFile(), sourceFile.getName() + ".zip"));
    }

    /**
     * zip压缩文档
     *
     * @param sourceName
     * @param destinationName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午5:18:43
     */
    public static void zip(String sourceName, String destinationName) throws IOException {
        zip(new File(sourceName), new File(destinationName));
    }

    /**
     * zip压缩文档
     *
     * @param sourceFile
     * @param destinationFile
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午5:18:43
     */
    public static void zip(File sourceFile, File destinationFile) throws IOException {
        // 对输出文件做CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destinationFile), new CRC32());
        ZipOutputStream zipOutputStream = new ZipOutputStream(cos);
        try {
            // 执行zip压缩
            zip(sourceFile, zipOutputStream, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            zipOutputStream.flush();
            zipOutputStream.close();
        }

    }

    /**
     * 解压zip文件
     *
     * @param sourceName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午10:17:52
     */
    public static void unzip(String sourceName) throws IOException {
        unzip(new File(sourceName));
    }

    /**
     * 解压zip文件
     *
     * @param sourceFile
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午10:17:52
     */
    public static void unzip(File sourceFile) throws IOException {
        unzip(sourceFile, sourceFile.getParentFile());
    }

    /**
     * 解压zip文件，并指定目的文件夹
     *
     * @param sourceName
     * @param destinationName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午10:18:11
     */
    public static void unzip(String sourceName, String destinationName) throws IOException {
        unzip(new File(sourceName), new File(destinationName));
    }

    /**
     * 解压zip文件
     *
     * @param sourceName
     * @param destinationName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午10:18:11
     */
    public static void unzip(File sourceFile, File dirFile) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
        ZipEntry zipEntry = null;
        // 循环获取zip流的entry文件条目
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            File file = new File(dirFile, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                file.mkdirs();
            } else {
                //解压文件内容
                unzipToFile(zipInputStream, file);
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }

    /**
     * @param zipInputStream
     * @param toFile
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午10:31:43
     */
    private static void unzipToFile(ZipInputStream zipInputStream, File toFile) throws IOException {
        byte[] byteArray = new byte[BUFFER_SIZE];
        int count;
        makeParentdirectorys(toFile);// 构建父级目录
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(toFile));
        // 读取解压文件信息，并写入到文件中
        while ((count = zipInputStream.read(byteArray, 0, BUFFER_SIZE)) != -1) {
            bufferedOutputStream.write(byteArray, 0, count);
        }
        bufferedOutputStream.close();
        zipInputStream.closeEntry();
    }

    /**
     * 创建父级目录
     *
     * @param file
     * @author Alan
     * @createTime 2015-11-29 下午10:28:21
     */
    private static void makeParentdirectorys(File file) {
        File directoryFile = file.getParentFile();
        // 校验父级目录是否存在
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
    }

    /**
     * 压缩
     *
     * @param sourceFile
     * @param zipOutputStream
     * @param dirName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午4:32:17
     */
    private static void zip(File sourceFile, ZipOutputStream zipOutputStream, String dirName) throws IOException {
        if (sourceFile.isDirectory()) {
            // 压缩文件夹
            zipDirectory(sourceFile, zipOutputStream, dirName);
        } else {
            // 压缩文件
            zipFile(sourceFile, zipOutputStream, dirName);
        }
    }

    /**
     * 压缩目录
     *
     * @param sourceFile
     * @param zipOutputStream
     * @param destinationName
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午4:31:49
     */
    private static void zipDirectory(File sourceFile, ZipOutputStream zipOutputStream, String destinationName) throws IOException {
        // 如果是目录文件
        File[] files = sourceFile.listFiles();// 获取目录下子文件
        if (files.length <= 0) {
            // 该目录为空目录，压缩空目录需要添加"/"
            zipOutputStream.putNextEntry(new ZipEntry(destinationName + sourceFile.getName() + "/"));
            zipOutputStream.closeEntry();
        }
        // 遍历递归其子文件
        for (File file : files) {
            zip(file, zipOutputStream, destinationName + sourceFile.getName() + File.separator);
        }
    }

    /**
     * 压缩文件
     *
     * @param sourceFile
     * @param zipOutputStream
     * @param dirPath
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-29 下午4:32:04
     */
    private static void zipFile(File sourceFile, ZipOutputStream zipOutputStream, String dirPath) throws IOException {
        int count;
        byte[] byteArray = new byte[BUFFER_SIZE];
        zipOutputStream.putNextEntry(new ZipEntry(dirPath + sourceFile.getName() + File.separator));
        // 构建爱你缓冲输入流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        while ((count = bufferedInputStream.read(byteArray, 0, BUFFER_SIZE)) != -1) {
            // 将读出的字节写入输出流中
            zipOutputStream.write(byteArray, 0, count);
        }

        bufferedInputStream.close();
        zipOutputStream.closeEntry();
    }


    public static void main(String[] args) throws IOException {
        ZipUtil.zip("H:\\wms");
        ZipUtil.unzip("H:\\wms.zip");
    }
}

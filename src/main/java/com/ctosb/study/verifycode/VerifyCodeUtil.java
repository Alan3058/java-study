package com.ctosb.study.verifycode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class VerifyCodeUtil {

    /**
     * 生成验证码的源字符串。 去除0，o，O，1，l容易混淆的字符
     */
    private final static String DEFAULT_VERIFY_CODE_CHAR = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
    /**
     * 默认生成字符数量
     */
    private final static int DEFAULT_GENERATE_SIZE = 4;
    /**
     * 默认颜色数组
     */
    private final static Color[] COLORS = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};

    private static Random random = new Random();

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param size
     * @return
     * @author Alan
     * @createTime 2015-11-26 下午10:50:07
     */
    public static String generateVerifyCode() {
        return generateVerifyCode(DEFAULT_GENERATE_SIZE);
    }

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param size
     * @return
     * @author Alan
     * @createTime 2015-11-26 下午10:50:07
     */
    public static String generateVerifyCode(int size) {
        return generateVerifyCode(size, DEFAULT_VERIFY_CODE_CHAR);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param size   验证码长度
     * @param source 验证码字符源
     * @return
     * @author Alan
     * @createTime 2015-11-26 下午10:50:07
     */
    public static String generateVerifyCode(int size, String source) {
        if (source == null || source.length() == 0) {
            source = DEFAULT_VERIFY_CODE_CHAR;// 默认为指定字符源
        }
        if (size <= 0) {
            size = DEFAULT_GENERATE_SIZE;// 默认为指定字符数
        }

        int codesLen = source.length();
        StringBuffer verifyCode = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            // 获取源字符串的随机字符组合
            verifyCode.append(source.charAt(random.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 验证码生成图片并输出到文件中
     *
     * @param width      图片宽
     * @param height     图片高
     * @param file       输出文件
     * @param verifyCode 验证码
     * @author Alan
     * @createTime 2015-11-26 下午10:50:07
     */
    public static void generateImage(int width, int height, File file, String verifyCode) {
        File parentDir = file.getParentFile();// 获取上级目录文件
        if (!parentDir.exists()) {
            parentDir.mkdirs();// 不存在则重新生成
        }
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();// 创建文件
            fileOutputStream = new FileOutputStream(file);// 构建文件输出流
            generateImage(width, height, fileOutputStream, verifyCode);// 将验证码输出到输出流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证码生成图片并输出到输出流中
     *
     * @param width        图片宽
     * @param height       图片高
     * @param outputStream 输出流
     */
    public static void generateImage(int width, int height, OutputStream outputStream) {
        generateImage(width, height, outputStream, generateVerifyCode());
    }

    /**
     * 验证码生成图片并输出到输出流中
     *
     * @param width        图片宽
     * @param height       图片高
     * @param outputStream 输出流
     * @param verifyCode   验证码
     */
    public static void generateImage(int width, int height, OutputStream outputStream, String verifyCode) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setBackground(Color.WHITE);// 设置背景色
        graphics.clearRect(0, 0, width, height);// 清除图片，重新刷新背景色
        // 画干扰线条
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < 10; i++) {
            graphics.drawLine(random.nextInt(width - 1), random.nextInt(height - 1), random.nextInt(width - 1), random.nextInt(height - 1));
        }
        // 画干扰点
        for (int i = 0; i < 50; i++) {
            graphics.drawOval(random.nextInt(width - 1), random.nextInt(height - 1), 1, 1);
        }

        // 画字符
        char[] verifyCodes = verifyCode.toCharArray();
        for (int i = 0; i < verifyCodes.length; i++) {
            // 设置字体
            graphics.setFont(new Font(Font.SERIF, (Font.ITALIC | Font.BOLD), random.nextInt(5) + 25));
            // 每个字符设置随机颜色
            graphics.setColor(COLORS[random.nextInt(COLORS.length)]);
            // 保持字符离左右边10个像素
            graphics.drawChars(verifyCodes, i, 1, ((width - 20) * i / verifyCodes.length) + 10, height / 2);
        }

        try {
            // 将图片写入输出流中
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File dir = new File("f://verifyFiles");
        for (int i = 0; i < 50; i++) {
            String verifyCode = generateVerifyCode();
            generateImage(100, 50, new File(dir, verifyCode + ".png"), verifyCode);
        }

    }
}

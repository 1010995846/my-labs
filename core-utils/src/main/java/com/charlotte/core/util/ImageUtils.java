package com.charlotte.core.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * <!-- https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox -->
 * <dependency>
 * <groupId>org.apache.pdfbox</groupId>
 * <artifactId>pdfbox</artifactId>
 * <version>2.0.20</version>
 * </dependency>
 * <!-- https://mvnrepository.com/artifact/org.apache.pdfbox/fontbox -->
 * <dependency>
 * <groupId>org.apache.pdfbox</groupId>
 * <artifactId>fontbox</artifactId>
 * <version>2.0.20</version>
 * </dependency>
 *
 * @author Charlotte
 */
public class ImageUtils {

    public static final float DEFAULT_DPI = 105;
    //默认转换的图片格式为jpeg
    public static final String DEFAULT_FORMAT = "jpeg";


    public static BufferedImage readPdf(byte[] bytes) throws IOException {
        PDDocument pdDocument = PDDocument.load(bytes);
        return readPdf(pdDocument);
    }

    public static BufferedImage readPdf(File file) throws IOException {
        PDDocument pdDocument = PDDocument.load(file);
        return readPdf(pdDocument);
    }

    public static BufferedImage readPdf(String pdfPath) throws IOException {
        PDDocument pdDocument = PDDocument.load(new File(pdfPath));
        return readPdf(pdDocument);
    }

    public static BufferedImage readPdf(InputStream inputStream) throws IOException {
        PDDocument pdDocument = PDDocument.load(inputStream);
        return readPdf(pdDocument);
    }

    public static BufferedImage readPdf(PDDocument pdDocument) throws IOException {
        // 利用PdfBox生成图像
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        // 循环每个页码
        int page = pdDocument.getNumberOfPages();
        int[][] singleImgRGBArray = new int[page][];
        // 宽度
        int width = 0;
        // 每一页的高度上下边界值
        int[] shiftHeight = new int[page + 1];
        for (int i = 0; i < page; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();
            width = imageWidth;
            shiftHeight[i + 1] = shiftHeight[i] + imageHeight;
            // 计算高度和偏移量
            // 使用第一张图片宽度;
            singleImgRGBArray[i] = image.getRGB(0, 0, width, imageHeight, null, 0, width);
        }

        // 保存每张图片的像素值
        BufferedImage imageResult = new BufferedImage(width, shiftHeight[page], BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < singleImgRGBArray.length; i++) {
            int[] singleImgRGB = singleImgRGBArray[i];
            imageResult.setRGB(0, shiftHeight[i], width, shiftHeight[i + 1] - shiftHeight[i], singleImgRGB, 0, width);
        }
        pdDocument.close();
        return imageResult;
    }

    public static void writeImage(BufferedImage bufferedImage, String imgPath) throws IOException {
        ImageIO.write(bufferedImage, DEFAULT_FORMAT, new File(imgPath));
    }

    public static String toBase64(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, DEFAULT_FORMAT, stream);
        String base64 = Base64.encode(stream.toByteArray());
        return base64;
    }

}

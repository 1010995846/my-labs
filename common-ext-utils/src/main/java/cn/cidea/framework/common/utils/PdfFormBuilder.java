package cn.cidea.framework.common.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PDF表单编辑工具
 * itextpdf、itext-asian
 *
 * @author Charlotte
 */
@Slf4j
public class PdfFormBuilder {

    private File source;

    private Map<String, Text> textMap = new HashMap<>();

    /**
     * 全局字体
     */
    private BaseFont textFont;

    private Map<String, Image> imageMap = new HashMap<>();

    public PdfFormBuilder(File source) {
        if (source == null) {
            throw new RuntimeException("file can not be null!");
        } else if (!source.exists()) {
            throw new RuntimeException("file is not find!");
        }
        this.source = source;
    }

    /**
     * 注意图片也要文本域
     */
    public PdfFormBuilder putImageFromUrl(String name, String url) {
        if (StringUtils.isBlank(url)) {
            return this;
        }
        try {
            Image image = Image.getInstance(new URL(StringFormatUtils.encodeChinese(url)));
            imageMap.put(name, image);
        } catch (Exception e) {
            log.error("read image url error, url = {}", url);
        }
        return this;
    }

    public PdfFormBuilder putText(String name, Text text) {
        textMap.put(name, text);
        return this;
    }

    public PdfFormBuilder putText(String name, String value) {
        textMap.put(name, Text.of(value));
        return this;
    }

    public PdfFormBuilder putText(String name, Object value) {
        textMap.put(name, Text.of(String.valueOf(value)));
        return this;
    }

    public PdfFormBuilder putText(Map<String, Object> map) {
        map.entrySet().forEach(entry -> putText(entry.getKey(), entry.getValue()));
        return this;
    }

    /**
     * 宋体
     * TODO 使用项目中自带的字体
     */
    public PdfFormBuilder simsun() throws DocumentException, IOException {
        textFont = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        return this;
    }

    /**
     * tomcat-embed-core
     */
    public void write(HttpServletResponse response, String fileName) throws IOException, DocumentException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "filename=" + new String(fileName.getBytes(), "iso8859-1"));
        write(response.getOutputStream());
    }

    public void write(File targetFile) throws IOException, DocumentException {
        write(new FileOutputStream(targetFile));
    }

    public void write(OutputStream os) throws IOException, DocumentException {
        try {
            byte[] file = FileUtils.readFileToByteArray(source);
            PdfReader reader = null;
            PdfStamper stamper = null;
            try {
                reader = new PdfReader(file);

                stamper = new PdfStamper(reader, os);
                // 不可编辑
                stamper.setFormFlattening(true);
                AcroFields acroFields = stamper.getAcroFields();
                if (textFont == null) {
                    textFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                }

                for (Map.Entry<String, Text> entry : textMap.entrySet()) {
                    String fieldName = entry.getKey();
                    Text text = entry.getValue();
                    acroFields.setField(fieldName, text.value);
                    acroFields.setFieldProperty(fieldName, "textfont", ObjectUtils.firstNonNull(text.font, textFont), null);
                    acroFields.setFieldProperty(fieldName, "textsize", text.textsize, null);
                }
                // acroFields.setField("tr", "On", true)
                for (Map.Entry<String, Image> entry : imageMap.entrySet()) {
                    String fieldName = entry.getKey();
                    Image image = entry.getValue();
                    // 通过域名获取所在页和坐标，左下角为起点
                    List<AcroFields.FieldPosition> positionList = acroFields.getFieldPositions(fieldName);
                    if (positionList == null || positionList.size() == 0) {
                        throw new RuntimeException("ImageField[" + fieldName + "]异常");
                    }
                    int pageNo = positionList.get(0).page;
                    Rectangle signRect = positionList.get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 获取操作的页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    // 根据域的大小缩放图片
                    // image.scaleToFit(signRect.getWidth() / 4, signRect.getHeight() / 4);
                    // // 添加图片并设置位置（个人通过此设置使得图片垂直水平居中，可参考，具体情况已实际为准）
                    // image.setAbsolutePosition(x / 2 - image.getWidth() / 2, y / 2 + image.getHeight());
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            } finally {
                if (stamper != null) {
                    try {
                        stamper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Text {

        private String value;

        private BaseFont font;

        private Float textsize = 11F;

        public static Text of(String value) {
            return new Text().setValue(value);
        }
    }
}

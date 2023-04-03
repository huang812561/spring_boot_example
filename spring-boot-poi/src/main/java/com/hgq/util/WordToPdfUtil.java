package com.hgq.util;

import com.aspose.words.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-06-14 13:36
 * @since 1.0
 **/
public class WordToPdfUtil {
    public static void main(String[] args) throws Exception {
        String filePath = "D:\\开放接口API模板.docx";
//        WordToPdfUtil wordToPdfUtil = new WordToPdfUtil();
//        wordToPdfUtil.file2pdf("D:\\", "开放接口API模板", ".docx");

        docToPdf(filePath);

    }

    public static String docToPdf(String filePath) throws Exception {
        if (Objects.isNull(filePath)) {
            return null;
        }

        File file = new File(filePath);
        String type = file.getAbsolutePath().substring(filePath.lastIndexOf("."));
        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String toFilePath = file.getParent();

        String newFileName = "";
        //获取转换成PDF之后文件名
        if (".doc".equals(type) || ".docx".equals(type)) {
            newFileName = fileName + ".pdf";
        } else {
            return null;
        }
        //获取Doc文档对象模型
        Document doc = new Document(filePath);
        File pdfOutputFile = new File(toFilePath + File.separatorChar + newFileName);
        //获取文件输出流
        FileOutputStream os = new FileOutputStream(pdfOutputFile);
        //为doc文档添加水印
//        insertWatermarkText(doc, "于文珂");
        //将doc文旦转换成PDF文件并输出到之前创建好的pdf文件中
        doc.save(os, SaveFormat.PDF);
        //关闭输出流
        if (os != null) {
            os.close();
        }
        return newFileName;
    }

    /**
     * @param toFilePath 文件夹路径
     * @param fileName   文件名
     * @param type       文件类型
     * @return
     * @throws Exception
     */
    public String file2pdf(String toFilePath, String fileName, String type) throws Exception {
        String htmFileName = null;

        //通过转换之后的PDF文件名,创建PDF文件
        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        //获取文件输出流
        FileOutputStream os = new FileOutputStream(htmlOutputFile);
        //获取Doc文档对象模型
        Document doc = new Document(toFilePath + File.separatorChar + fileName + type);
        //为doc文档添加水印
//        insertWatermarkText(doc, "于文珂");
        //将doc文旦转换成PDF文件并输出到之前创建好的pdf文件中
        doc.save(os, SaveFormat.PDF);
        //关闭输出流
        if (os != null) {
            os.close();
        }
        return htmFileName;
    }

    /**
     * 为word文档添加水印
     *
     * @param doc           word文档模型
     * @param watermarkText 需要添加的水印字段
     * @throws Exception
     */
    private static void insertWatermarkText(Document doc, String watermarkText) throws Exception {
        /*Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        //水印内容
        watermark.getTextPath().setText(watermarkText);
        //水印字体
        watermark.getTextPath().setFontFamily("宋体");
        //水印宽度
        watermark.setWidth(500);
        //水印高度
        watermark.setHeight(100);
        //旋转水印
        watermark.setRotation(-40);
        //水印颜色
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);
        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark.setWrapType(WrapType.NONE);
        watermark.setVerticalAlignment(VerticalAlignment.CENTER);
        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);
        for (Section sect : doc.getSections())
        {
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
        System.out.println("Watermark Set");*/
    }

    /**
     * 在页眉中插入水印
     *
     * @param watermarkPara
     * @param sect
     * @param headerType
     * @throws Exception
     */
    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType) throws Exception {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
        if (header == null) {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }
        header.appendChild(watermarkPara.deepClone(true));
    }
}

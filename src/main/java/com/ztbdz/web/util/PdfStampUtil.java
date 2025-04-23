package com.ztbdz.web.util;
import com.ztbdz.web.config.SystemConfig;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * PDF盖章
 */
public class PdfStampUtil {

    /**
     * 在PDF的每一页右下角添加电子章
     * @param inputPdfPath  输入PDF路径
     * @param stampImagePath 电子章图片路径（PNG格式，透明背景）
     * @param outputPdfPath 输出PDF路径
     * @param x_axis 盖章位置X轴
     * @param y_axis 盖章位置Y轴
     */
    public static void addStampToPdf(String inputPdfPath, String stampImagePath, String outputPdfPath,Float x_axis,Float y_axis) throws IOException {
        try (PDDocument document = PDDocument.load(new File(inputPdfPath))) {
            // 加载电子章图片
            PDImageXObject stampImage = PDImageXObject.createFromFile(stampImagePath, document);

            // 遍历所有页面
            for (PDPage page : document.getPages()) {
                // 获取页面尺寸
                PDRectangle mediaBox = page.getMediaBox();
                float pageWidth = mediaBox.getWidth();
                float pageHeight = mediaBox.getHeight();

                // 计算盖章位置（右下角，边距20px）
                float margin = 20;
                float imageWidth = 100;  // 电子章宽度（按需调整）
                float imageHeight = (stampImage.getHeight() * imageWidth) / stampImage.getWidth();  // 按比例缩放高度

//                float x = pageWidth - imageWidth - margin;
//                float y = margin;  // PDF坐标系原点在左下角
                float x = pageWidth -SystemConfig.X_AXIS;
                float y = SystemConfig.Y_AXIS;  // PDF坐标系原点在左下角
                if(!StringUtils.isEmpty(x_axis)) x = pageWidth -x_axis;
                if(!StringUtils.isEmpty(y_axis)) y =  y_axis;

                // 在页面上绘制电子章
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    contentStream.drawImage(stampImage, x, y, imageWidth, imageHeight);
                }
            }

            // 保存修改后的PDF
            document.save(outputPdfPath);
        }
    }

}

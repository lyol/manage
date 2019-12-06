package com.lyl.layuiadmin.util.aspose;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.lyl.layuiadmin.enums.FileCnversionTypeEnum;
import com.lyl.layuiadmin.pojo.aspose.FileCnversionDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WordToOthers {

    /**
     * word转换
     * 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
     *
     * @param
     */
    public static FileCnversionDO wordToOthers(FileCnversionDO fc) {
        //默认失败
        fc.setStatus(0);
        if (!AsposeUtil.getLicense(AsposeUtil.LICENSE_TYPE.WORD)) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            log.info("验证失败,会产生水印");
        }
        try {
            //创建一个doc对象
            Document doc = null;
            //统计时间
            StopWatch stopWatch = StopWatch.createStarted();
            ImageSaveOptions iso = null;
            //输出流
            FileOutputStream os = null;
            //初始化定义
            File file = null;
            //判断是否转换图片
            if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToJPEG)) {
                iso = new ImageSaveOptions(SaveFormat.JPEG);
            } else {
                //file.mkdirs();
                //新建一个空白文档
                file = new File(fc.getOutputFilePath());
                //创建文件夹
                file.mkdirs();
                file = new File(fc.getOutputFilePath() + fc.getFileNameAfter());
                os = new FileOutputStream(file);
            }
            //getInputfile是将要被转化文档
            doc = new Document(fc.getInputFilePath());
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            //------------------------------------------逻辑判断
            //>>>>>>DocToPDF
            if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToPDF)) {
                doc.save(os, SaveFormat.PDF);
            }//>>>>>>DocToDocx
            else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToDocx)) {
                doc.save(os, SaveFormat.DOCX);
            }//>>>>>>DocToTexT
            else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToTexT)) {
                doc.save(os, SaveFormat.TEXT);
            }//>>>>>>DocToXps
            else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToXps)) {
                doc.save(os, SaveFormat.XPS);
            }//>>>>>>HtmlToDoc
            else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.HtmlToDoc)) {
                doc.save(os, SaveFormat.DOC);
            }//>>>>>>DocToJPEG
            else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocxToDoc)) {
                doc.save(os, SaveFormat.DOC);
            } else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocxToPDF)) {
                doc.save(os, SaveFormat.PDF);
            } else if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.DocToJPEG)) {
                iso.setPrettyFormat(true);
                iso.setUseAntiAliasing(true);
                for (int i = 0; i < doc.getPageCount(); i++) {
                    iso.setPageIndex(i);
                    doc.save(fc.getOutputFilePath() + fc.getFileNameAfter() + "---" + (i + 1) + ".jpeg", iso);
                }
            }
            //统计时间
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            //设置时间
            fc.setTimeConsuming(((time / 1000.0) + "").trim());
            //成功
            fc.setStatus(0);
            if (os != null){
                os.close();
            }
            log.info("转换成功：" + fc.getFileCnversionType() + ",耗时：" + (time / 1000.0) + "秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return fc;
    }
}

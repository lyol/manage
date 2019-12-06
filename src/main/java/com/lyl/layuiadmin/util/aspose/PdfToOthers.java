package com.lyl.layuiadmin.util.aspose;

import com.aspose.pdf.*;
import com.lyl.layuiadmin.enums.FileCnversionTypeEnum;
import com.lyl.layuiadmin.pojo.aspose.FileCnversionDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PdfToOthers {

    public static FileCnversionDO PdfToOthers(FileCnversionDO vo) {
        vo.setStatus(1);
        if (!AsposeUtil.getLicense(AsposeUtil.LICENSE_TYPE.PDF)) {
            log.info("验证失败，产生水印！");
        }

        Document doc = null;
        try {
            //统计时间
            StopWatch stopWatch = StopWatch.createStarted();
            doc = new Document(vo.getInputFilePath());
            //创建一个空白文件夹
            File file = null;
            FileOutputStream os = null;
            file = new File(vo.getOutputFilePath());
            //创建文件夹
            file.mkdirs();
            file = new File(vo.getOutputFilePath() + vo.getFileNameAfter());
            os = new FileOutputStream(file);
            if (vo.getFileCnversionType().equals(FileCnversionTypeEnum.PdfToDocx)) {
                DocSaveOptions saveOptions = new DocSaveOptions();
                saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
                doc.save(os, SaveFormat.DocX);
            } else if (vo.getFileCnversionType().equals(FileCnversionTypeEnum.PdfToExcel)) {
                ExcelSaveOptions saveOptions = new ExcelSaveOptions();
                doc.save(os, saveOptions);
            } else if (vo.getFileCnversionType().equals(FileCnversionTypeEnum.PdfToPpt)) {
                PptxSaveOptions saveoptions = new PptxSaveOptions();
                doc.save(os, saveoptions);
            }
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            //设置时间
            vo.setTimeConsuming(((time / 1000.0) + "").trim());
            //成功
            vo.setStatus(0);
            if (os != null){
                os.close();
            }
            log.info("转换成功：" + vo.getFileCnversionType() + ",耗时：" + (time / 1000.0) + "秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return vo;
    }
}
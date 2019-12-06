package com.lyl.layuiadmin.util.aspose;

import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import com.lyl.layuiadmin.enums.FileCnversionTypeEnum;
import com.lyl.layuiadmin.pojo.aspose.FileCnversionDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsposeUtil {

    public enum LicenseTypeEnum {}

    public enum LICENSE_TYPE {
        PDF("PDF"), WORD("WORD"), EXCEL("EXCEL");
        String value;

        private LICENSE_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static InputStream getInputStream() throws Exception {
        String license = "<License>\n" +
                "    <Data>\n" +
                "        <Products>\n" +
                "            <Product>Aspose.Total for Java</Product>\n" +
                "            <Product>Aspose.Words for Java</Product>\n" +
                "        </Products>\n" +
                "        <EditionType>Enterprise</EditionType>\n" +
                "        <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                "        <LicenseExpiry>20991231</LicenseExpiry>\n" +
                "        <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                "    </Data>\n" +
                "    <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                "</License>";
        InputStream is = new ByteArrayInputStream(license.getBytes("UTF-8"));
        return is;
    }


    public static boolean getLicense(LICENSE_TYPE licenseType) {
        boolean result = false;
        try {
            // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            InputStream is = AsposeUtil.class.getClassLoader().getResourceAsStream("license.xml");
            if (licenseType == LICENSE_TYPE.WORD) {
                com.aspose.words.License aposeLic = new com.aspose.words.License();
                aposeLic.setLicense(is);
            } else if (licenseType == LICENSE_TYPE.EXCEL) {
                com.aspose.cells.License lic = new com.aspose.cells.License();
                lic.setLicense(is);
            } else {
                com.aspose.pdf.License l = new com.aspose.pdf.License();
                l.setLicense(getInputStream());
            }
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param wordPath 需要被转换的word全路径带文件名
     * @param pdfPath  转换之后pdf的全路径带文件名
     */
    public static void word2pdf(String wordPath, String pdfPath) {
        if (!getLicense(LICENSE_TYPE.WORD)) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            StopWatch stopWatch = StopWatch.createStarted();
            File file = new File(pdfPath); //新建一个pdf文档
            FileOutputStream os = new FileOutputStream(file);
            //Address是将要被转化的word文档
            Document doc = new Document(wordPath);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, com.aspose.words.SaveFormat.PDF);
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            os.close();
            log.info("共耗时：" + (time / 1000.0) + "秒"); //转化用时
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param excelPath 需要被转换的excel全路径带文件名
     * @param pdfPath   转换之后pdf的全路径带文件名
     */
    public static void excel2pdf(String excelPath, String pdfPath) {
        if (!getLicense(LICENSE_TYPE.EXCEL)) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            StopWatch stopWatch = StopWatch.createStarted();
            Workbook wb = new Workbook(excelPath);// 原始excel路径
            FileOutputStream fileOS = new FileOutputStream(new File(pdfPath));
            wb.save(fileOS, com.aspose.cells.SaveFormat.PDF);
            fileOS.close();
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            log.info("共耗时：" + (time / 1000.0) + "秒"); //转化用时
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * excel转换
     */
    public static FileCnversionDO excelToPdf(FileCnversionDO fc) {
        fc.setStatus(1);
        if (!getLicense(LICENSE_TYPE.EXCEL)) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            log.info("验证失败,会产生水印");
            return fc;
        }
        try {
            //统计时间
            StopWatch stopWatch = StopWatch.createStarted();
            //创建一个空白文件夹
            File file = null;
            Workbook wb = new Workbook(fc.getInputFilePath());// 原始excel路径
            FileOutputStream os = null;
            file = new File(fc.getOutputFilePath());
            //创建文件夹
            file.mkdirs();
            file = new File(fc.getOutputFilePath() + fc.getFileNameAfter());
            os = new FileOutputStream(file);
            if (fc.getFileCnversionType().equals(FileCnversionTypeEnum.ExcelToPDF)) {
                wb.save(os, com.aspose.cells.SaveFormat.PDF);
            }
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            //设置时间
            fc.setTimeConsuming(((time / 1000.0) + "").trim());
            //成功
            fc.setStatus(0);
            if (os != null){
                os.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return fc;
    }

    public static void main(String[] args) {
        //word 和excel 转为pdf
        String filePaths = "E:\\temp\\temp\\doc.doc";
        String pdfPath = "E:\\temp\\temp\\2019.pdf";
        //doc2pdf(filePaths, pdfPath);//filePaths需要转换的文件位置 pdfPath为存储位置
        String excel2pdf = "E:\\temp\\temp\\2019.xlsx";
        //excel2pdf(excel2pdf, pdfPath);
        //word2pdf(filePaths, pdfPath);

        FileCnversionDO fcDo = new FileCnversionDO();
        fcDo.setFileCnversionType(FileCnversionTypeEnum.ExcelToPDF);
        fcDo.setInputFilePath("E:/temp/temp/2019.xls");
        fcDo.setOutputFilePath("E:/temp/temp/");
        fcDo.setFileNameAfter("2019.pdf");
        //PdfToOthers.PdfToOthers(fcDo);
        //wordToOthers(fcDo);
        excelToPdf(fcDo);
    }

}

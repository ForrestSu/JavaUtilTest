package org;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Hello world!
 */
public class App {

    /**
     * 校验字符串是否符合日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isVaildDate(String strDate, String format) {
        boolean isok = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            df.parse(strDate);
            isok = true;
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return isok;
    }

    public static void testUUId()
    {
        //转化为String对象
        String uuid = UUID.randomUUID().toString();
        // 打印UUID
        System.out.println(uuid);
        uuid = uuid.replace("-", "");
        //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        System.out.println(uuid);
    }


    public static void LoadXlsx(String excelFile) {

        try (FileInputStream input = new FileInputStream(excelFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(input))
        {
            AnalysisEventListener<List<String>> objectAnalysisEventListener = new AnalysisEventListener<List<String>>() {
                @Override
                public void invoke(List<String> list, AnalysisContext analysisContext) {
                    System.out.println("size = "+ list.size() +" list == "+ list );
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    System.out.println("doAfterAllAnalysed");
                }
            };

            ExcelReader excelReader = new ExcelReader(bufferedInputStream, null, objectAnalysisEventListener);

            System.out.println("start read...");
            excelReader.read();
            System.out.println("excelReader.read()!!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testCommonsIo(){

        String file_name = "test.txt";
        file_name.trim();

        String prefix = FilenameUtils.getBaseName(file_name);

        System.out.println("prefix == <" + prefix+">");

        try {
            Date dt = DateUtils.parseDate("20190532", "yyyyMMdd");
            System.out.println(dt.toLocaleString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void testMd5(){

        //FileUtils.checksumCRC32()
    }

    public static void main(String[] args) {
       // System.out.println("Hello World!");
       // testCommonsIo();
       // testUUId();

        String excelFile = "E:\\FactorInfo_20190331.xlsx";
       // LoadXlsx(excelFile);
    }
}

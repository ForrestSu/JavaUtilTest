package org;


import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
                private boolean isEof = false;
                private Map<String, ImportFactor> data = new HashMap<>();
                private List<String> errInfos = new ArrayList<>();

                @Override
                public void invoke(List<String> list, AnalysisContext context) {
                    System.out.println("当前行：" + context.getCurrentRowNum());

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
            Date dt = DateUtils.parseDate("20190532", new String[]{"yyyyMMdd"});
            System.out.println(dt.toLocaleString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void testMd5(){

        //FileUtils.checksumCRC32()
    }
    public static void ReadXlsxMode(String excelFile){
        try (FileInputStream in = new FileInputStream(excelFile))
        {
            List<Object> data = EasyExcelFactory.read(in, new Sheet(1, 1, ImportFactor.class));


            System.out.println(data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void LoadXlsxAdv(String excelFile) {

        try (FileInputStream input = new FileInputStream(excelFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(input))
        {

            FactorListener listener = new FactorListener();
            ExcelReader excelReader = new ExcelReader(bufferedInputStream, null, listener);

            System.out.println("start read...");
            excelReader.read();
            System.out.println("excelReader.read()!!");
            List<ImportFactor> records = listener.GetData();
            System.out.println(records);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       // System.out.println("Hello World!");
       // testCommonsIo();
       // testUUId();

        String excelFile = "E:\\FactorInfo_20190331.xlsx";
       // LoadXlsx(excelFile);
       // ReadXlsxMode(excelFile);
        LoadXlsxAdv(excelFile);
    }
}

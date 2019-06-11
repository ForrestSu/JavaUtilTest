package org;


import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    public static void LoadXlsx(String excelFile) {

        try (FileInputStream input = new FileInputStream(excelFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(input)) {
            AnalysisEventListener<List<String>> objectAnalysisEventListener = new AnalysisEventListener<List<String>>() {
                private boolean isEof = false;
                private Map<String, ImportFactor> data = new HashMap<>();
                private List<String> errInfos = new ArrayList<>();

                @Override
                public void invoke(List<String> list, AnalysisContext context) {
                    System.out.println("当前行：" + context.getCurrentRowNum());

                    System.out.println("size = " + list.size() + " list == " + list);
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

    public static void testCommonsIo() {

        String file_name = "test.txt";
        file_name.trim();

        String prefix = FilenameUtils.getBaseName(file_name);

        System.out.println("prefix == <" + prefix + ">");

        try {
            Date dt = DateUtils.parseDate("20190532", new String[]{"yyyyMMdd"});
            System.out.println(dt.toLocaleString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void testMd5() {

        //FileUtils.checksumCRC32()
    }

    public static void ReadXlsxMode(String excelFile) {
        try (FileInputStream in = new FileInputStream(excelFile)) {
            // List<Object> data = EasyExcelFactory.read(in, new Sheet(1, 1, ImportFactor.class));
            // System.out.println(data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void LoadXlsxAdv(String excelFile) {

        try (FileInputStream input = new FileInputStream(excelFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(input)) {

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

    public static void TestListFile() {
        File localDir = new File("E:\\test");
        Collection<File> lists = FileUtils.listFiles(localDir, new String[]{"m", "p", "docx"}, true);

        if (lists == null) {
            System.out.println("is null");
        } else {
            System.out.println(lists.size());
            lists.stream().forEach(f -> System.out.println("==> " + f.getName()));
        }
    }

    public static void testRemoveHashMap() {

        final Map<String, String> mymap = new HashMap<String, String>();
        mymap.put("sunquan", "1234");
        mymap.put("lisi", "1234");
        mymap.put("aaa", "1234");
        mymap.put("bbb", "1234");
        mymap.put("ccc", "1234");
        System.out.println(mymap);

        Set<String> key_set = mymap.keySet();
        key_set.clear();

        System.out.println("key_set " + key_set);
        System.out.println("mymap == " + mymap);
    }

    public static void testObject(int initial) {

        Boolean validity = null;
        if (validity == null) {
            validity = true;
        }
        boolean val = validity;
        System.out.println("val  == " + val);

        System.out.println("===");
    }
    public static void  testMkdirs(){

        String tmpPath = "E:\\factor1\\factor1\\sunquan";
        if (!tmpPath.endsWith(File.separator)) {
            tmpPath = tmpPath + File.separator;
        }
        File tmpdir = new File(tmpPath);
        if (!tmpdir.exists()) {
           boolean  result =  tmpdir.mkdirs();

           System.out.println("result == " + result);
        }

    }

    public static void testUUId() {
        //转化为String对象
        String uuid = UUID.randomUUID().toString();
        // 打印UUID
        System.out.println(uuid);
        uuid = uuid.replace("-", "");
        //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        System.out.println(uuid);
    }

    public static void testUnZip(){

        String file = "E:\\factor1\\测试.zip";
        String tmpWorkDir  = "E:\\factor1\\";
        File zipfile = new File(file);
        try {
            ZipUtil.unpack(zipfile, new File(tmpWorkDir), Charset.forName("GBK"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("unzip oK!");
    }

    private static Pattern FILE_NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z][0-9a-zA-Z_]*$");
    public static boolean isValidFactorId(String str) {
        Matcher m = FILE_NAME_PATTERN.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static void testRegex(){
        String filename = "AA";
        boolean match = isValidFactorId(filename);
        System.out.println("match == " + match);
    }

    public static void MapShowing(Map<Integer, String> mymap) {
        mymap.put(300, "wangwu");
        mymap.put(100, "sunquan");
        mymap.put(200, "lisi");
        mymap.put(200, "lisi");
        mymap.put(90001235, "wangwu");
        mymap.put(90001234, "wangwu");
        System.out.println("打印Map size() " + mymap.size());
        for(Map.Entry<Integer, String> one: mymap.entrySet()){
            System.out.println("==>" + one.getKey() +":" +  one.getValue());
        }
    }

    public static void CallShowMap() {
        System.out.println("打印 HashMap");
        Map<Integer, String> hashmap = new HashMap<>();
        MapShowing(hashmap);

        System.out.println("打印 TreeMap");
        Map<Integer, String> treemap = new TreeMap<>();
        MapShowing(treemap);
    }

    public static void main(String[] args) {
        // System.out.println("Hello World!");
        // testCommonsIo();
        // testUUId();

        // String excelFile = "E:\\FactorInfo_20190331.xlsx";
        // LoadXlsx(excelFile);
        // ReadXlsxMode(excelFile);
        // LoadXlsxAdv(excelFile);
       //testRemoveHashMap();

        //ImportFactor obj = new ImportFactor(10);
        //testMkdirs();

        // testUnZip();
        // testRegex();
        CallShowMap();
    }
}

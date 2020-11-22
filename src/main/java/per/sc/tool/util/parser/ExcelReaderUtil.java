package per.sc.tool.util.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.monitorjbl.xlsx.StreamingReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * POI解析Excel
 */
public class ExcelReaderUtil {

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path
     * @param path
     * @throws IOException
     */
    public static List<List<String>> readExcel(String path) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        // return a list contains many list
        List<List<String>> lists = new ArrayList<List<String>>();
        //读取excel文件
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            //获取工作薄
            Workbook wb = null;
            if (fileType.equals("xls")) {
            	wb = StreamingReader.builder()
                 .rowCacheSize(1000)  //缓存到内存中的行数，默认是10
                 .bufferSize(10240)  //读取资源时，缓存到内存的字节大小，默认是1024
                 .open(is);
//                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
            	wb = StreamingReader.builder()
                        .rowCacheSize(5000)  //缓存到内存中的行数，默认是10
                        .bufferSize(102400)  //读取资源时，缓存到内存的字节大小，默认是1024
                        .open(is);
            } else {
                return null;
            }

            //读取第一个工作页sheet
            Sheet sheet = wb.getSheetAt(0);
            //第一行为标题
            for (Row row : sheet) {
                ArrayList<String> list = new ArrayList<String>();
                for (Cell cell : row) {
                    //根据不同类型转化成字符串
                    list.add(cell.getStringCellValue());
                }
                lists.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }



    public static void main(String[] args) {
        String path = "F:\\GithubRepository\\materialG-EDSB\\Grain-File\\ATEX_OUT_InfosGrainsPixels.xlsx";
        List<List<String>> lists = readExcel(path);
        for (List<String> list : lists) {
            for (String strs : list) {
                System.out.println(strs);
            }
        }
    }


}
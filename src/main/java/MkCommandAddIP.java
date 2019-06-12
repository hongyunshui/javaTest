/**
 *从excel读取攻击IP相关信息，然后生成在交换机后台添加主机组的命令
 * Created by hys on 2019/6/11.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import iotest.BufferWriteTest;

public class MkCommandAddIP {
    public static void main(String[] args) {
        String readFilePath = "C:\\Users\\hys\\Desktop\\IP封堵\\封堵6.12\\封禁表截至目前所有合并0612.xls";
        String filePath1 = "C:\\Users\\hys\\Desktop\\IP封堵\\command.txt";
        String columns[] = {"IP地址","告警次数","告警时间","来源","封禁状态","报告时间","序号"};
        int alert_num = 9; // 告警阈值默认值
        writeExcelToTxt(readFilePath,columns, 50,filePath1,alert_num); // 生成执行命令到脚本
    }
    /**
     * 读取 excel 内容到列表
     * @param readFilePath 要渠道去的文件位置
     * @param columns excel的列名称
     * @return 返回列表
     */
    private static List<Map<String, String>> readExcelToList(String readFilePath, String columns[]){
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list1 = null;
        String cellData1 = null;
        wb = readExcel(readFilePath); // 获取excel中所有内容
        if(wb != null){
            //用来存放表中数据
            list1 = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            // 把从excle中读取的第一个元素压入列表
            for (int i = 1; i<rownum; i++) {
                Map<String, String> map1 = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if(row !=null){
                    cellData1 = (String) getCellFormatValue(row.getCell(0));
                    map1.put(columns[0],cellData1);
                }else{
                    break;
                }
                list1.add(map1);
            }
        }
        return list1;
    }

    /**
     * 筛选出告警次数大于xx 的IP
     * @param cnt 要限制的次数
     * @param readFilePath excel文件路径     * @param columns
     * @return 返回一个列表
     */
    private static List<Map<String, String>> readExcelToList1(String readFilePath, String columns[],int cnt){
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list1 = null;
        String cellData1 = null;
        wb = readExcel(readFilePath); // 获取excel中所有内容
        if(wb != null){
            //用来存放表中数据
            list1 = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            // 把从excle中读取的第一个元素压入列表
            for (int i = 1; i<rownum; i++) {
                Map<String, String> map1 = new LinkedHashMap<String, String>();
                row = sheet.getRow(i); //获取当前行的值
                if(row !=null){
                    cellData1 = (String) getCellFormatValue(row.getCell(0)); // 获取当前行IP
                    // 判断告警次数是否大于cnt
                    if ( Integer.parseInt(String.valueOf(row.getCell(1))) > cnt){
                        System.out.println( Integer.parseInt(String.valueOf(row.getCell(1))) + ": " + cellData1);
                        map1.put(columns[0],cellData1);
                        list1.add(map1);
                    }
                }else{
                    break;
                }
            }
        }
        return list1;
    }

    /**
     * 把列表的值写入到txt文件
     * @param columns excel列名称
     * @param readFilePath excel文件的路径
     * @param denyjy_num 起始的主机组编号
     * @param writeFilePath 要写入的文件路径及名称
     */
    static void writeExcelToTxt(String readFilePath, String columns[], int denyjy_num, String writeFilePath, int alert_num){
        List<Map<String,String>> list ;
        String command = null;
        int cnt =0;
        if (alert_num == 0 ){
            System.out.println("******************生成添加所有IP的命令**********************");
            list = readExcelToList(readFilePath,columns); // 把excel 中的IP内容读取到List
        }
        else {
            System.out.println("******************生成告警次数大于"+ alert_num +"次的命令**********************");
            list = readExcelToList1(readFilePath,columns,alert_num); // 把excel 中的IP内容读取到List
        }
        BufferWriteTest bwt = new BufferWriteTest();
        bwt.test(writeFilePath,"**********添加IP的命令如下************");
        for (Map<String,String> map : list){
            // 如果IP 数量达到所需要的值，则生成新的命令并换行
            if (cnt%50 == 0 ) {
                if (cnt == 0) {
                    command = "define host add name denyjy" + denyjy_num + " ipaddr ' ";
                    denyjy_num++;
                } else {
                    command = command + "'\n";
                    bwt.test(writeFilePath, command);
                    command = "define host add name denyjy" + denyjy_num + " ipaddr ' ";
                    denyjy_num++;
                }
            }
            //输出IP
            for (Entry<String,String> entry : map.entrySet()) {
                command = command + entry.getValue()+" ";
                cnt ++;
            }
        }
        bwt.test(writeFilePath,command + "'");  //把最后生成的命令添加到文件
        bwt.test(writeFilePath,"*******添加IP命令生成结束********\n"
                + "********告警次数 >" + alert_num + "次的IP*********\n"
                + "******共添加了" + cnt + "个IP地址*********\n"
                + "*******" + (new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss")).format(new Date()) + "*******\n");
        System.out.println("******共添加了" + cnt + "个IP地址*********\n");
    }
    /**
     * 使用workbook 读取 Excel 内容
     * @param filePath 读取路径
     * @return 返回值
     */
    private static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    private static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}

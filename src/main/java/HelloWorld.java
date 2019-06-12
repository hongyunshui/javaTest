import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by hys on 2019/6/8.
 */
public class HelloWorld {
    public static void main(String[] args){
        System.out.println("Hello World YYY YY");

        String readFilePath = "C:\\Users\\hys\\Desktop\\IP封堵\\封堵6.12\\封禁表截至目前所有合并0612.xls";
        String writeFilePath1 = "C:\\Users\\hys\\Desktop\\IP封堵\\command.txt";
        String columns[] = {"IP地址","告警次数","告警时间","来源","封禁状态","报告时间","序号"};
        int alert_num = 9;
        int denyjy_num = 90; // denyjy起始值
        MkCommandAddIP.writeExcelToTxt(readFilePath,columns,denyjy_num,writeFilePath1,alert_num);
    }
}

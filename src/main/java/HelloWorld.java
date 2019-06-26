/**
 *
 * Created by hys on 2019/6/8.
 */
public class HelloWorld {
    public static void main(String[] args){
        System.out.println("Hello World YYY YY");
        String readFilePath = "C:\\Users\\hys\\Desktop\\IP封堵\\封堵6.14\\封禁表截至目前所有合并0614.xls"; //要读取的xls文件路径
        String writeFilePath1 = "C:\\Users\\hys\\Desktop\\IP封堵\\封堵6.14\\所有IPcommand0614.txt";  // 生成的命令所在的文件
        String columns[] = {"IP地址","告警次数","告警时间","来源","封禁状态","报告时间","序号"};
        int alert_num = 9;  //此值是多少就代表要抽取“告警次数大于多少的IP”来生成命令
        int denyjy_num = 30; // 此值是主机组名称的起始编号“denyjyXX”
        MkCommandAddIP.mkComdToTxt(readFilePath,columns,denyjy_num,writeFilePath1,alert_num); //调用此函数即可生成命令

    }
}

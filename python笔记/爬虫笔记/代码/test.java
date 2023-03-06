/*
 * @Descripttion: 
 * @Author: 只会Ctrl CV的菜鸟
 * @version: 
 * @Date: 2023-02-14 16:02:40
 * @LastEditTime: 2023-02-14 18:11:29
 */

 import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class test{
    public static void main(String[] args) {
        String pattern ="\\?(.*)&wlan";
        String text="http://10.0.1.5/a79.htm?wlanuserip=10.38.144.2&wlanacname=HJ-BRAS-ME60-01&wlanacip=10.32.255.10&wlanusermac=c6-53-a2-fa-a6-b8&areaID=ethtrunk/102:2725.0";


        // 创建 Pattern 对象
        Pattern p = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = p.matcher(text);
        if(m.find()){

            System.out.println(m.group(1).replace("-", ""));
        }
    }
}


//"http://10.0.1.5:801/eportal/portal/login?callback=dr1003&login_method=1&user_account=%2C1%2C2000300315&user_password=16881026fF.&wlan_user_ip=10.38.144.2&wlan_user_ipv6=&wlan_user_mac=c653a2faa6b8&wlan_ac_ip=10.32.255.10&wlan_ac_name=HJ-BRAS-ME60-01&jsVersion=4.1&terminal_type=2&lang=zh-cn&lang=zh"
//http://10.0.1.5:801/eportal/portal/login?callback=dr1003&login_method=1&user_account=%2C1%2C2000300315&user_password=16881026fF.&jsVersion=4.1&terminal_type=2&lang=zh-cn&lang=zh&wlanuserip=10.38.144.2&wlanacname=HJ-BRAS-ME60-01&wlanacip=10.32.255.10&wlanusermac=c653a2faa6b8

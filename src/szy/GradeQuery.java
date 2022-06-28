package szy;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import szy.util.HtmlModelUtil;
import szy.util.MailUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Siaze
 * @date 2022/6/27
 */
public class GradeQuery {

    private static final String indexUrl = "http://58.87.100.100:81/znlykjdxswxy_jsxsd/";

    private static final String loginUrl = "http://58.87.100.100:81/znlykjdxswxy_jsxsd/xk/LoginToXk";

    private static final String queryUrl = "http://58.87.100.100:81/znlykjdxswxy_jsxsd/kscj/cjcx_list";

    private static final String username = "学号";

    private static final String password = "密码";

    private static final String mail = "邮箱地址";

    private static final OkHttpClient client = new OkHttpClient();

    private static int size;

    private static int lastSize;


    public static void main(String[] args) throws Exception {

        TimerTask task = new TimerTask() {



            private String cookie;

            private boolean flag = true;


            @Override
            public void run() {
                try {
                    Element table = null;
                   if (flag){
                       cookie = getCookie();
                   }

                   Document document = getGrade(cookie);

                    table = document.getElementById("dataList");
                    String result = format(table);

                    if (size != lastSize){
                        lastSize = size;
                        System.out.println("成绩已更新");
                        // 发送邮箱通知
                        MailUtil.sentSimpleMail(result, mail);
                    }else {
                        System.out.println("成绩未更新！");
                    }

                    flag = false;

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if (flag){
                        System.out.println("任务取消！");
                        cancel();
                    }else {
                        System.err.println("出现错误，重试！");
                        flag = true;
                    }

                }

            }
        };

        // 1000为 1秒钟后执行 每10分钟查询一次
        new Timer().schedule(task, 1000, 10 * 60 * 1000);


    }


    /**
     * 登录
     * @return 登录成功的cookie
     * @throws IOException
     */
    private static String getCookie() throws IOException {

        Request index = new Request.Builder().url(indexUrl).build();
        String cookie = client.newCall(index).execute().header("Set-Cookie");

        RequestBody body = new FormBody.Builder().add("encoded", Base64.encode(username.getBytes(StandardCharsets.UTF_8)) + "%%%" + Base64.encode(password.getBytes(StandardCharsets.UTF_8))).build();

        Request request = new Request.Builder().url(loginUrl).addHeader("cookie", cookie).post(body).build();
        client.newCall(request).execute();
        return cookie;
    }

    private static Document getGrade(String cookie) throws IOException {
        // 查询 2021-2022-2 学期的所有成绩
        RequestBody queryBody = new FormBody.Builder().add("kksj", "2021-2022-2")
                .add("xsfs", "all").build();
        Request queryRequest = new Request.Builder().addHeader("Cookie", cookie).post(queryBody).url(queryUrl).build();
        String data = Objects.requireNonNull(client.newCall(queryRequest).execute().body()).string();
        return Jsoup.parse(data);
    }

    /**
     * 输出为网页
     * @param table 成绩表格
     * @return 转化后的网页
     * @throws Exception
     */
    private static String format(Element table) throws Exception {

        Elements trs;
        try {
            trs = table.getElementsByTag("tr");
        } catch (Exception e) {
            throw new Exception("请检查账号密码是否正确。");
        }

        Element head = trs.first();

        List<String> tabs = new ArrayList();
        head.children().forEach(o -> tabs.add(o.text()));

        trs.remove(0);
        size = trs.size();
        return HtmlModelUtil.formatToHtml(tabs, trs);
    }

}

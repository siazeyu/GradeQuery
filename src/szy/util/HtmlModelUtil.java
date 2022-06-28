package szy.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * @author Siaze
 * @date 2022/6/28
 */
public class HtmlModelUtil {
    private static final String MODEL =  "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        .content {\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "            background-color: #e8ebf4;\n" +
            "            overflow: hidden;\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "        .table {\n" +
            "            width: 100%;\n" +
            "            text-align: center;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 40;\n" +
            "            background-color: #d0d7e8;\n" +
            "            border-radius: 10px;\n" +
            "            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);\n" +
            "        }\n" +
            "\n" +
            "        th {\n" +
            "            color: #889096;\n" +
            "        }\n" +
            "\n" +
            "        td {\n" +
            "            color: #020304;\n" +
            "            padding: 3px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"content\">\n" +
            "        <table id=\"tablist\" class=\"table\">\n" +
            "            <tr id=\"header\">\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "    </div>\n" +
            "</body>\n" +
            "\n" +
            "</html>";


    public static String formatToHtml(List<String> headers, Elements data){
        Document result = Jsoup.parse(MODEL);

        Element header = result.getElementById("header");

        header.appendChild(generaElement("th", headers.get(3)))
                .appendChild(generaElement("th", headers.get(4)))
                .appendChild(generaElement("th", headers.get(6)))
                .appendChild(generaElement("th", headers.get(9)));

        Element tabList = result.getElementById("tablist");

        data.forEach( e -> {
            Element tr = new Element("tr");
            tr.appendChild(generaElement("td", e.child(3).text()))
                    .appendChild(generaElement("td", e.child(4).text()))
                    .appendChild(generaElement("td", e.child(6).text()))
                    .appendChild(generaElement("td", e.child(9).text()));
            tabList.appendChild(tr);
        });

        return result.outerHtml();
    }

    private static Element generaElement(String tag, String text){
        return new Element(tag).appendText(text);
    }
}

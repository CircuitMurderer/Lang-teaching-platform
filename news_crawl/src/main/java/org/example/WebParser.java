package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class WebParser {
    private Document doc;
    private List<String> hrefs;
    private List<String> titles;
    private List<String> news;

    public WebParser(String html) {
        this.doc = Jsoup.parse(html);
        this.news = new ArrayList<>();
        this.hrefs = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    public void flush() {
        this.doc = null;
        this.news.clear();
        this.hrefs.clear();
        this.titles.clear();
    }

    public void renew(String html) {
        this.flush();
        this.doc = Jsoup.parse(html);
    }

    public List<String> getNOPSSnews() {
        Elements ele = doc.getElementsByClass("QHbox");
        for (Element e: ele) {
            Elements uls = e.getElementsByClass("clearfix");
            for (Element ul: uls) {
                if (ul.hasClass("page_n clearfix"))
                    continue;
                Elements links = ul.getElementsByTag("a");
                for (Element link: links) {
                    hrefs.add(link.attr("href"));
                    titles.add(link.text());
                }
            }
        }
        return titles;
    }
}

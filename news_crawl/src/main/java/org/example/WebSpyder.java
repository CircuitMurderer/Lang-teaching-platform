package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WebSpyder {
    private final List<String> hrefs;
    private final List<String> news;
    private final List<String> times;
    private final List<String> titles;

    static final String NOPSS = "http://www.nopss.gov.cn/GB/430752/430755";
    static final String BASENOPSS = "http://www.nopss.gov.cn";

    public WebSpyder() {
        this.news = new ArrayList<>();
        this.times = new ArrayList<>();
        this.hrefs = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    public void flush() {
        this.news.clear();
        this.times.clear();
        this.hrefs.clear();
        this.titles.clear();
    }

    private void getNOPSSTitlesAndHrefs() {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(NOPSS).openStream(), "gbk", NOPSS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            Elements ele = doc.getElementsByClass("QHbox");
            for (Element e : ele) {
                Elements uls = e.getElementsByClass("clearfix");
                for (Element ul : uls) {
                    if (ul.hasClass("page_n clearfix"))
                        continue;
                    Elements links = ul.getElementsByTag("a");
                    for (Element link : links) {
                        this.hrefs.add(link.attr("href"));
                        this.titles.add(link.text());
                    }
                    Elements tm = ul.getElementsByTag("em");
                    for (Element t : tm) {
                        this.times.add(t.text());
                    }
                }
            }
        }
    }

    public List<String> getNowTitles() {
        return this.titles;
    }

    public List<String> getNowHrefs() {
        return this.hrefs;
    }

    public List<String> getNowTimes() {
        return this.times;
    }

    public List<String> getNOPSSNews() {
        this.flush();
        System.out.println("Getting news...");
        this.getNOPSSTitlesAndHrefs();
        for (String url: this.hrefs) {
            String newUrl = BASENOPSS + url;
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(newUrl).openStream(), "gbk", newUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                Elements ele = doc.getElementsByClass("text_con clearfix ");
                for (Element e: ele) {
                    Elements paras = e.getElementsByTag("p");
                    StringBuilder oneNews = new StringBuilder();
                    for (Element para: paras) {
                        StringBuilder s = new StringBuilder();
                        if (!para.children().isEmpty()) {
                            for (Element txt: para.getElementsByTag("strong")) {
                                s.append("<strong>").append(txt.text()).append("</strong>");
                            }
                        }
                        s.append(para.ownText());
                        oneNews.append(s).append("\n");
                    }
                    this.news.add(oneNews.toString());
                }
            }
        }
        System.out.println("Done.");
        return this.news;
    }
}

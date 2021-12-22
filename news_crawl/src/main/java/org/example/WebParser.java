package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WebParser {
    private Document doc;
    private List<String> hrefs;
    private List<String> titles;
    private List<String> news;

    private final String NOPSS = "http://www.nopss.gov.cn/GB/430752/430755";
    private final String baseNOPSS = "http://www.nopss.gov.cn";

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

    public void renew(String url) {
        this.flush();
        this.doc = null;
        try {
            this.doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getNOPSSTitlesAndHrefs() {
        Elements ele = doc.getElementsByClass("QHbox");
        for (Element e: ele) {
            Elements uls = e.getElementsByClass("clearfix");
            for (Element ul: uls) {
                if (ul.hasClass("page_n clearfix"))
                    continue;
                Elements links = ul.getElementsByTag("a");
                for (Element link: links) {
                    this.hrefs.add(link.attr("href"));
                    this.titles.add(link.text());
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

    public List<String> getNOPSSNews() {
        this.renew(NOPSS);
        this.getNOPSSTitlesAndHrefs();
        for (String url: this.hrefs) {
            String newUrl = baseNOPSS + url;
            Document doc = null;
            try {
                doc = Jsoup.connect(newUrl).get();
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
        return this.news;
    }
}

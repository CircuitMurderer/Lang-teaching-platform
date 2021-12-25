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
    static final String NOPSS = "http://www.nopss.gov.cn/GB/430752/430755";
    static final String BaseNOPSS = "http://www.nopss.gov.cn";

    static final String $163 = "https://news.163.com/";

    static final int MAXLENGTH = 20;

    private final List<String> hrefs;
    private final List<String> news;
    private final List<String> times;
    private final List<String> titles;

    public WebSpyder() {
        this.news = new ArrayList<>();
        this.times = new ArrayList<>();
        this.hrefs = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    private void flush() {
        this.news.clear();
        this.times.clear();
        this.hrefs.clear();
        this.titles.clear();
    }

    public List<String> getNowTitles() {
        return this.titles.size() > MAXLENGTH ?
                this.titles.subList(0, MAXLENGTH) : this.titles;
    }

    public List<String> getNowHrefs() {
        return this.hrefs.size() > MAXLENGTH ?
                this.hrefs.subList(0, MAXLENGTH) : this.hrefs;
    }

    public List<String> getNowTimes() {
        return this.times.size() > MAXLENGTH ?
                this.times.subList(0, MAXLENGTH) : this.times;
    }

    private void getNOPSSHeadsAndHrefs() {
        this.flush();
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

    public List<String> getNOPSSNews() {
        System.out.println("Getting news...");
        this.getNOPSSHeadsAndHrefs();

        int bound = Math.min(this.hrefs.size(), MAXLENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            String newUrl = BaseNOPSS + url;
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(newUrl).openStream(), "gbk", newUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                Elements ele = doc.getElementsByClass("text_con clearfix ");
                for (Element e : ele) {
                    Elements paras = e.getElementsByTag("p");
                    StringBuilder s = new StringBuilder();
                    for (Element para : paras) {
                        s.append(para.text()).append("\n");
                    }
                    this.news.add(s.toString());
                }
            }
        }
        System.out.println("Done.");
        return this.news;
    }

    private void get163HeadsAndHrefs(String newsKind) {
        this.flush();
        if (!newsKind.equals("domestic") && !newsKind.equals("world")) {
            System.out.println("Bad kind of 163 news.");
            return;
        }

        String url = $163 + newsKind;
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            for (Element ele : doc.getElementsByClass("hidden")) {
                for (Element a : ele.getElementsByTag("a")) {
                    this.hrefs.add(a.attr("href"));
                    this.titles.add(a.text());
                }
            }
        }
    }

    public List<String> get163News(String newsKind) {
        if (!newsKind.equals("domestic") && !newsKind.equals("world")) {
            System.out.println("Bad kind of 163 news.");
            return new ArrayList<>();
        }

        System.out.println("Getting news...");
        this.get163HeadsAndHrefs(newsKind);

        int bound = Math.min(this.hrefs.size(), MAXLENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                for (Element ele : doc.getElementsByClass("post_info")) {
                    String time = ele.ownText();
                    time = time.substring(0, 19);
                    this.times.add(time);
                    break;
                }
                for (Element ele : doc.getElementsByClass("post_body")) {
                    StringBuilder s = new StringBuilder();
                    for (Element p : ele.getElementsByTag("p")) {
                        if (p.hasAttr("class") || p.hasAttr("align")) {
                            continue;
                        }
                        if (p.text().equals("")) {
                            continue;
                        }
                        s.append(p.text()).append("\n");
                    }
                    this.news.add(s.toString());
                }
            }
        }
        return this.news;
    }
}

package org.example;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
    static final String NOPSS_BASE = "http://www.nopss.gov.cn";

    static final String $_163_NEWS = "https://news.163.com/";

    static final String GU_SHI_WEN = "https://www.gushiwen.cn/";

    static final String ZAO_BAO = "https://www.zaobao.com/forum/views";
    static final String ZAO_BAO_BASE = "https://www.zaobao.com";

    static final String CN_DAILY = "https://language.chinadaily.com.cn/news_bilingual";

    static final int MAX_LENGTH = 20;

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
        return this.titles.size() > MAX_LENGTH ?
                this.titles.subList(0, MAX_LENGTH) : this.titles;
    }

    public List<String> getNowHrefs() {
        return this.hrefs.size() > MAX_LENGTH ?
                this.hrefs.subList(0, MAX_LENGTH) : this.hrefs;
    }

    public List<String> getNowTimes() {
        return this.times.size() > MAX_LENGTH ?
                this.times.subList(0, MAX_LENGTH) : this.times;
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
                        this.hrefs.add(NOPSS_BASE + link.attr("href"));
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
        System.out.println("Getting NOPSS texts...");
        this.getNOPSSHeadsAndHrefs();

        int bound = Math.min(this.hrefs.size(), MAX_LENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(url).openStream(), "gbk", url);
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

        String url = $_163_NEWS + newsKind;
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

        System.out.println("Getting 163 news...");
        this.get163HeadsAndHrefs(newsKind);

        int bound = Math.min(this.hrefs.size(), MAX_LENGTH);
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

    public void getGSWHeadsAndHrefs() {
        this.flush();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(GU_SHI_WEN).openStream(), "utf-8", GU_SHI_WEN);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            for (Element ele : doc.getElementsByClass("cont")) {
                Elements paras = ele.getElementsByTag("p");
                if (paras.isEmpty()) {
                    continue;
                }
                Element p = paras.get(0);
                for (Element a : p.getElementsByTag("a")) {
                    this.hrefs.add(a.attr("href"));
                    this.titles.add(a.text());
                    this.times.add("Any");
                }
            }
        }
    }

    public List<String> getGSWNews() {
        System.out.println("Getting gushiwen texts...");
        this.getGSWHeadsAndHrefs();
        WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED);
        client.getOptions().setThrowExceptionOnScriptError(false);

        int bound = Math.min(this.hrefs.size(), MAX_LENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            try {
                HtmlPage page = client.getPage(url);
                var flatten = page
                        .getByXPath("//div[@style='text-align:center; margin-top:-5px;']");

                if (flatten.size() > 0) {
                    HtmlDivision div = (HtmlDivision)flatten.get(0);
                    for (var child : div.getByXPath("//a[text()='展开阅读全文 ∨']")) {
                        page = ((HtmlAnchor) child).click();
                    }
                }

                Document doc = Jsoup.parse(page.asXml(), url);
                Elements divs = doc.getElementsByClass("cont");
                Element d = divs.get(1);

                StringBuilder s = new StringBuilder();
                for (Element e : d.children()) {
                    if (e.tagName().equals("p")) {
                        s.append(e.text()).append("\n");
                    } else if (e.tagName().equals("div") &&
                            e.className().equals("contson")) {
                        String newStr = e.text()
                                .replace(") ", ")\n")
                                .replace("？ ", "？\n")
                                .replace("！ ", "！\n")
                                .replace("。 ", "。\n");
                        s.append(newStr).append("\n");
                    }
                }

                boolean needFlatten = false;
                for (Element e : doc.getElementsByClass("sons")) {
                    if (e.hasAttr("id") && e.id().contains("fanyiquan")) {
                        needFlatten = true;
                        for (Element ed : e.getElementsByClass("contyishang")) {
                            String newStr = ed.text()
                                    .replace(" 译文 ", "\n译文\n")
                                    .replace(" 注释 ", "\n注释\n")
                                    .replace("。 ", "。\n")
                                    .replace("▲", "");
                            s.append("\n").append(newStr).append("\n");
                            break;
                        }
                    }
                }

                if (!needFlatten) {
                    for (Element ed : doc.getElementsByClass("contyishang")) {
                        String newStr = ed.text()
                                .replace(" 译文 ", "\n译文\n")
                                .replace(" 注释 ", "\n注释\n")
                                .replace("。 ", "。\n")
                                .replace("▲", "");
                        s.append("\n").append(newStr).append("\n");
                        break;
                    }
                }
                this.news.add(s.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        client.close();
        return this.news;
    }

    private void getZaoBaoHeadsAndHrefs() {
        this.flush();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(ZAO_BAO).openStream(), "utf-8", ZAO_BAO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            Element div = doc.getElementById("more-container");
            assert div != null;
            for (Element a : div.getElementsByTag("a")) {
                this.hrefs.add(ZAO_BAO_BASE + a.attr("href"));
                for (Element ti : a.getElementsByClass("f18 m-eps")) {
                    this.titles.add(ti.text());
                    break;
                }
            }
        }
    }

    public List<String> getZaoBaoNews() {
        System.out.println("Getting zaobao news...");
        this.getZaoBaoHeadsAndHrefs();

        int bound = Math.min(this.hrefs.size(), MAX_LENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            Document doc = null;
            try {
                var stream = new URL(url).openStream();
                doc = Jsoup.parse(stream, "utf-8", url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                for (Element div : doc.getElementsByClass("text-track-v1 author-info f14")) {
                    for (Element tm : div.getElementsByClass("mgt10")) {
                        this.times.add(tm.text());
                        break;
                    }
                    break;
                }

                Element contain = doc.getElementById("article-body");
                assert contain != null;

                StringBuilder s = new StringBuilder();
                for (Element p : contain.getElementsByTag("p")) {
                    s.append(p.text()).append("\n");
                }
                this.news.add(s.toString());
            }
        }
        return this.news;
    }

    private void getCNDailyHeadsAndHrefs() {
        this.flush();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(CN_DAILY).openStream(), "utf-8", CN_DAILY);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            for (Element p : doc.getElementsByClass("gy_box_txt2")) {
                for (Element a : p.getElementsByTag("a")) {
                    this.hrefs.add("https:" + a.attr("href"));
                    //this.titles.add(a.text());
                }
            }
        }
    }

    public List<String> getCNDailyNews() {
        System.out.println("Getting CN-Daily news...");
        this.getCNDailyHeadsAndHrefs();

        int bound = Math.min(this.hrefs.size(), MAX_LENGTH);
        for (int i = 0; i < bound; i++) {
            String url = this.hrefs.get(i);
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                for (Element mTitle : doc.getElementsByClass("main_title")) {
                    Element tit = mTitle.getElementsByTag("h1").get(0);
                    Element tm = mTitle.getElementsByClass("main_title3").get(0);

                    String nowTime = tm.text();
                    nowTime = nowTime.substring(nowTime.length() - 16);

                    this.titles.add(tit.text().replace("'", "‘"));
                    this.times.add(nowTime);
                    break;
                }

                StringBuilder s = new StringBuilder();
                for (Element mText : doc.getElementsByClass("mian_txt")) {
                    for (Element p : mText.getElementsByTag("p")) {
                        String htm = p.html().replace("<br>", "[br]");
                        var par = Jsoup.parse(htm);
                        var txt = par.text().replace("[br]", "\n");
                        s.append(txt.replace("'", "‘")).append("\n");
                    }
                    break;
                }
                this.news.add(s.toString());
            }
        }
        return this.news;
    }
}

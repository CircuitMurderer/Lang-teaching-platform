package org.example;


public class App {
    public static void main(String[] args) {
        WebSpyder spyder = new WebSpyder();
        SqlConnector sc = new SqlConnector();

        var texts = spyder.getNOPSSNews();
        var titles = spyder.getNowTitles();
        var times = spyder.getNowTimes();

        sc.insertManyNews("culture", titles, times, texts, "gbk");

        texts = spyder.get163News("world");
        titles = spyder.getNowTitles();
        times = spyder.getNowTimes();

        sc.insertManyNews("world", titles, times, texts, "utf8");

        texts = spyder.get163News("domestic");
        titles = spyder.getNowTitles();
        times = spyder.getNowTimes();

        sc.insertManyNews("domestic", titles, times, texts, "utf8");

        texts = spyder.getCNDailyNews();
        titles = spyder.getNowTitles();
        times = spyder.getNowTimes();

        sc.insertManyNews("doublelang", titles, times, texts, "utf8");

        texts = spyder.getGSWNews();
        titles = spyder.getNowTitles();
        times = spyder.getNowTimes();

        sc.insertManyNews("gushiwen", titles, times, texts, "utf8");

        texts = spyder.getZaoBaoNews();
        titles = spyder.getNowTitles();
        times = spyder.getNowTimes();

        sc.insertManyNews("expert", titles, times, texts, "utf8");
    }
}

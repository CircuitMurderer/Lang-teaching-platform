package org.example;


import java.util.List;

public class App {
    public static void main(String[] args) {
        WebSpyder spyder = new WebSpyder();
        List<String> news = spyder.getNOPSSNews();
        List<String> titles = spyder.getNowTitles();
        List<String> times = spyder.getNowTimes();

        //System.out.println(times.get(1) + "\n" + titles.get(1) + "\n" + news.get(1));
        SqlConnector sc = new SqlConnector();
        //sc.insertNews("NOPSS", titles.get(0), times.get(0), news.get(0));
        sc.insertManyNews("NOPSS", titles, times, news);
        sc.selectNews("NOPSS");
    }
}

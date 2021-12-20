package org.example;


public class App {
    public static void main(String[] args) {
        String SheKeJJZK = "http://www.nopss.gov.cn/GB/430752/430755";
        WebSpyder spyder = new WebSpyder(SheKeJJZK);
        WebParser parser = new WebParser(spyder.getContent());

        System.out.println(parser.getNOPSSnews());
    }
}

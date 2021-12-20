package org.example;


public class App {
    public static void main(String[] args) {
        WebSpyder spyder = new WebSpyder("http://www.people.com.cn/");
        System.out.println(spyder.getContent());
    }
}

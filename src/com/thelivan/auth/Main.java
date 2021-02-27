package com.thelivan.auth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static HttpServer server;
    public static Date date = new Date();
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        System.out.println("Starting server....");
        server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.createContext("/auth", new Handler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Server started on port 8001");
        while (true) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = "";
            command = in.readLine();
            if (command.startsWith("stop")) {
                System.exit(0);
            }
        }
    }

    public static boolean verifyPassword(String username, String password) {
        if (username.matches("TheLivan") && password.matches("2325gasg")) {
            return true;
        } else if (username.matches("Gamgid") && password.matches("hg24561")) {
            return true;
        } else if (username.matches("Ggangds") && password.matches("jjjoho")) {
            return true;
        } else if (username.matches("_HEADER_") && password.matches("1245hasgksh")) {
            return true;
        } else if (username.matches("afteh") && password.matches("Kikignag18")) {
            return true;
        }
        return false;
    }

    public static class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            System.out.println("[" + sdf.format(Main.date) + "] " + getUsername(httpExchange) + " " + getPassword(httpExchange) + " " + getIp(httpExchange)); // Выводим лог в консоль
            if (httpExchange.getRemoteAddress().getAddress().getHostName().matches("127.0.0.1")) return; // Проверка на левые хосты, с других хостов подключатся нельзя.
            if (verifyPassword(getUsername(httpExchange), getPassword(httpExchange))) {
                byte[] response = ("OK:" + getUsername(httpExchange) + ":0").getBytes();
                httpExchange.sendResponseHeaders(200, response.length);
                outputStream.write(response);
                outputStream.flush();
            } else {
                byte[] response = ("Wrong password please try again").getBytes();
                httpExchange.sendResponseHeaders(200, response.length);
                outputStream.write(response);
                outputStream.flush();
            }
            outputStream.close();
            httpExchange.close();
        }

        private String getUsername(HttpExchange httpExchange) {
            return httpExchange.getRequestURI().toString().split("username=")[1].split("&")[0];
        }

        private String getIp(HttpExchange httpExchange) {
            return httpExchange.getRequestURI().toString().split("ip=")[1];
        }

        private String getPassword(HttpExchange httpExchange) {
            return httpExchange.getRequestURI().toString().split("password=")[1].split("&")[0];
        }
    }
}

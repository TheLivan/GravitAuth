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
    public static final Date DATE = new Date();
    public static final int PORT = 8001;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        doLog("Starting server....");
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.createContext("/auth", new Main.Handler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        doLog("Server started on port " + PORT);
        while (true) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = "";
            command = in.readLine();
            if (command.startsWith("stop")) {
                System.exit(0);
            }
        }
    }

    public static boolean verifyPassword(String username, String password) { // Вот тут писать логины и пароли.
        if (username.matches("login") && password.matches("password")) {
            return true;
        } else if (username.matches("login1") && password.matches("password2")) {
            return true;
        }
        return false;
    }

    public static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            doLog("[" + DATE_FORMAT.format(DATE) + "] " + getUsername(httpExchange) + " " + getPassword(httpExchange) + " " + getIp(httpExchange)); // Выводим лог в консоль
            if (httpExchange.getRemoteAddress().getAddress().getHostName().matches("127.0.0.1")) return; // С других хостов подключаться нельзя, только localhost.
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
    }

    public static void doLog(String log) {
        System.out.println("[Auth] " + log);
    }

    public static String getUsername(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().split("username=")[1].split("&")[0];
    }

    public static String getIp(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().split("ip=")[1];
    }

    public static String getPassword(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().split("password=")[1].split("&")[0];
    }
}

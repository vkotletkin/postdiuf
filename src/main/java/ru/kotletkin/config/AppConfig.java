package ru.kotletkin.config;

public class AppConfig {
    public static String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");
    public static String S3_ACCESS_KEY = "some_access_key1";
    public static String S3_SECRET_KEY = "some_secret_key1";
    public static String S3_ENDPOINT = "http://192.168.126.129:8333";
    public static String S3_BUCKET = "telegram-bucket";
}

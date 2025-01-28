package ru.kotletkin.config;

public class AppConfig {
    public static String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");
    public static String S3_ACCESS_KEY = System.getenv("S3_ACCESS_KEY");
    public static String S3_SECRET_KEY = System.getenv("S3_SECRET_KEY");
    public static String S3_ENDPOINT = System.getenv("S3_ENDPOINT");
    public static String S3_BUCKET = System.getenv("S3_BUCKET");
}

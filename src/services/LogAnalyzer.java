package services;

import java.io.*;
import java.util.*;

public class LogAnalyzer {
    private static final int FAILED_LOGIN_THRESHOLD = 5;


    private static final Map<String, Integer> loginAttempts = new HashMap<>();
    private static final Map<String, Integer> suspiciousIPs = new HashMap<>();
    private static final List<GeoIPService.GeoInfo> geoInfoList = new ArrayList<>();

    public static void analyzeLogs(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                analyzeLogLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Post-processing
        for (Map.Entry<String, Integer> entry : loginAttempts.entrySet()) {
            if (entry.getValue() >= FAILED_LOGIN_THRESHOLD) {
                suspiciousIPs.put(entry.getKey(), entry.getValue());

                try {
                    GeoIPService geo = new GeoIPService();
                    GeoIPService.GeoInfo info = geo.lookup(entry.getKey());
                    if (info != null) {
                        geoInfoList.add(info);
                    }
                } catch (IOException e) {
                    System.err.println("GeoIP error for " + entry.getKey() + ": " + e.getMessage());
                }
            }
        }

        CSVExporter.export(suspiciousIPs, "outputs/alerts.csv");
        JsonExporter.export(geoInfoList, "outputs/attacks.json");
    }

    public static void analyzeLogLine(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 3) return;

        String ip = parts[1];
        String action = parts[2];

        if ("FAILED".equalsIgnoreCase(action)) {
            loginAttempts.put(ip, loginAttempts.getOrDefault(ip, 0) + 1);

            if (loginAttempts.get(ip) == FAILED_LOGIN_THRESHOLD) {
                String alert = "🚨 Brute-force detected from IP: " + ip +
                               "\nFailed login attempts: " + loginAttempts.get(ip);
                System.out.println(alert);
                EmailAlertService.send("mr.omarkh2000@gmail.com", "Brute-force Alert", alert);
            
             // GeoIP lookup here too
             try {
                GeoIPService geo = new GeoIPService();
                GeoIPService.GeoInfo info = geo.lookup(ip);
                if (info != null) {
                    geoInfoList.add(info);
                    suspiciousIPs.put(ip, loginAttempts.get(ip)); // also store count

                    // Export updated files in real-time
                    CSVExporter.export(suspiciousIPs, "outputs/alerts.csv");
                    JsonExporter.export(geoInfoList, "outputs/attacks.json");
                }
            } catch (IOException e) {
                System.err.println("GeoIP error (real-time) for " + ip + ": " + e.getMessage());
            }
        }
    }

        System.out.println("📄 Analyzing: " + line);
    }
}

package services;

import java.io.*;
import java.util.*;

public class LogAnalyzer {
    private static final int FAILED_LOGIN_THRESHOLD = 5;

    public static void analyzeLogs(String filePath) {
        Map<String, Integer> loginAttempts = new HashMap<>();
        Map<String, Integer> suspiciousIPs = new HashMap<>();
        List<GeoIPService.GeoInfo> geoInfoList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 3) continue;

                String ip = parts[1];
                String action = parts[2];

                if ("FAILED".equalsIgnoreCase(action)) {
                    loginAttempts.put(ip, loginAttempts.getOrDefault(ip, 0) + 1);

                    if (loginAttempts.get(ip) == FAILED_LOGIN_THRESHOLD) {
                        String alert = "🚨 Brute-force detected from IP: " + ip +
                                       "\nFailed login attempts: " + loginAttempts.get(ip);
                        System.out.println(alert);
                        EmailAlertService.send("mr.omarkh2000@gmail.com", "Brute-force Alert", alert);
                    }
                }
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
}

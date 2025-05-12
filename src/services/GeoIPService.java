package services;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class GeoIPService {
    private final DatabaseReader dbReader;

    public GeoIPService() throws IOException {
        
        File database = new File("ressources/GeoLite2-City.mmdb");
        this.dbReader = new DatabaseReader.Builder(database).build();
    }

    public static class GeoInfo {
        public String ip;
        public String country;
        public double lat;
        public double lon;

        public GeoInfo(String ip, String country, double lat, double lon) {
            this.ip = ip;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
        }
    }


    public GeoInfo lookup(String ipAddress) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            CityResponse response = dbReader.city(ip);


            String country = response.getCountry().getName();
            String city = response.getCity().getName();
            Double latitude = response.getLocation().getLatitude();
            Double longitude = response.getLocation().getLongitude();

            if (country == null || latitude == null || longitude == null) {
                System.err.println("the database doesn't contain information about this address " + ipAddress);
                return null;
            }
            
            System.out.println("IP: " + ipAddress);
            System.out.println("Pays: " + country);
            System.out.println("Ville: " + city);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            return new GeoInfo(ipAddress, country, latitude, longitude);
        } catch (GeoIp2Exception | IOException e) {
            System.err.println("GeoIp error " + ipAddress + ": " + e.getMessage());
            return null;
        }
    }
}


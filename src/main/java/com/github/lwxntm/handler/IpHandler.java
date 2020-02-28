package com.github.lwxntm.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author lei
 */
public class IpHandler {
    static ObjectMapper mapper = new ObjectMapper();
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse resp = null;

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String getIpInfo(String ip) throws IOException {

        try {
            CloseableHttpResponse resp = httpClient.execute(new HttpGet("https://api.ip.sb/geoip/" + ip));
            if (resp.getStatusLine().getStatusCode() == 200) {
                IpSbObj ipSbObj = mapper.readValue(EntityUtils.toString(resp.getEntity(), "UTF-8"), IpSbObj.class);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(ipSbObj.ip).append("\nAS ").append(ipSbObj.asn).append(" ").append(ipSbObj.organization).append("\n");
                stringBuilder.append(ipSbObj.city).append(" ").append(ipSbObj.country).append(" ").append(ipSbObj.country_code);
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            return e.toString();
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
        return " ";
    }

    public static String handleRawStr(String str) {
        String s = str;
        System.out.println(s);
        if (s.startsWith("http://")) {
            System.out.println(true);
            s = s.substring(7);
        }
        if (s.startsWith("https://")) {  System.out.println(true);
            s = s.substring(8);
        }
        if (s.contains("/")) {  System.out.println(true);
            s = s.substring(0, s.indexOf("/"));
        }
        if (s.contains(":")) {  System.out.println(true);
            s = s.substring(0, s.indexOf(":"));
        }
        try {
            String hostAddress = InetAddress.getByName(s).getHostAddress();
            return getIpInfo(hostAddress);
        } catch (IOException e) {
            return e.toString();
        }
    }

    static class IpSbObj {

        /**
         * GsomFormat
         */

        private String organization;
        private double longitude;
        private String city;
        private String timezone;
        private String isp;
        private int offset;
        private int asn;
        private String asn_organization;
        private String country;
        private String ip;
        private double latitude;
        private String postal_code;
        private String continent_code;
        private String country_code;

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getAsn() {
            return asn;
        }

        public void setAsn(int asn) {
            this.asn = asn;
        }

        public String getAsn_organization() {
            return asn_organization;
        }

        public void setAsn_organization(String asn_organization) {
            this.asn_organization = asn_organization;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(String postal_code) {
            this.postal_code = postal_code;
        }

        public String getContinent_code() {
            return continent_code;
        }

        public void setContinent_code(String continent_code) {
            this.continent_code = continent_code;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }
    }
}

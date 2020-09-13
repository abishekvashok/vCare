package io.github.pranavgade20.pennapps.networkmanager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Request {
    public static int make(int age, int gender, int h1, int h2, int h3, int h4) {
        try {
            URL url = new URL("localhost:5000/predict?" +
                    "age=" + age +
                    "gender=" + gender/2 +
                    "h1=" + h1 +
                    "h2=" + h2 +
                    "h3=" + h3 +
                    "h4=" + h4);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Scanner scanner = new Scanner(connection.getInputStream());

            String response = scanner.nextLine();
            if (response.length() == 3) {
                if (response.charAt(1) == '0') return 0;
                if (response.charAt(1) == '1') return 1;
            }

            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }
}
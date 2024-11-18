package com.example.smk7;

public class Db_Contract {

    public static String ip = "192.168.100.48";
    public static final String BASE_URL = "http://" + ip + "/WebNewbieTeam/"; // Update if needed
    public static final String urlLogin = BASE_URL + "api-login.php";  // This is correct
    public static ApiService getApiService() {
        return ApiClient.getRetrofitInstance().create(ApiService.class);  // Ensure you use Retrofit to create the service
    }
}

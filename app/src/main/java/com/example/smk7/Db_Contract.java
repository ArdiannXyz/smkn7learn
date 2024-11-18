package com.example.smk7;

public class Db_Contract {

    public static String ip = "192.168.25.105";
    public static final String BASE_URL = "http://" + ip + "/WebNewbieTeam/";
    public static final String urlLogin = BASE_URL + "api-login.php";
    public static ApiService getApiService() {
        return ApiClient.getRetrofitInstance().create(ApiService.class);
    }
}

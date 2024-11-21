package com.example.smk7;

public class Db_Contract {

    public static String ip = "192.168.140.109";
    public static final String BASE_URL = "http://" + ip + "/WebNewbieTeam/";
    public static final String urlLogin = BASE_URL + "api-login.php";
    public static final String urlLupaPassword = BASE_URL + "api-lupa-password.php";
    public static final String urlGantiPassword = BASE_URL + "api-ganti-password.php";


    public static ApiService getApiService() {
        return ApiClient.getRetrofitInstance().create(ApiService.class);
    }
}

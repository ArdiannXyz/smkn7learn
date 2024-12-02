package com.example.smk7;

public class Db_Contract {

    public static String ip = "192.168.255.254";
    public static final String BASE_URL = "http://" + ip + "/WebNewbieTeam/";
    public static final String urlLogin = BASE_URL + "api-login.php";
    public static final String urlLupaPassword = BASE_URL + "api-check_email.php";
    public static final String urlGantiPassword = BASE_URL + "api-reset_password.php";
    public static final String urlApiDashboard = BASE_URL + "api_dashboard.php";
    public static final String urlApiUploadMateri = BASE_URL + "api-uploadMateri.php";

    public static ApiService getApiService() {
        return ApiClient.getRetrofitInstance().create(ApiService.class);
    }
}

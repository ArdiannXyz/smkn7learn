package com.example.smk7.Siswa;

import static android.content.Context.MODE_PRIVATE;
import static com.example.smk7.LoginActivity.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smk7.ApiDatabase.ApiResponse;
import com.example.smk7.ApiDatabase.ApiService;
import com.example.smk7.ApiDatabase.ApiServiceInterface;
import com.example.smk7.Guru.Adapter.KelasAdapter;
import com.example.smk7.Guru.Model.KelasModel;
import com.example.smk7.LoginActivity;
import com.example.smk7.R;
import com.example.smk7.Siswa.Adapter.TugasSiswaAdapter;
import com.example.smk7.Siswa.Model.TugasSiswaModel;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardSiswaFragment extends Fragment {

    private TextView textViewNamaSiswa, textViewNISN;
    private RecyclerView recyclerViewTugas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_siswa_fragment, container, false);

        // Inisialisasi elemen UI
        textViewNamaSiswa = view.findViewById(R.id.txt_namasiswa);
        textViewNISN = view.findViewById(R.id.txt_nisn);
        recyclerViewTugas = view.findViewById(R.id.recyclerView);

        LinearLayout linearLayoutMateriSiswa = view.findViewById(R.id.materisiswa);
        LinearLayout linearLayoutTugasSiswa = view.findViewById(R.id.tugassiswa);

        linearLayoutMateriSiswa.setOnClickListener(v -> openFragment(3));
        linearLayoutTugasSiswa.setOnClickListener(v -> openFragment(6));

        // Ambil user ID dari SharedPreferences
        int userId = getUserIdFromPreferences();

        // Panggil API untuk mengambil data siswa
        fetchDataSiswaFromServer(userId);
        fetchTugasData();

        return view;
    }

    private void fetchTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getTugasData2();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Response", "Full Response: " + apiResponse.toString());  // Log the full response

                    if ("success".equals(apiResponse.getStatus())) {
                        List<TugasSiswaModel> tugasList = apiResponse.getTugasSiswaModel();

                        // Log for debugging
                        if (tugasList != null && !tugasList.isEmpty()) {
                            Log.d("Tugas List", "Jumlah Tugas: " + tugasList.size());
                            setupRecyclerView(tugasList);
                        } else {
                            Log.e("API Response", "Tugas data is empty");
                            Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API Response", "API status is not success");
                        Toast.makeText(getContext(), "API error: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response failed: " + response.code() + ", message: " + response.message());
                    Toast.makeText(getContext(), "API error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("API Failure", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);  // Default -1 jika tidak ada user_id
    }

    private void fetchDataSiswaFromServer(int userId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String apiSiswaUrl = "http://192.168.100.48/api-mobile-php/api_siswa.php?user_id=" + userId;
                Log.d("URL_Siswa", apiSiswaUrl);

                HttpRequest request = HttpRequest.get(apiSiswaUrl);
                if (request.code() == 200) {
                    String response = request.body();
                    Log.d("API_Response", response);

                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONObject dataSiswa = jsonObject.getJSONObject("data");
                        String namaSiswa = dataSiswa.getString("nama");
                        String nisnSiswa = dataSiswa.getString("nisn");

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                textViewNamaSiswa.setText("Nama: " + namaSiswa);
                                textViewNISN.setText("NISN: " + nisnSiswa);
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                textViewNamaSiswa.setText("Data tidak ditemukan.");
                                textViewNISN.setText("");
                            });
                        }
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            textViewNamaSiswa.setText("Error mengambil data.");
                            textViewNISN.setText("");
                        });
                    }
                }
            } catch (Exception e) {
                Log.e("Error", "Kesalahan saat mengambil data: " + e.getMessage());
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        textViewNamaSiswa.setText("Kesalahan terjadi.");
                        textViewNISN.setText("");
                    });
                }
            }
        });
    }

    private void openFragment(int position) {
        if (getActivity() != null) {
            ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(position);
        }
    }

    private void setupRecyclerView(List<TugasSiswaModel> tugasList) {
        if (tugasList != null && !tugasList.isEmpty()) {
            // Pass null for viewPager and false for isViewPagerRequired in this fragment
            TugasSiswaAdapter adapter = new TugasSiswaAdapter(tugasList);  // Only pass tugasList
            recyclerViewTugas.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewTugas.setAdapter(adapter);
        }
    }
}

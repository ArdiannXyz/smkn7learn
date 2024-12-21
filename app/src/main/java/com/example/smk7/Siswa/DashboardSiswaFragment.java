package com.example.smk7.Siswa;

import static android.content.Context.MODE_PRIVATE;

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
    private static final String PREF_NAME = "MyAppPrefs";
    private TextView textViewNamaSiswa, textViewNISN;
    private RecyclerView recyclerViewTugas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_siswa_fragment, container, false);

        initializeViews(view);
        setupClickListeners(view);

        int userId = getUserIdFromPreferences();
        fetchDataSiswaFromServer(userId);
        fetchTugasData();

        return view;
    }

    private void initializeViews(View view) {
        textViewNamaSiswa = view.findViewById(R.id.txt_namasiswa);
        textViewNISN = view.findViewById(R.id.txt_nisn);
        recyclerViewTugas = view.findViewById(R.id.recyclerView);
    }

    private void setupClickListeners(View view) {
        LinearLayout linearLayoutMateriSiswa = view.findViewById(R.id.materisiswa);
        LinearLayout linearLayoutTugasSiswa = view.findViewById(R.id.tugassiswa);

        linearLayoutMateriSiswa.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(3,false);
            }
        });

        linearLayoutTugasSiswa.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(8,false);
            }
        });
    }

    private void fetchTugasData() {
        ApiServiceInterface apiService = ApiService.getRetrofitInstance().create(ApiServiceInterface.class);
        Call<ApiResponse> call = apiService.getTugasData2();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("API Status", "Status: " + apiResponse.getStatus());

                    List<TugasSiswaModel> tugasList = apiResponse.getTugasSiswaModel();
                    if (tugasList != null && !tugasList.isEmpty()) {
                        for (TugasSiswaModel tugas : tugasList) {
                            Log.d("Tugas", "Judul: " + tugas.getJudulTugas());
                        }
                        setupRecyclerView(tugasList);
                    } else {
                        Log.e("Data Error", "Tugas list is null or empty");
                    }
                } else {
                    Log.e("API Error", "Response failed with code: " + response.code());
                }
            }


            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("API Failure", "Failed to fetch data: " + t.getMessage());
                showToast("Failed to fetch data: " + t.getMessage());
            }
        });
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt("id", 7);
    }

    private void fetchDataSiswaFromServer(int Id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String apiSiswaUrl = "http://192.168.118.49/WebNewbieTeam/api_siswa.php?id=" + Id;
                Log.d("URL_Siswa", apiSiswaUrl);

                HttpRequest request = HttpRequest.get(apiSiswaUrl);
                if (request.code() == 200) {
                    handleSuccessfulResponse(request.body());
                } else {
                    handleFailedResponse();
                }
            } catch (Exception e) {
                handleError(e);
            }
        });
    }

    private void handleSuccessfulResponse(String response) throws Exception {
        Log.d("API_Response", response);
        JSONObject jsonObject = new JSONObject(response);
        boolean success = jsonObject.getBoolean("success");

        if (success) {
            JSONObject dataSiswa = jsonObject.getJSONObject("data");
            String namaSiswa = dataSiswa.getString("nama");
            String nisnSiswa = dataSiswa.getString("nisn");

            updateUI(namaSiswa, nisnSiswa);
        } else {
            updateUIForNoData();
        }
    }

    private void handleFailedResponse() {
        updateUIForError("Error mengambil data.");
    }

    private void handleError(Exception e) {
        Log.e("Error", "Kesalahan saat mengambil data: " + e.getMessage());
        updateUIForError("Kesalahan terjadi.");
    }

    private void updateUI(String nama, String nisn) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                textViewNamaSiswa.setText("Nama: " + nama);
                textViewNISN.setText("NISN: " + nisn);
            });
        }
    }

    private void updateUIForNoData() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                textViewNamaSiswa.setText("Data tidak ditemukan.");
                textViewNISN.setText("");
            });
        }
    }

    private void updateUIForError(String errorMessage) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                textViewNamaSiswa.setText(errorMessage);
                textViewNISN.setText("");
            });
        }
    }

    private void openFragment(int position) {
        if (getActivity() != null) {
            ((DashboardSiswa) getActivity()).viewPager2.setCurrentItem(position);
        }
    }

    private void setupRecyclerView(List<TugasSiswaModel> tugasList) {
        if (tugasList != null && !tugasList.isEmpty()) {
            TugasSiswaAdapter adapter = new TugasSiswaAdapter(tugasList);
            recyclerViewTugas.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewTugas.setAdapter(adapter);
        }
    }
}
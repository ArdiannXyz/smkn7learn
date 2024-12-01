package com.example.smk7.Guru;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smk7.RecycleBankTugas.BankTugasKelas_Guru;
import com.example.smk7.RecycleBankTugas.BankTugasMapel_Guru;
import com.example.smk7.RecycleBankTugas.RecycleViewBankTugas_Guru;
import com.example.smk7.RecycleTugasGuru.RecycleViewTugasGuru;
import com.example.smk7.RecycleTugasGuru.UploadTugasKelas_Guru;
import com.example.smk7.RecycleTugasGuru.UploadTugasMapelGuru;
import com.example.smk7.Recyclemateriguru.RecyleViewMateri_Guru;
import com.example.smk7.Recyclemateriguru.UploadMateriKelas_Guru;
import com.example.smk7.Recyclemateriguru.UploadMateriMapel_Guru;

public class ViewPagerAdapterGuru extends FragmentStateAdapter {
    public ViewPagerAdapterGuru(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DashboardGuruFragment();
            case 1:
                return new Kelas_guru();
            case 2:
                return new Profil_guru();
            case 3:
                return new UploadMateriMapel_Guru();
            case 4:
                return new UploadTugasMapelGuru();
            case 5:
                return new BankTugasMapel_Guru();
            case 6:
                return new ViewProfil_Guru();
            case 7:
                return new EditProfil_Guru();
            case 8:
                return new UploadMateriKelas_Guru();
            case 9:
                return new UploadTugasKelas_Guru();
            case 10:
                return new BankTugasKelas_Guru();
            case 11:
                return new RecyleViewMateri_Guru();
            case 12:
                return new RecycleViewTugasGuru();
            case 13:
                return new RecycleViewBankTugas_Guru();
            default:
                return new DashboardGuruFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 14;
    }
}

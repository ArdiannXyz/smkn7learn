package com.example.smk7;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smk7.Guru.BankMateri_Guru;
import com.example.smk7.Guru.BankTugas_Guru;
import com.example.smk7.Guru.DashboardGuruFragment;
import com.example.smk7.Guru.ProfilGuru;
import com.example.smk7.Guru.UploadMateri_Guru;
import com.example.smk7.Guru.UploadTugas_Guru;

public class ViewPagerAdapterGuru extends FragmentStateAdapter {
    public ViewPagerAdapterGuru(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new DashboardGuruFragment();
        } else if (position == 1) {
            return new ProfilGuru();
        } else if (position == 2) {
            return new UploadMateri_Guru();
        } else if (position == 3) {
            return new UploadTugas_Guru();
        } else if (position == 4) {
            return new BankTugas_Guru();
        } else if (position == 5) {
            return new BankMateri_Guru();
        } else {
            return new DashboardGuruFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}

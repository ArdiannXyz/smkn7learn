package com.example.smk7.Guru;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
            return new Kelas_guru();
        } else if (position == 2) {
            return new Profil_guru();
        } else if (position == 3) {
            return new MateriKelas_Guru();
        } else if (position == 4) {
            return new TugasKelas_Guru();
        } else if (position == 5) {
            return new BankTugasKelas_Guru();
        } else if (position == 6) {
            return new Profil_guru();
        } else if (position == 7) {
            return new ViewProfil_Guru();
        } else if (position == 8) {
            return new EditProfil_Guru();
        } else {
            return new DashboardGuruFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}

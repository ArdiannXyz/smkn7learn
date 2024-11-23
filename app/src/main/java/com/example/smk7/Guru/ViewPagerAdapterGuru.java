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
        switch (position) {
            case 0:
                return new DashboardGuruFragment();
            case 1:
                return new Kelas_guru();
            case 2:
                return new Profil_guru();
            case 3:
                return new MateriKelas_Guru(); // Upload Materi
            case 4:
                return new TugasKelas_Guru();
            case 5:
                return new BankTugasKelas_Guru();
            case 6:
                return new Profil_guru();
            case 7:
                return new ViewProfil_Guru();
            case 8:
                return new EditProfil_Guru();
            default:
                return new DashboardGuruFragment(); // Default fragment
        }
    }


    @Override
    public int getItemCount() {
        return 9;
    }
}

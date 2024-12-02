package com.example.smk7.Siswa;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdaptersiswa extends FragmentStateAdapter {
    public ViewPagerAdaptersiswa(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new DashboardSiswaFragment();
        } else if (position == 1) {
            return new Mapel_Siswa();
        } else if (position == 2) {
            return new Profil_Siswa();
        } else if (position == 3) {
            return new Materi_Siswa();
        } else if (position == 4) {
            return new Tugas_Siswa();
        } else if (position == 5) {
            return new ViewProfil_Siswa();
        } else if (position == 6) {
            return new EditProfil_Siswa();
        } else if (position == 7) {
            return new UploadTugas_Siswa();
        } else {
            return new DashboardSiswaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }
}

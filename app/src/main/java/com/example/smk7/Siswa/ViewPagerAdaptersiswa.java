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
        switch (position) {
            case 0:
                return new DashboardSiswaFragment();
            case 1:
                return new Mapel_Siswa();
            case 2:
                return new Profil_Siswa();
            case 3:
                return new RecycleViewMapelSiswa();
            case 4:
                return new ViewProfil_Siswa();
            case 5:
                return new EditProfil_Siswa();
            case 6:
                return new Tugas_Siswa();
            case 7:
                return new Materi_Siswa();
            case 8:
                return new DetailMateri_Siswa();
            case 9:
                return new UploadTugas_Siswa();
            case 10:
                return new DetailMateri_Siswa();
            default:
                return new DashboardSiswaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 11;
    }
}

package com.example.smk7.Admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smk7.Profil;
import com.example.smk7.TambahData;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new Dashboard();
        } else if (position == 1) {
            return new TambahData();
        } else if (position == 2) {
            return new Profil();
        } else if (position == 3) {
            return new DataSiswa();
        } else if (position == 4) {
            return new DataGuru();
        } else if (position == 5) {
            return new DataKelas();
        } else if (position == 6) {
            return new DataMapel();
        } else if (position == 7) {
            return new MasterSiswaKelas();
        } else if (position == 8) {
            return new MasterGuruMapel();
        } else if (position == 9) {
            return new UploadMapelFragment();
        } else {
            return new Dashboard();  // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
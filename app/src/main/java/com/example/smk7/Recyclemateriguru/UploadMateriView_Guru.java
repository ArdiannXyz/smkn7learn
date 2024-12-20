//package com.example.smk7.Recyclemateriguru;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.smk7.Guru.DashboardGuru;
//import com.example.smk7.R;
//
//public class UploadMateriView_Guru extends Fragment {
//
//    private ImageView BackButton ;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_upload_materi_view_guru, container, false);
//
//        BackButton = view.findViewById(R.id.back_Buttonedit);
//
//
//
//
//
//        BackButton.setOnClickListener(v -> {
//            if (getActivity() instanceof DashboardGuru) {
//                ((DashboardGuru) getActivity()).viewPager2.setCurrentItem(0);
//            }
//        });
//        return view;
//
//    }
//}
package com.example.smk7.Siswa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;


public class UploadTugas_Siswa extends Fragment {

    private Button buttonAddFile, buttonSubmitFile;
    private Uri fileUri;
    private WebView webView;
    private BottomNavigationHandler navigationHandler;
    private static final int PICK_FILE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploadtugas_siswa, container, false);

        buttonAddFile = view.findViewById(R.id.button_add_file);
        buttonSubmitFile = view.findViewById(R.id.button_submit_file);

        // Tombol tambahkan file klik handler
        buttonAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);  // Menggunakan onActivityResult
            }
        });

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();

            // Pastikan URI tidak null
            if (selectedFileUri != null) {
                Log.d("File Selected", "URI: " + selectedFileUri.toString());

                // Dapatkan ekstensi file
                String fileExtension = getFileExtension(selectedFileUri);
                Log.d("File Selected", "File Extension: " + fileExtension); // Debug ekstensi file

                // Tampilkan file jika ekstensi dikenali
                if (fileExtension != null) {
                    showFileInWebView(selectedFileUri);
                } else {
                    // Jika ekstensi file tidak dikenali
                    Toast.makeText(getActivity(), "Tipe file tidak dikenali", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }





    protected void showFileInWebView(Uri fileUri) {
        String fileExtension = getFileExtension(fileUri);
        WebView webView = getView().findViewById(R.id.webView);

        if ("pdf".equalsIgnoreCase(fileExtension)) {
            // Menampilkan PDF menggunakan Google Docs Viewer
            String pdfUrl = fileUri.toString();
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        } else if ("docx".equalsIgnoreCase(fileExtension) || "xlsx".equalsIgnoreCase(fileExtension)) {
            // Menampilkan file DOCX atau XLSX menggunakan Google Docs Viewer
            String fileUrl = fileUri.toString();
            webView.loadUrl("https://docs.google.com/viewer?url=" + fileUrl);
        } else if ("jpg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension)) {
            // Jika gambar, tampilkan gambar di WebView
            String imageUrl = fileUri.toString();
            webView.loadUrl(imageUrl);
        } else {
            // File tidak dikenali untuk ditampilkan di WebView
            Toast.makeText(getActivity(), "Format file tidak didukung untuk tampilan", Toast.LENGTH_SHORT).show();
        }
    }






    // Fungsi untuk mengambil ekstensi file dari URI
    private String getFileExtension(Uri uri) {
        String extension = null;

        // Jika URI berupa content://, cek MIME type
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String mimeType = getActivity().getContentResolver().getType(uri); // Ambil MIME type
                if (mimeType != null) {
                    extension = mimeType.split("/")[1]; // Ambil bagian ekstensi dari MIME type
                }
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // Jika URI berupa file://, kita bisa mengambil ekstensi dari path file langsung
            String filePath = uri.getPath();
            if (filePath != null && filePath.lastIndexOf(".") > 0) {
                extension = filePath.substring(filePath.lastIndexOf(".") + 1);
            }
        }

        // Debug: Tampilkan ekstensi yang terdeteksi
        Log.d("File Extension", "Extension: " + extension);
        return extension;
    }




    private String getRealPathFromURI(Uri contentUri) {
        String path = null;

        // Cek apakah URI adalah jenis 'content' atau file
        if ("content".equalsIgnoreCase(contentUri.getScheme())) {
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex != -1) {
                    path = cursor.getString(columnIndex); // Mengambil path file
                }
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(contentUri.getScheme())) {
            path = contentUri.getPath();
        }

        return path;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lanjutkan dengan membuka file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICK_FILE_REQUEST);
            } else {
                // Izin ditolak, beri tahu pengguna
                Toast.makeText(getActivity(), "Izin diperlukan untuk mengakses file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationHandler = (BottomNavigationHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomNavigationHandler");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (navigationHandler != null) {
            navigationHandler.hideBottomNav();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationHandler = null;
    }

}
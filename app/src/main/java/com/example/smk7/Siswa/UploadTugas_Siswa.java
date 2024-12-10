package com.example.smk7.Siswa;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smk7.BottomNavigationHandler;
import com.example.smk7.R;

import java.io.IOException;
import java.util.ArrayList;


public class UploadTugas_Siswa extends Fragment {

    private static final int PICK_FILES_REQUEST = 1;
    private LinearLayout filePreviewLayout;
    private Button btnUploadFile, btnSubmit;
    private ProgressBar uploadProgressBar;
    private ImageView BackButton;
    private ArrayList<Uri> selectedFiles = new ArrayList<>();
    private ArrayList<View> fileViews = new ArrayList<>();
    private BottomNavigationHandler navigationHandler;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_tugas_siswa, container, false);

        filePreviewLayout = view.findViewById(R.id.filePreviewLayout);
        btnUploadFile = view.findViewById(R.id.btnUploadFile);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        uploadProgressBar = view.findViewById(R.id.uploadProgressBar);
        BackButton = view.findViewById(R.id.back_button);

        BackButton.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardSiswa) {
                ViewPager2 viewPager = ((DashboardSiswa) getActivity()).viewPager2;
                viewPager.setCurrentItem(0, false);
                if (navigationHandler != null) {
                    navigationHandler.showBottomNav();
                }
            }
        });


        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFiles();
            }
        });

        return view;
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Pilih File"), PICK_FILES_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        selectedFiles.add(fileUri);
                        displayFile(fileUri);
                    }
                } else if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    selectedFiles.add(fileUri);
                    displayFile(fileUri);
                }
            }
        }
    }

    private void displayFile(Uri fileUri) {
        View fileView = getLayoutInflater().inflate(R.layout.file_preview_item, null);

        // Ambil nama file yang lebih informatif
        String fileName = getFileName(fileUri);
        TextView fileNameTextView = fileView.findViewById(R.id.fileName);
        fileNameTextView.setText(fileName);

        ImageView deleteIcon = fileView.findViewById(R.id.deleteIcon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePreviewLayout.removeView(fileView);
                selectedFiles.remove(fileUri);
            }
        });

        // Menambahkan preview file berdasarkan tipe file
        String fileType = getFileType(fileUri);
        if (fileType.equals("image")) {
            displayImagePreview(fileUri, fileView);
        } else if (fileType.equals("pdf")) {
            displayPdfPreview(fileUri, fileView);
        } else if (fileType.equals("ppt") || fileType.equals("docx")) {
            displayFilePreviewWithIcon(fileUri, fileView, fileType);
        } else if (fileType.equals("video")) {
            displayVideoPreview(fileUri, fileView);
        }

        // Membuka file saat preview diklik
        fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(fileUri);
            }
        });

        filePreviewLayout.addView(fileView);
        fileViews.add(fileView);
    }

    private String getFileName(Uri uri) {
        String fileName = null;

        if (uri.getScheme().equals("content")) {
            // Coba dapatkan nama file melalui ContentResolver
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index); // Nama file dari cursor
                }
                cursor.close();
            }
        }

        // Jika tidak dapatkan nama file, coba menggunakan last path segment
        if (fileName == null) {
            fileName = uri.getLastPathSegment();  // Jika file berasal dari path langsung
        }

        return fileName != null ? fileName : "Unknown File"; // Jika nama file kosong, tampilkan default
    }

    private String getFileType(Uri fileUri) {
        String mimeType = getContext().getContentResolver().getType(fileUri);
        if (mimeType != null) {
            if (mimeType.startsWith("image")) {
                return "image";
            } else if (mimeType.equals("application/pdf")) {
                return "pdf";
            } else if (mimeType.equals("application/vnd.ms-powerpoint") || mimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
                return "ppt";
            } else if (mimeType.equals("application/msword") || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                return "docx";
            } else if (mimeType.startsWith("video")) {
                return "video";
            }
        }
        return "unknown";
    }

    private void displayImagePreview(Uri fileUri, View fileView) {
        ImageView imagePreview = new ImageView(getContext());
        imagePreview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);  // Menyesuaikan gambar agar memenuhi layout
        imagePreview.setImageURI(fileUri);
        ((LinearLayout) fileView.findViewById(R.id.previewContainer)).addView(imagePreview);

    }


    private void displayPdfPreview(Uri pdfUri, View fileView) {
        // Menampilkan preview PDF (render halaman pertama)
        try {
            ParcelFileDescriptor fileDescriptor = getContext().getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfRenderer renderer = new PdfRenderer(fileDescriptor);
            if (renderer.getPageCount() > 0) {
                PdfRenderer.Page page = renderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();

                ImageView pdfPreview = new ImageView(getContext());
                pdfPreview.setImageBitmap(bitmap);
                pdfPreview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)); // Full screen
                ((LinearLayout) fileView.findViewById(R.id.previewContainer)).addView(pdfPreview);
            }
            renderer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFilePreviewWithIcon(Uri fileUri, View fileView, String fileType) {
        // Menambahkan preview untuk PPT, DOCX dengan icon di tengah
        ImageView fileIcon = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;



        fileIcon.setLayoutParams(layoutParams);// Center the icon

        if (fileType.equals("ppt")) {
            fileIcon.setImageResource(R.drawable.ppt); // Ganti dengan icon PPT
        } else if (fileType.equals("docx")) {
            fileIcon.setImageResource(R.drawable.docx); // Ganti dengan icon DOCX
        } else if (fileType.equals("video")) {
            fileIcon.setImageResource(R.drawable.multimedia); // Ganti dengan icon video
        }

        // Menambahkan ikon ke layout
        ((LinearLayout) fileView.findViewById(R.id.previewContainer)).addView(fileIcon);
    }

    private void displayVideoPreview(Uri videoUri, View fileView) {
        // Menambahkan thumbnail video (gunakan library atau cara lain)
        ImageView videoPreview = new ImageView(getContext());
        videoPreview.setImageResource(R.drawable.multimedia); // Icon video atau thumbnail
        videoPreview.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        ((LinearLayout) fileView.findViewById(R.id.previewContainer)).addView(videoPreview);
    }

    private void openFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getContext().getContentResolver().getType(fileUri));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Buka File"));
    }

    private void submitFiles () {
        uploadProgressBar.setVisibility(View.VISIBLE);
        // Simulate file upload progress
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectedFiles.size(); i++) {
                    try {
                        Thread.sleep(1000); // Simulate upload delay
                        final int progress = (int) (((i + 1) / (float) selectedFiles.size()) * 100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadProgressBar.setProgress(progress);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
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
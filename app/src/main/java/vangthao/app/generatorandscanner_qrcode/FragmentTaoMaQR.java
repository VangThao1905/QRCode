package vangthao.app.generatorandscanner_qrcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class FragmentTaoMaQR extends Fragment {
    private View rootView;

    private EditText edtGiaTriMaQR;
    private Button btnTaoNgay, btnTaiXuong;
    private ImageView imgViewKetQua;
    private TextView txtMaHienTai;
    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tao_ma_qr, container, false);
        AnhXa();
        init();
        return rootView;
    }

    private void init() {
        Drawable drawable = imgViewKetQua.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String ketQua = FragmentQuyet_Anh.scanQRImage(bitmap);
        txtMaHienTai.setText(ketQua);


        btnTaoNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String giaTriQR = edtGiaTriMaQR.getText().toString();
                if (giaTriQR.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.data_invalid, Toast.LENGTH_SHORT).show();
                } else {
                    QRGEncoder qrgEncoder = new QRGEncoder(giaTriQR, null, QRGContents.Type.TEXT, 1000);
                    Bitmap qrBitmaps = qrgEncoder.getBitmap();
                    imgViewKetQua.setImageBitmap(qrBitmaps);
                   txtMaHienTai.setText(giaTriQR);
                    edtGiaTriMaQR.setText("");
                }
            }
        });
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

        btnTaiXuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToGallery();
            }
        });

    }

    private void saveToGallery() {
        Drawable drawable = imgViewKetQua.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String saveImageURL = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "QRCode_" + timeStamp, "image of qr code");

        Uri saveImageURI = Uri.parse(saveImageURL);
        imgViewKetQua.setImageURI(saveImageURI);
        Toast.makeText(getActivity(), R.string.save_image_success, Toast.LENGTH_SHORT).show();
    }

    private void AnhXa() {
        edtGiaTriMaQR = rootView.findViewById(R.id.edtGiaTriQR);
        imgViewKetQua = rootView.findViewById(R.id.imgViewKetQua);
        btnTaoNgay = rootView.findViewById(R.id.btnTaoNgay);
        btnTaiXuong = rootView.findViewById(R.id.btnTaiXuong);
        txtMaHienTai = rootView.findViewById(R.id.txtMaHienTai);
    }
}


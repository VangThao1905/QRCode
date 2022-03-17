package vangthao.app.generatorandscanner_qrcode;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class FragmentQuyetMaQR extends Fragment {
    private View rootView;
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView txtKetQua;
    MediaPlayer mediaPlayer;
    String ketQua = "",checkLink = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quyet_ma_qr, container, false);
        AnhXa();
        init();
        return rootView;
    }

    private void init() {
        codeScanner = new CodeScanner(getActivity(), scannerView);
        mediaPlayer = MediaPlayer.create(getActivity(),R.raw.ting_sound);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ketQua = result.getText();
                        mediaPlayer.start();
                        if(ketQua.length() >= 4){
                            checkLink = ketQua.substring(0, 4);
                        }
                        //Log.d("CHECK",checkLink);
                        txtKetQua.setText(ketQua);
                        //txtKetQuaQuyetAnh.setMovementMethod(LinkMovementMethod.getInstance());
                        if (checkLink.equals("http")) {
                            txtKetQua.setText(ketQua);
                            txtKetQua.setTextColor(Color.BLUE);
                            txtKetQua.setPaintFlags(txtKetQua.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            txtKetQua.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(ketQua));
                                    startActivity(browserIntent);
                                }
                            });
                        }else{
                            txtKetQua.setText(ketQua);
                            txtKetQua.setTextColor(Color.BLACK);
                            txtKetQua.setPaintFlags(txtKetQua.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
                            txtKetQua.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

                        }
                    }
                });
            }
        });

        codeScanner.startPreview();

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }


    private void requestForCamera() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getActivity(), "Quyền truy cập Camera chưa được cấp phép!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void AnhXa() {
        scannerView = rootView.findViewById(R.id.scannerView);
        txtKetQua = rootView.findViewById(R.id.txtKetQua);
    }
}


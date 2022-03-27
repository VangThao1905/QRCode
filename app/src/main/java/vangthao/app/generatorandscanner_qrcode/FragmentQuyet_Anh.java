package vangthao.app.generatorandscanner_qrcode;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class FragmentQuyet_Anh extends Fragment {
    private View rootView;
    private ImageView imgViewHinhQuet;
    private Button btnBatDauQuet;
    private TextView txtKetQuaQuetAnh;
    private ImageButton imgBtnTaiAnhLen;
    private int REQUEST_CODE_IMAGE = 1;
    private String realPath = "", ketQua = "", checkLink = "";
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quyet_anh, container, false);
        ketQua = getActivity().getString(R.string.result);
        AnhXa();
        init();
        return rootView;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void init() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.ting_sound);
        imgBtnTaiAnhLen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnBatDauQuet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = imgViewHinhQuet.getDrawable();

                Bitmap bitmap = drawableToBitmap(drawable);
                ketQua = scanQRImage(bitmap);
                if (ketQua != null) {
                    mediaPlayer.start();
                    if (ketQua.length() >= 4) {
                        checkLink = ketQua.substring(0, 4);
                    }
                    //Log.d("CHECK",checkLink);
                    txtKetQuaQuetAnh.setText(ketQua);
                    //txtKetQuaQuyetAnh.setMovementMethod(LinkMovementMethod.getInstance());
                    if (checkLink.equals("http")) {
                        txtKetQuaQuetAnh.setText(ketQua);
                        txtKetQuaQuetAnh.setTextColor(Color.BLUE);
                        txtKetQuaQuetAnh.setPaintFlags(txtKetQuaQuetAnh.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txtKetQuaQuetAnh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ketQua));
                                startActivity(browserIntent);
                            }
                        });
                    } else {
                        txtKetQuaQuetAnh.setText(ketQua);
                        txtKetQuaQuetAnh.setTextColor(Color.BLACK);
                        txtKetQuaQuetAnh.setPaintFlags(txtKetQuaQuetAnh.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
                        txtKetQuaQuetAnh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.not_upload_qr_image_to_scan_notifications, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgViewHinhQuet.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String scanQRImage(Bitmap bitmap) {
        String contents = null;
        int[] array = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), array);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();

        try {
            Result result = reader.decode(binaryBitmap);
            contents = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return contents;
    }


    private void AnhXa() {
        imgBtnTaiAnhLen = rootView.findViewById(R.id.imgBtnTaiAnhLen);
        imgViewHinhQuet = rootView.findViewById(R.id.imgViewHinhQuyet);
        btnBatDauQuet = rootView.findViewById(R.id.btnBatDauQuyet);
        txtKetQuaQuetAnh = rootView.findViewById(R.id.txtKetQua_QuyetAnh);
    }
}


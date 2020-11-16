package vangthao.app.generatorandscanner_qrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class TrangChuActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        AnhXa();
        init();
    }

    private void init() {
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void AnhXa() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }
}
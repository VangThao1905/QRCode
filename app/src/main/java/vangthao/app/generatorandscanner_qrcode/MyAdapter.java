package vangthao.app.generatorandscanner_qrcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyAdapter extends FragmentStatePagerAdapter {
    private String listTab[] = {"Tạo mã", "Quyét-Camera","Quyét-ảnh"};
    FragmentTaoMaQR fragmentTaoMaQR;
    FragmentQuyetMaQR fragmentQuyetMaQR;
    FragmentQuyet_Anh fragmentQuyet_anh;

    public MyAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentTaoMaQR = new FragmentTaoMaQR();
        fragmentQuyetMaQR = new FragmentQuyetMaQR();
        fragmentQuyet_anh = new FragmentQuyet_Anh();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return fragmentTaoMaQR;
        }else if(position == 1){
            return fragmentQuyetMaQR;
        }else if(position == 2){
            return fragmentQuyet_anh;
        }else{
            //TODO nothing
        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}

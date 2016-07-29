package com.android.customview.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.android.customview.R;
import com.android.customview.fragment.RCViewFragment;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by litonghui on 2016/7/22.
 */
public class RCViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcview);
        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RCViewFragment rcviewFragment = new RCViewFragment();
            transaction.replace(R.id.sample_content_fragment,rcviewFragment);
            transaction.commit();
        }
        getmd5();
    }
    private void getmd5() {
        PackageInfo pkg;
        try {
            pkg = getPackageManager().getPackageInfo("com.sogou.gamebox",
                    PackageManager.GET_SIGNATURES);

            String md5 = null;
            if (pkg.signatures != null && pkg.signatures.length > 0) {
                Signature sig = pkg.signatures[0];
                String chars = sig.toCharsString();
                md5 = stringToMD5(chars);
                Log.v("litonghui","sig: "+sig);
                Log.v("litonghui","chars: "+chars);
                Log.v("litonghui","md5:"+md5);
            }
        } catch (Exception e) {

        }
    }
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }
}

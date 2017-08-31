package com.cheng315.chengnfc.utils.manager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;

import com.cheng315.chengnfc.MApplication;
import com.cheng315.chengnfc.utils.AppUtils;

/**
 * Created by Administrator on 2017/8/31.
 *
 */

public class NFCManager {

    private static final String TAG = "NFCManager";

    private static volatile NFCManager INSTANCE = null;

    private NfcAdapter mNfcAdapter;
    private Intent mIntent;
    private Context mContext;


    private NFCManager() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(MApplication.appContext);
    }


    /**
     *
     */
    public static NFCManager getInstance() {

        if (INSTANCE == null) {
            synchronized (NFCManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NFCManager();
                }
            }
        }

        return INSTANCE;
    }


    /**
     * huoqu dao xin de intent ;
     */
    public void onNewIntent(Intent intent) {

        if (intent == null) return;


        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {


            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage[] msg;

            if (rawMessage != null) {
                msg = new NdefMessage[rawMessage.length];
                for (int i = 0 ; i < rawMessage.length; i++) {
                    msg[i] = (NdefMessage) rawMessage[i];
                }
            }

            byte[] nfc_id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            Tag extra_tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] tag_id = extra_tag.getId();
            // 进制转换

            String hex = AppUtils.getHex(tag_id);


            if(mListener != null) {

                mListener.onTransmit(hex);
            }


        }


    }


    /**
     * 运行时 处理
     */
    public void onResume(Context context, PendingIntent pendingIntent) {

        if (mNfcAdapter != null) {

            if (!mNfcAdapter.isEnabled()) {
                return;
            }

            mNfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, null, null);
        }

    }


    /**
     * 暂停 处理
     */

    public void onPause(Context context) {
        if (mNfcAdapter != null) {

            mNfcAdapter.disableForegroundDispatch((Activity) context);

        }
    }



    private OnTagIdCompleteListener mListener;

    public void setOnTagIdCompleteListener(OnTagIdCompleteListener onTagIdCompleteListener) {

        this.mListener = onTagIdCompleteListener;
    }



    public  interface OnTagIdCompleteListener{

        void onTransmit(String tagId);
    }

}

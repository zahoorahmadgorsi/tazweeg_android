package com.bulahej.tazweeg.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.utilties.ImageProcessing;
import com.bulahej.tazweeg.utilties.Utilities;
import com.kyanogen.signatureview.SignatureView;

import java.io.File;

public class SignatureFragment extends DialogFragment {

    private View view;
    private SignatureView svSignaturePad = null;
    private Button btnCancel, btnSaveSignature, btnClearSignature;

    private ImageProcessing imageProcessing = null;
    private OnGetSignatureClickListener clickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signature, container, false);
        initUIElements();
        setButtonsClicks();
        return view;

    }

    public void setOnGetSignatureClickListener(OnGetSignatureClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void setButtonsClicks() {

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(SignatureFragment.this).commit();
//                getFragmentManager().popBackStack();
            }
        });

        btnSaveSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.myLogDebug("onClickSaveSig()");
                if (svSignaturePad.isBitmapEmpty() == false && svSignaturePad.isEnableSignature() == true) {
                    Utilities.myLogDebug("signature != null");
                    Bitmap bitmap = svSignaturePad.getSignatureBitmap();
                    imageProcessing = new ImageProcessing(getActivity());
                    imageProcessing.savePictureInCacheMemory(bitmap);
                    dismissAllowingStateLoss();
                    if (clickListener != null) {
                        clickListener.onGetSignature(bitmap);
                    }
                }
            }
        });

        btnClearSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svSignaturePad.clearCanvas();
            }
        });

    }

    public Bitmap getSignatureBitmap() {
        Utilities.myLogDebug("getSignatureBitmap()");

        if (svSignaturePad != null) {
            Utilities.myLogDebug("getSignatureBitmap(), bitmap != null");
//            return imageProcessing.loadPictureFormCacheMemory();
            return svSignaturePad.getSignatureBitmap();
        }

        return null;
    }

    public File getSignatureImagePath() {

        if (imageProcessing != null) {
            Utilities.myLogDebug("Signature image path: " + imageProcessing.loadCacheFilePath());
            return imageProcessing.loadCacheFilePath();
        }
        return null;
    }

    private void initUIElements() {
        btnCancel = view.findViewById(R.id.btnCancel);
        svSignaturePad = view.findViewById(R.id.svSignaturePad);
        btnSaveSignature = view.findViewById(R.id.bSaveSig);
        btnClearSignature = view.findViewById(R.id.bClearSig);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismissAllowingStateLoss();
            }
        };
    }

    public interface OnGetSignatureClickListener {
        void onGetSignature(Bitmap bitmap);
    }

}

package com.wonders.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.legal_rights.R;

public class LoadingDialog {
    private static Dialog loadingDialog;

    public static void show(Context context) {
        show(context,true);
    }

    public static void show(Context context,boolean cancelable){
        if (loadingDialog == null){
            Dialog dialog = new Dialog(context, R.style.loading_dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading_dialog, null);
            dialog.setContentView(view);
            ImageView spaceshipImage = view.findViewById(R.id.img);
            Animation runAnim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog);
            spaceshipImage.startAnimation(runAnim);
            dialog.setCancelable(cancelable);

            dialog.show();

            loadingDialog = dialog;
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loadingDialog = null;
                }
            });
        }
    }

    public static void dismiss() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}

package mx.unam.ciencias.jcasas.pendejifest.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import mx.unam.ciencias.jcasas.pendejifest.R;

/**
 * Created by jcasas on 7/25/17.
 */

public class ActivityUtils {

    public static AlertDialog makeStdAlert(String title, String message, Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getStringByRes(R.string.dialog_positive_std, context),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public static String getStringByRes(int id, Context context) {
        return context.getResources().getString(id).toString();
    }
}

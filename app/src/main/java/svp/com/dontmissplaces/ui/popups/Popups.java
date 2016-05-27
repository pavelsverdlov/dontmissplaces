package svp.com.dontmissplaces.ui.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;

import svp.com.dontmissplaces.R;

public class Popups {

    public static class SavePlacePopup {
        public interface OnClickListener {
            void onSaveClick();

            void onCancelClick();
        }

        private final View view;
        private final AlertDialog.Builder builder;
        AlertDialog dialog;

        public SavePlacePopup(Activity context) {
            builder = new AlertDialog.Builder(context);
            view = context.getLayoutInflater().inflate(R.layout.popup_add_new_place, null);
            builder.setView(view);
        }

        public void setOnClickListener(OnClickListener listener) {
            /*
            builder// Add action buttons
                    .setPositiveButton(android.R.string..signin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            close();
                        }
                    });
                    */
        }

        public SavePlacePopup show() {
            dialog = builder.create();
            dialog.show();
            return this;
        }

        public void close() {
            dialog.cancel();
        }
    }

}

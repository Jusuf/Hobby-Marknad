package se.netmine.hobby_marknad;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jusuf on 2018-02-25.
 */

public class ConnectServiceDialog extends DialogFragment {
//

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View connectServiceDialogView = inflater.inflate(R.layout.connect_service_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);


        Button buttonVinNumber = (Button) connectServiceDialogView.findViewById(R.id.buttonVinNumber);
        Button buttonCancel = (Button) connectServiceDialogView.findViewById(R.id.buttonCancel);

        buttonVinNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ConnectToServiceFragment fragment = new ConnectToServiceFragment();
//                mainActivity.onNavigateToFragment(fragment);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(connectServiceDialogView);

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM| Gravity.CENTER;
        wmlp.y = 200;   //y position

        return dialog;
    }
}

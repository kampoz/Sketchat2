package com.kampoz.sketchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.GroupRealm;

/**
 * Created by wasili on 2017-04-24.
 */

public class AddGroupDialogFragment extends DialogFragment {

    private Context context;
    private EditText etGroupName;
    private Button bCancel;
    private Button bOK;
    public AddGroupDialogFragmentListener listener;

    public interface AddGroupDialogFragmentListener{
        void onCancelClickInAddGroup();
        void onOKClickInAddGroup();
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.group_add_dialog, null);

        etGroupName = (EditText)view.findViewById(R.id.etGroupName);
        bCancel = (Button)view.findViewById(R.id.bCancelinAddGroup);
        bOK = (Button)view.findViewById(R.id.bOKinAddGroup);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupRealm groupRealm = new GroupRealm();
                groupRealm.setGroupName(etGroupName.getText().toString());
                groupRealm.addNewGroup(groupRealm);
//                groupRealmToEdit.changeName(etGroupName.getText().toString());
//                listener.onOKclick();
                getDialog().dismiss();
                listener.onOKClickInAddGroup();
            }
        });


        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(AddGroupDialogFragmentListener listener) {
        this.listener = listener;
    }
}

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
import com.kampoz.sketchat.dao.GroupDao;
import com.kampoz.sketchat.realm.GroupRealm;

/**
 * Created by wasili on 2017-04-24.
 */

public class AddGroupDialogFragment extends DialogFragment {

    private Context context;
    private EditText etGroupName;
    private Button bCancel;
    private Button bOK;
    private GroupDao groupDao;
    public AddGroupDialogFragmentListener listener;

    public interface AddGroupDialogFragmentListener{
        void onOKClickInAddGroup(String groupName);
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);
        groupDao = new GroupDao();
        etGroupName = (EditText)view.findViewById(R.id.etName);
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
                groupDao.addNewGroup(groupRealm);
                getDialog().dismiss();
                listener.onOKClickInAddGroup(etGroupName.getText().toString());
            }
        });


        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        groupDao.getRealm().close();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(AddGroupDialogFragmentListener listener) {
        this.listener = listener;
    }
}

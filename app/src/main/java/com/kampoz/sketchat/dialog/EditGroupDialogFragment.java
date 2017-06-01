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

public class EditGroupDialogFragment extends DialogFragment {

    private Context context;
    private GroupRealm groupRealmToEdit;
    private EditText etGroupName;
    private Button bDeleteGroup;
    private Button bCancel;
    private Button bOK;
    public EditGroupDialogFragmentListener listener;
    private GroupDao groupDao;

    public interface EditGroupDialogFragmentListener{
        void onCancelClickInEdit();
        void onOKClickInEdit();
        void onDeleteGroupClickInEdit(String groupName);
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);
        bDeleteGroup = (Button) view.findViewById(R.id.bDelete);
        bCancel = (Button) view.findViewById(R.id.bCancel);
        bOK = (Button) view.findViewById(R.id.bOK);
        etGroupName = (EditText)  view.findViewById(R.id.etChangeName);
        etGroupName.setText(groupRealmToEdit.getGroupName());
        groupDao = new GroupDao();

        bDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupRealmToEdit.getGroupName().toString();
                //groupRealmToEdit.deleteGroup(groupRealmToEdit.getId());
                groupDao.deleteGroup(groupRealmToEdit.getId());
                listener.onDeleteGroupClickInEdit(groupName);
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClickInEdit();
            }
        });

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //groupRealmToEdit.changeName(etGroupName.getText().toString());
                groupDao.changeName(etGroupName.getText().toString(), groupRealmToEdit.getId());
                listener.onOKClickInEdit();
            }
        });

        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GroupRealm getGroupRealmToEdit() {
        return groupRealmToEdit;
    }

    public void setGroupRealmToEdit(GroupRealm groupRealm) {
        this.groupRealmToEdit = groupRealm;
    }

    public void setEditGroupDialogFragmentListener(EditGroupDialogFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        groupDao.closeRealmInstance();
    }
}

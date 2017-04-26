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
import com.kampoz.sketchat.fragments.GroupsFragment;
import com.kampoz.sketchat.realm.GroupRealm;

import io.realm.Realm;

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

    public interface EditGroupDialogFragmentListener{
        void onCancelClick();
        void onOKclick();
        void onDeleteGroupClick(String groupName);
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.group_edit_dialog, null);
        bDeleteGroup = (Button) view.findViewById(R.id.bDeleteGroupinEditGroup);
        bCancel = (Button) view.findViewById(R.id.bCancelinEditGroup);
        bOK = (Button) view.findViewById(R.id.bOKinEditGroup);
        etGroupName = (EditText)  view.findViewById(R.id.etChangeGroupName);
        etGroupName.setText(groupRealmToEdit.getGroupName());


        bDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupRealmToEdit.getGroupName().toString();
                groupRealmToEdit.deleteGroup(groupRealmToEdit.getId());
                listener.onDeleteGroupClick(groupName);
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick();
            }
        });

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupRealmToEdit.changeName(etGroupName.getText().toString());
                listener.onOKclick();
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
}

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
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.SubjectRealm;

/**
 * Created by wasili on 2017-04-24.
 */

public class EditSubjectDialogFragment extends DialogFragment {

    private Context context;
    private SubjectRealm subjectRealmToEdit;
    private EditText etSubjectName;
    private Button bDeleteSubject;
    private Button bCancel;
    private Button bOK;
    private TextView tvNameLabel;
    private TextView tvEditDialogLabel;
    private int groupId;
    public EditSubjectDialogFragmentListener listener;

    public interface EditSubjectDialogFragmentListener{
        void onCancelClickInEdit();
        void onOKClickInEdit();
        void onDeleteSubjectClickInEdit(String subjectName, int groupId);
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);
        tvEditDialogLabel = (TextView)view.findViewById(R.id.tvEditDialogLabel);
        tvEditDialogLabel.setText("Subject edit");
        tvNameLabel = (TextView)view.findViewById(R.id.tvNameLabel);
        tvNameLabel.setText("Change subject's name");
        bDeleteSubject = (Button) view.findViewById(R.id.bDelete);
        bDeleteSubject.setText("Delete subject");
        bCancel = (Button) view.findViewById(R.id.bCancel);
        bOK = (Button) view.findViewById(R.id.bOK);
        etSubjectName = (EditText)  view.findViewById(R.id.etChangeName);
        etSubjectName.setText(subjectRealmToEdit.getSubject());


        bDeleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = subjectRealmToEdit.getSubject();
                subjectRealmToEdit.deleteSubject(subjectRealmToEdit.getId());
                listener.onDeleteSubjectClickInEdit(subjectName, groupId);
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
                subjectRealmToEdit.changeName(etSubjectName.getText().toString());
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

    public SubjectRealm getSubjectRealmToEdit() {
        return subjectRealmToEdit;
    }

    public void setSubjectRealmToEdit(SubjectRealm subjectRealm) {
        this.subjectRealmToEdit = subjectRealm;
    }

    public void setEditSubjectDialogFragmentListener(EditSubjectDialogFragmentListener listener) {
        this.listener = listener;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }
}

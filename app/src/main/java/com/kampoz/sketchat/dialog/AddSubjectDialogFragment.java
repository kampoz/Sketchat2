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
import com.kampoz.sketchat.realm.SubjectRealm;

/**
 * Created by wasili on 2017-04-24.
 */

public class AddSubjectDialogFragment extends DialogFragment {

    private Context context;
    private EditText etSubjectName;
    private Button bCancel;
    private Button bOK;
    public AddSubjectDialogFragmentListener listener;

    public interface AddSubjectDialogFragmentListener {
        void onOKClickInAddSubject(String groupName);
    }

    @Override
    public Dialog onCreateDialog(Bundle ssvadInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);

        etSubjectName = (EditText)view.findViewById(R.id.etGroupName);
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
                SubjectRealm subjectRealm = new SubjectRealm();
                subjectRealm.setSubject(etSubjectName.getText().toString());
                subjectRealm.addNewSubject(subjectRealm);
//                groupRealmToEdit.changeName(etSubjectName.getText().toString());
//                listener.onOKClickInEdit();
                getDialog().dismiss();
                listener.onOKClickInAddSubject(etSubjectName.getText().toString());
            }
        });


        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(AddSubjectDialogFragmentListener listener) {
        this.listener = listener;
    }
}

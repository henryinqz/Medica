package com.example.medical_clinic_scheduling_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.HashSet;

public class MultipleSpecialistSelectionFragment extends DialogFragment {

    private HashSet<String> selectedItems = new HashSet<String>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        String[] selectionList = getActivity().getResources().getStringArray(R.array.SelectSpecialist);
        dialogBuilder.setTitle("Please Select from the Following Specialists");
        dialogBuilder.setMultiChoiceItems(selectionList, null, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int a, boolean b){
                if(b) {
                    selectedItems.add(selectionList[a]);
                }else{
                    selectedItems.remove(selectionList[a]);
                }
            }
        });

        dialogBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int a){

            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int a){

            }
        });
        return dialogBuilder.create();
    }

    public HashSet<String> getSelectedItems(){
        return this.selectedItems;
    }

}

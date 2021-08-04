package com.example.medical_clinic_scheduling_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.HashSet;

public class MultipleSelectionFragment extends DialogFragment {
    private HashSet<String> selectedItems;
    private String[] selectionList;
    private String dialogTitle;

    public MultipleSelectionFragment(String[] selectionList) {
        this.selectionList = selectionList;
        this.dialogTitle = "Select items";

        this.selectedItems = new HashSet<String>();
    }
    public MultipleSelectionFragment(String[] selectionList, String dialogTitle) {
        this.selectionList = selectionList;
        this.dialogTitle = dialogTitle;

        this.selectedItems = new HashSet<String>();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(this.dialogTitle);

        dialogBuilder.setMultiChoiceItems(this.selectionList, null, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int clickedIndex, boolean isSelected) {
                if (isSelected)
                    selectedItems.add(selectionList[clickedIndex]);
                else
                    selectedItems.remove(selectionList[clickedIndex]);
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

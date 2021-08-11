package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.motion.toasterlibrary.FormInputLayout;

public class FirstFragment extends Fragment {

    FormInputLayout firstForm;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstForm = view.findViewById(R.id.firstForm);
   //     firstForm.setMaxLenth(10);
    //    firstForm.setDrawableLeft(android.R.drawable.ic_delete,20);
      //  firstForm.visibilityPassword(true);
//        firstForm.setDrawableLeftExtra(android.R.drawable.ic_delete,20);
        //firstForm.setDrawableRightExtra(android.R.drawable.ic_delete,20);
   //     firstForm.setPadding(150,0,150,0);
     //   firstForm.setMarginOutside(150,120,150,120);
     //   firstForm.setDrawableBottom(android.R.drawable.ic_delete,150);
      //  firstForm.setDrawableRight(android.R.drawable.ic_delete,150);
    //    firstForm.setDrawableTop(android.R.drawable.ic_delete,150);
        firstForm.setHelperMessage("Helper Text");
        firstForm.setCounter(20);
        firstForm.setMaxLines(5);
        firstForm.setMaxLength(20);




        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

    }
}
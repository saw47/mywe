package ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.github.saw47.mywe.databinding.FragmentAddPageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import viewmodel.ViewModelMain;

public class AddPageFragment extends Fragment {

    private FragmentAddPageBinding binding;
    private ViewModelMain model;
    private final String TAG = "MW-APF";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(ViewModelMain.class);
        binding = FragmentAddPageBinding.inflate(inflater, container, false);

        binding.textFieldValue.requestFocus();
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        );

        if (model.tempNote != null) {
            binding.textFieldValue.setText(model.tempNote.getTextNote());
            //TODO Add fill another fields
        }


        binding.textFieldValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                model.tempText = binding.textFieldValue.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(binding.textFieldValue.getWindowToken(), 0);
        super.onStop();
    }

}
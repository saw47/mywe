package ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.FragmentMainBinding;

import java.util.List;
import java.util.Objects;

import adapter.NoteAdapter;
import object.Note;
import viewmodel.ViewModelMain;

public class MainFragment extends Fragment {

    private final String TAG = "MW-MF";

    private FragmentMainBinding binding;
    private ViewModelMain model;
    private NoteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(ViewModelMain.class);

        binding = FragmentMainBinding.inflate(inflater, container, false);

        adapter = new NoteAdapter(this.getContext(), model.data, model);
        binding.mainRv.setItemAnimator(null);
        binding.mainRv.setAdapter(adapter);

        model.data.observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Note> notes) {
                Log.d(TAG, "onChanged in observer, call submitList notes size " + notes.size());
                adapter.submitList(notes);
                binding.mainRv.setAdapter(adapter);
            }
        });




        model.noteIsSelectedEvent.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged (Object o) {
                if (Boolean.FALSE.equals(model.noteIsSelectedEvent.getValue()))
                {
                    //binding.mainRv.setAdapter(adapter);
                    //adapter.submitList(null);
                    //adapter.submitList(model.data.getValue());
                }
            }
        });

        model.noteIsDeletedEvent.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged (Object o) {
                if (Boolean.TRUE.equals(model.noteIsDeletedEvent.getValue()))
                {
                    adapter.submitList(null);
                    binding.mainRv.setAdapter(adapter);
                    model.clearDeleteNotesLiveEvent();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
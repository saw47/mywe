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
    private LiveData<List<Note>> notesLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(ViewModelMain.class);
        notesLiveData = model.data;
        binding = FragmentMainBinding.inflate(inflater, container, false);
        adapter = new NoteAdapter(this.getContext(), Objects.requireNonNull(model.data.getValue()), model);

        binding.mainRv.setAdapter(adapter);

        notesLiveData.observe(getViewLifecycleOwner(), new Observer() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged (Object o) {
                adapter.refreshListData(Objects.requireNonNull(notesLiveData.getValue()));
                Log.d(TAG, "onChanged qty notes " + notesLiveData.getValue().size());
                adapter.notifyDataSetChanged();
            }
        });


        model.noteIsSelectedEvent.observe(getViewLifecycleOwner(), new Observer() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged (Object o) {
                if (Boolean.FALSE.equals(model.noteIsSelectedEvent.getValue()))
                {
                    binding.mainRv.setAdapter(adapter);
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
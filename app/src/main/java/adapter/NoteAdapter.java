package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.FeedItemCardBinding;

import java.util.List;

import listener.NoteCardClickListener;
import object.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final String TAG = "MW-NA";

    private List<Note> data;
    private FeedItemCardBinding binding;
    private LayoutInflater inflater;
    private NoteCardClickListener listener;

    public NoteAdapter(Context context, List<Note> data, NoteCardClickListener listener) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshListData(List<Note> newData) {
        this.data = newData;
        //notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        Note note;

        public ViewHolder(View view) {
            super(view);
        }

        public void bind(Note note, FeedItemCardBinding binding) {
            this.note = note;
            binding.text.setText(note.getTextNote());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = FeedItemCardBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Note note = data.get(position);
        viewHolder.bind(note, binding);

        binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFrameClick(note);
                Log.d("MW-NA", "onFrameClick " + note.getNumber() + " " + note.getTextNote());
            }
        });

        binding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View v) {
                v.setBackgroundColor(R.color.md_theme_dark_onPrimary); //TODO temp
                v.setAlpha(0.5F);
                listener.onframelongclick(note);
                Log.d("MW-NA", "onLongClick " + note.getNumber() + " " + note.getTextNote());
                return true;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }


}

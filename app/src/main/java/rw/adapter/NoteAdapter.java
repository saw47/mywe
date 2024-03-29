package rw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.github.saw47.mywe.databinding.FeedItemCardBinding;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import listener.NoteCardClickListener;
import object.Note;
import rw.helper.ItemTouchHelperAdapter;
import util.TabPositionState;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private final String TAG = "MW-NA";

    private final LiveData<List<Note>> notes;
    private FeedItemCardBinding binding;
    private final LayoutInflater inflater;
    private final NoteCardClickListener listener;

    public NoteAdapter(Context context, LiveData<List<Note>> data, NoteCardClickListener listener) {
        super(DIFF_CALLBACK);
        this.notes = data;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = FeedItemCardBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Note note = Objects.requireNonNull(getNotesValue()).get(position);
        viewHolder.bind(note, binding);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Note oldNote, @NonNull Note newNote) {
                    return (oldNote.getNumber() == newNote.getNumber()) &&
                            (oldNote.getTextNote().equals(newNote.getTextNote()));
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldNote, @NonNull Note newNote) {
                    return oldNote.equals(newNote);
                }
            };

    @Override
    public void onItemDismiss(int position) {
        Note note = Objects.requireNonNull(getNotesValue()).get(position);
        listener.changeActualNoteState(note);
        notifyItemRemoved(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        Note note;

        public ViewHolder(FeedItemCardBinding binding, NoteCardClickListener listener) {
            super(binding.getRoot());
            binding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!listener.noteIsSelectedState()) {
                        listener.onFrameClick(note);
                    } else {
                        if (listener.getTempSelectedNotes().contains(note)) {
                            v.setAlpha(1F);
                            listener.unselectNote(note);
                        } else {
                            v.setAlpha(0.5F);
                            listener.onFrameLongClick(note);
                        }
                    }
                }
            });


            binding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener.getTempSelectedNotes().contains(note)) {
                        v.setAlpha(1F);
                        listener.unselectNote(note);
                    } else {
                        v.setAlpha(0.5F);
                        listener.onFrameLongClick(note);
                    }
                    return true;
                }
            });
        }

        public void bind(Note note, FeedItemCardBinding binding) {
            this.note = note;
            binding.text.setText(note.getTextNote());
            binding.sportNoteFlag.setVisibility((note.getIsSportNote()) ? View.VISIBLE : View.GONE );
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(getNotesValue()).size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private List<Note> getNotesValue() {
        TabPositionState st = listener.getTabState();
        return Objects.requireNonNull(notes.getValue()).stream().filter(note -> note.getNoteState().equals(st)).collect(Collectors.toList());
    }
}

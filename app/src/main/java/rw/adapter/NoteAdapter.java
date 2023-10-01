package rw.adapter;

import android.content.Context;
import android.util.Log;
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
import rw.helper.ItemTouchHelperViewHolder;
import util.TabPositionState;

// 49 75 146
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
        Log.d(TAG, "onCreateViewHolder");
        return new ViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Note note = Objects.requireNonNull(getNotesValue()).get(position);
        viewHolder.bind(note, binding);
        Log.d(TAG, "onBindViewHolder number  " + note.getNumber() + " text " + note.getTextNote() + " position " + viewHolder.getAdapterPosition());
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Note oldNote, @NonNull Note newNote) {
                    return oldNote.getNumber() == newNote.getNumber();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldNote, @NonNull Note newNote) {
                    return oldNote.equals(newNote);
                }
            };

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // TODO not impl
    }

    @Override
    public void onItemDismiss(int position) {
        Note note = Objects.requireNonNull(getNotesValue()).get(position);
        listener.changeActualNoteState(note);
        notifyItemRemoved(position);
        // TODO hz how it work
    }


    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        Note note;

        public ViewHolder(FeedItemCardBinding binding, NoteCardClickListener listener) {
            super(binding.getRoot());

            binding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!listener.noteIsSelectedState()) {
                        Log.d(TAG, "Short unselected click note " + note.getTextNote() + " position " + getAdapterPosition());
                        listener.onFrameClick(note);
                    } else {
                        if (listener.getTempSelectedNotes().contains(note)) {
                            Log.d(TAG, "onClick unselect note " + note.getTextNote() + " position " + getAdapterPosition());
                            v.setAlpha(1F);
                            listener.unselectNote(note);
                        } else {
                            Log.d(TAG, "onClick select note " + note.getTextNote() + " position " + getAdapterPosition());
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
                        Log.d(TAG, "onLongClick unselect note " + note.getTextNote() + " position " + getAdapterPosition());
                        v.setAlpha(1F);
                        listener.unselectNote(note);
                    } else {
                        Log.d(TAG, "onLongClick select note " + note.getTextNote() + " position " + getAdapterPosition());
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

        @Override
        public void onItemSelected() {
            // TODO not impl
        }

        @Override
        public void onItemClear() {
            // TODO not impl
        }
    }


    @Override
    public int getItemCount() {
        //Log.d(TAG, "getItemCount call");
        return Objects.requireNonNull(getNotesValue()).size();
    }

    @Override
    public long getItemId(int position) {
        //Log.d(TAG, "getItemId call");
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d(TAG, "getItemViewType call");
        return position;
    }

    private List<Note> getNotesValue() {
        TabPositionState st = listener.getTabState();
        return (st == TabPositionState.OLD) ? Objects.requireNonNull(notes.getValue())
                .stream().filter(note -> note.getNoteState().equals(st))
                .collect(Collectors.toList()) : notes.getValue();
    }

}

package com.example.lebrun_nicolas.myapplication.fragments.notes;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lebrun_nicolas.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NoteItem} and makes a call to the
 * specified {@link NotesFragment.OnItemClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<NoteItem> values;
    private final NotesFragment.OnItemClickListener listener;

    ListAdapter(List<NoteItem> items, NotesFragment.OnItemClickListener listener) {
        values = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        holder.item = values.get(position);
        holder.title.setText(values.get(position).getTitle());
        holder.note.setText(Html.fromHtml(values.get(position).getNote()));
        holder.date.setText(format.format(values.get(position).getDate()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onNoteItemClick(holder.item);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        final TextView title;
        final TextView note;
        final TextView date;
        NoteItem item;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.title);
            note = (TextView) view.findViewById(R.id.note);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
}




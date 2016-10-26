package com.example.lebrun_nicolas.myapplication.fragments.notes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jp.wasabeef.richeditor.RichEditor;

import com.example.lebrun_nicolas.myapplication.DAL.DaoNote;
import com.example.lebrun_nicolas.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteDetailFragment.OnNoteDetailListener} interface
 * to handle interaction events.
 * Use the {@link NoteDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDetailFragment extends Fragment {

    private static final String NOTE_ID = "param1";
    private static final String NOTE_TITLE = "param2";
    private static final String NOTE_NOTE = "param3";
    private static final String NOTE_DATE = "param4";

    private DaoNote daoNote;

    private String mId;
    private String mTitle;
    private String mNote;
    private String mDate;

    private EditText titleEditText;
    private OnNoteDetailListener mListener;
    private Button saveButton;
    private Button deleteButton;
    private RichEditor richEditor;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item Parameter 1.
     * @return A new instance of fragment NoteDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteDetailFragment newInstance(NoteItem item) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        NoteDetailFragment fragment = new NoteDetailFragment();

        Bundle args = new Bundle();

        args.putString(NOTE_ID, item.getId()+"");
        args.putString(NOTE_TITLE, item.getTitle());
        args.putString(NOTE_NOTE, item.getNote());
        if(item.getDate() != null) {
            args.putString(NOTE_DATE, format.format(item.getDate()));
        } else {
            args.putString(NOTE_DATE, "");
        }
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(NOTE_ID);
            mTitle = getArguments().getString(NOTE_TITLE);
            mNote = getArguments().getString(NOTE_NOTE);
            mDate = getArguments().getString(NOTE_DATE);
        }

        this.daoNote = DaoNote.getInstance(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);

        this.richEditor = (RichEditor) view.findViewById(R.id.editor);
        richEditor.setHtml(mNote);
        richEditor.setEditorHeight(200);

        this.titleEditText = (EditText) view.findViewById(R.id.title_edit);
        this.titleEditText.setText(mTitle);
        this.saveButton = (Button) view.findViewById(R.id.save_button);
        this.deleteButton = (Button) view.findViewById(R.id.delete_button);

        if(!mId.equals("0")) {
            this.saveButton.setText("Save");
            ((TextView)view.findViewById(R.id.date)).setText(mDate);
        } else {
            this.saveButton.setText("Create");
            this.deleteButton.setVisibility(View.INVISIBLE);
        }

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });

        return view;
    }

    public void saveNote() {

        mTitle = this.titleEditText.getText().toString();
        mNote = this.richEditor.getHtml();

        NoteItem note = new NoteItem(Integer.valueOf(mId), mTitle, mNote, null);
        if(!mId.equals("0")) {
            daoNote.updateNote(note);
        } else {
            daoNote.insertNote(note);
        }

        getFragmentManager().popBackStack();

    }

    public void deleteNote() {
        daoNote.deleteNote(Integer.valueOf(mId));
        getFragmentManager().popBackStack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteDetailListener) {
            mListener = (OnNoteDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoteDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNoteDetailListener {
        // TODO: Update argument type and name
        void onNoteDetailInteraction(Uri uri);
    }
}

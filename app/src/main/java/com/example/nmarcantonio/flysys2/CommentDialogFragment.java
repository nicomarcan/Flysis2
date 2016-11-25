package com.example.nmarcantonio.flysys2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private View view;
    private OnFragmentInteractionListener mListener;
    private AtomicBoolean thumbs_up;
    private float[] ratingsArray = {
        2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f
    };
    private String airline;
    private String number;
    private String comment;

    public CommentDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        if (isInLayout()) {
            return super.onCreateDialog(bundle);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_comment_dialog, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        initView(view);
        return dialog;
    }

    // TODO: Rename and change types and number of parameters
    public static CommentDialogFragment newInstance(Boolean thumbs, String airline, String number ) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("thumbs", thumbs);
        args.putString("airline", airline);
        args.putString("number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && thumbs_up == null) {
            thumbs_up = new AtomicBoolean(getArguments().getBoolean("thumbs"));
            airline = getArguments().getString("airline");
            number = getArguments().getString("number");
        }
    }

    public void setThumbs(Boolean thumbs) {
        if (thumbs_up != null) {
            thumbs_up.set(thumbs);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        final ImageButton thumbsUpButton = (ImageButton) view.findViewById(R.id.flight_comments_dialog_thumbs_up);
        final ImageButton thumbsDownButton = (ImageButton) view.findViewById(R.id.flight_comments_dialog_thumbs_down);
        if (thumbs_up.get()) {
            thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_green_24dp));
            thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
        }
        else {
            thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
            thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp));
        }

        RatingBar friendliness = (RatingBar) view.findViewById(R.id.flight_comment_dialog_friendliness);
        friendliness.setRating(ratingsArray[0]);
        RatingBar food = (RatingBar) view.findViewById(R.id.flight_comment_dialog_food);
        food.setRating(ratingsArray[1]);
        RatingBar punctuality = (RatingBar) view.findViewById(R.id.flight_comment_dialog_puntctuality);
        punctuality.setRating(ratingsArray[2]);
        RatingBar mileage_programe = (RatingBar) view.findViewById(R.id.flight_comment_dialog_mileage_programe);
        mileage_programe.setRating(ratingsArray[3]);
        RatingBar comfort = (RatingBar) view.findViewById(R.id.flight_comment_dialog_comfort);
        comfort.setRating(ratingsArray[4]);
        RatingBar quality_price = (RatingBar) view.findViewById(R.id.flight_comment_dialog_quality_price);
        quality_price.setRating(ratingsArray[5]);
        TextView commentView = (TextView) view.findViewById(R.id.flight_comment_dialog_comment);
        commentView.setText(comment);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateRatings();
        TextView comment = (TextView) view.findViewById(R.id.flight_comment_dialog_comment);
        this.comment = comment.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (!isInLayout()) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        view = inflater.inflate(R.layout.fragment_comment_dialog, container, false);
        initView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void initView(View view) {
        final ImageButton thumbsUpButton = (ImageButton) view.findViewById(R.id.flight_comments_dialog_thumbs_up);
        final ImageButton thumbsDownButton = (ImageButton) view.findViewById(R.id.flight_comments_dialog_thumbs_down);
        final CommentDialogFragment contextOuter = this;
        thumbsUpButton.setOnClickListener(new View.OnClickListener() {
            AtomicBoolean thumbs_up = contextOuter.thumbs_up;
            @Override
            public void onClick(View v) {
                if (!thumbs_up.get()) {
                    thumbs_up.set(true);
                    thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_green_24dp));
                    thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
                }
            }
        });
        thumbsDownButton.setOnClickListener(new View.OnClickListener() {
            AtomicBoolean thumbs_up = contextOuter.thumbs_up;
            @Override
            public void onClick(View v) {
                if (thumbs_up.get()) {
                    thumbs_up.set(false);
                    thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp));
                }
            }
        });
        final Button sendButton = (Button) view.findViewById(R.id.flight_comment_dialog_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {

            CommentDialogFragment context = contextOuter;
            @Override
            public void onClick(View v) {
                context.updateRatings();
                new PutFlightCommentTask().execute(
                        airline,
                        number,
                        String.valueOf((int) (context.ratingsArray[0] * 2)),
                        String.valueOf((int) (context.ratingsArray[1] * 2)),
                        String.valueOf((int) (context.ratingsArray[2] * 2)),
                        String.valueOf((int) (context.ratingsArray[3] * 2)),
                        String.valueOf((int) (context.ratingsArray[4] * 2)),
                        String.valueOf((int) (context.ratingsArray[5] * 2)),
                        String.valueOf(context.thumbs_up.get()),
                        context.getComment()
                );
                dismiss();
            }
        });
        if (thumbs_up.get()) {
            thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_green_24dp));
            thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));
        }
        else {
            thumbsUpButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
            thumbsDownButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private String getComment() {
        EditText editText = (EditText) view.findViewById(R.id.flight_comment_dialog_comment);
        return editText.getText().toString();
    }

    private void updateRatings() {
        RatingBar friendliness = (RatingBar) view.findViewById(R.id.flight_comment_dialog_friendliness);
        ratingsArray[0] = friendliness.getRating();
        RatingBar food = (RatingBar) view.findViewById(R.id.flight_comment_dialog_food);
        ratingsArray[1] = food.getRating();
        RatingBar punctuality = (RatingBar) view.findViewById(R.id.flight_comment_dialog_puntctuality);
        ratingsArray[2] = punctuality.getRating();
        RatingBar mileage_programe = (RatingBar) view.findViewById(R.id.flight_comment_dialog_mileage_programe);
        ratingsArray[3] = mileage_programe.getRating();
        RatingBar comfort = (RatingBar) view.findViewById(R.id.flight_comment_dialog_comfort);
        ratingsArray[4] = comfort.getRating();
        RatingBar quality_price = (RatingBar) view.findViewById(R.id.flight_comment_dialog_quality_price);
        ratingsArray[5] = quality_price.getRating();

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

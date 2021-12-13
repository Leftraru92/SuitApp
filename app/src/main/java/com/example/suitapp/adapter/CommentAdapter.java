package com.example.suitapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Comment;
import com.example.suitapp.model.Gender;
import com.example.suitapp.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> mValues;
    private OnCommentListener onCommentListener;

    public CommentAdapter(List<Comment> items, OnCommentListener onCommentListener) {
        mValues = items;
        this.onCommentListener = onCommentListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_article_questions, parent, false);

        return new ViewHolder(view, onCommentListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment mItem = mValues.get(position);
        boolean isAnswered = !mItem.getAnswer().equals("");
        if(isAnswered)
            holder.tvAnswer.setText(mItem.getAnswer() + "   " + Util.getDateFormatted(mItem.getDate(), false));
        holder.tvQuestion.setText(mItem.getQuestion());

        holder.btResponder.setVisibility((!isAnswered && mItem.isOwner()) ? View.VISIBLE : View.GONE);
        holder.btDelete.setVisibility((isAnswered && mItem.isOwner()) ? View.VISIBLE : View.GONE);
        holder.clRespuesta.setVisibility((isAnswered) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Comment> commentList) {
        mValues = commentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvQuestion, tvAnswer;
        public Comment mItem;
        public Button btDelete, btResponder, btSend, btCancel;
        public ConstraintLayout clRespuesta;
        OnCommentListener onCommentListener;
        LinearLayoutCompat llAnswer;
        TextInputLayout tilAnswer;
        TextInputEditText tietAnswer;

        public ViewHolder(View view, OnCommentListener onCommentListener) {
            super(view);
            mView = view;
            tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
            //tvUser = view.findViewById(R.id.tvUser);
            tvAnswer = view.findViewById(R.id.tvAnswer);
            btDelete = view.findViewById(R.id.btDelete);
            btResponder = view.findViewById(R.id.btResponder);
            btSend = view.findViewById(R.id.btSend);
            btCancel = view.findViewById(R.id.btCancel);
            clRespuesta = view.findViewById(R.id.clRespuesta);
            llAnswer = view.findViewById(R.id.llAnswer);
            tilAnswer = view.findViewById(R.id.tilAnswer);
            tietAnswer = view.findViewById(R.id.tietAnswer);
            this.onCommentListener = onCommentListener;

            btDelete.setOnClickListener(this);
            btResponder.setOnClickListener(this);
            btCancel.setOnClickListener(this);
            btSend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btDelete:
                    onCommentListener.onDeleteComment(mValues.get(getAdapterPosition()).getId());
                    clRespuesta.setVisibility(View.GONE);
                    break;
                case R.id.btResponder:
                    showForm(true);
                    break;
                case R.id.btSend:
                    if (validateAnswer()) {
                        onCommentListener.onResponseComment(mValues.get(getAdapterPosition()).getId(), tietAnswer.getText().toString());
                        tietAnswer.setText("");
                        showForm(false);
                        //btResponder.setEnabled(false);
                    }
                    break;
                case R.id.btCancel:
                    showForm(false);
                    break;
            }
        }

        private boolean validateAnswer() {
            boolean valid;
            if (tietAnswer.length() == 0) {
                tilAnswer.setError("Debe ingresar una respuesta");
                valid = false;
            } else if (tietAnswer.length() > tilAnswer.getCounterMaxLength()) {
                tilAnswer.setError("Su respuesta es demasiado larga");
                valid = false;
            } else {
                tilAnswer.setError(null);
                valid = true;
            }
            return valid;
        }

        private void showForm(boolean show) {
            llAnswer.setVisibility((show) ? View.VISIBLE : View.GONE);
            btResponder.setVisibility((show) ? View.GONE : View.VISIBLE);
        }
    }

    public interface OnCommentListener {
        void onDeleteComment(int position);

        void onResponseComment(int position, String answer);
    }
}
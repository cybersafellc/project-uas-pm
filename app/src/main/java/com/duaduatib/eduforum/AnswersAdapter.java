package com.duaduatib.eduforum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duaduatib.eduforum.model.Answer;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {
    private List<Answer> answersList;

    public AnswersAdapter(List<Answer> answersList) {
        this.answersList = answersList;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answersList.get(position);
        holder.tvAnswer.setText(answer.getAnswers());
        holder.tvUser.setText(answer.getUser().getFull_name());
    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnswer, tvUser;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }
}
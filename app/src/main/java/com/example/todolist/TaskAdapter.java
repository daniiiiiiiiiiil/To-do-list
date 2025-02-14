package com.example.todolist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_adapter, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskTitle.setText(task.getTitle());
        holder.textViewTaskDescription.setText(task.getDescription());


        holder.checkBoxIsCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxIsCompleted.setChecked(task.isCompleted());

        holder.checkBoxIsCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("CHECKBOX", "Task ID: " + task.getId() + ", isChecked: " + isChecked + ", position: " + position);
            To_do_list activity = (To_do_list) holder.itemView.getContext();
            activity.updateTask(task.getId(), isChecked, position);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTaskTitle;
        TextView textViewTaskDescription;
        CheckBox checkBoxIsCompleted;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            checkBoxIsCompleted = itemView.findViewById(R.id.checkBoxIsCompleted);
        }
    }
}

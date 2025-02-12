
package com.example.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class To_do_list extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextTaskName, editTextTaskDescription;
    private RecyclerView recyclerViewTasks;
    private FirebaseFirestore db;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        init();
        loadTasks();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);


        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setAdapter(taskAdapter);

    }


    public void onCreateTaskClicked(View view) {
        String taskName = editTextTaskName.getText().toString();
        String taskDescription = editTextTaskDescription.getText().toString();

        if (!taskName.isEmpty() && !taskDescription.isEmpty()) {
            String userId = mAuth.getCurrentUser().getUid();
            onClickCreateTask(userId, taskName, taskDescription);
            editTextTaskName.setText("");
            editTextTaskDescription.setText("");
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadTasks() {
        String userId = mAuth.getCurrentUser().getUid();
        getTasks(userId);
    }

    public void onClickCreateTask(String userId, String title, String description) {
        Map<String, Object> task = new HashMap<>();
        task.put("userId", userId);
        task.put("title", title);
        task.put("description", description);
        task.put("isCompleted", false);
        task.put("createdAt", new Date());

        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Задача создана!", Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при создании задачи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getTasks(String userId) {
        db.collection("tasks")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        taskList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String description = document.getString("description");
                            boolean isCompleted = document.getBoolean("isCompleted");
                            Date createdAt = document.getDate("createdAt");
                            String taskId = document.getId();
                            Task taskObject = new Task(taskId, userId, title, description, isCompleted, createdAt);
                            taskList.add(taskObject);
                        }
                        taskAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Ошибка при загрузке задач: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateTask(String taskId, boolean isCompleted) {
        db.collection("tasks")
                .document(taskId)
                .update("isCompleted", isCompleted)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Задача обновлена!", Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при обновлении задачи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void deleteTask(String taskId) {
        db.collection("tasks")
                .document(taskId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Задача удалена!", Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при удалении задачи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void updateTask(String taskId, boolean isCompleted, int position) {
        db.collection("tasks")
                .document(taskId)
                .update("isCompleted", isCompleted)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Задача обновлена!", Toast.LENGTH_SHORT).show();
                    // Обновляем isCompleted в списке
                    taskList.get(position).setCompleted(isCompleted);
                    // Уведомляем адаптер об изменении элемента
                    taskAdapter.notifyItemChanged(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при обновлении задачи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}

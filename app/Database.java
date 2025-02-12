import com.google.firebase.firestore.FirebaseFirestore;

public class Database extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        edLogin = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public void createTask(String userId, String title, String description) {
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
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String description = document.getString("description");
                            boolean isCompleted = document.getBoolean("isCompleted");
                            Date createdAt = document.getDate("createdAt");
                            Log.d("TASK", "Title: " + title + ", Description: " + description);
                        }
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
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при удалении задачи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
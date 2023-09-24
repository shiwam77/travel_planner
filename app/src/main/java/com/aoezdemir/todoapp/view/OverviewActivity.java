package com.aoezdemir.todoapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.crud.database.TodoDBHelper;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.components.TodoListSorter;

import java.util.List;


public class OverviewActivity extends AppCompatActivity {

    public final static int REQUEST_CREATE_NEW_TODO = 0;
    public final static int REQUEST_EDIT_TODO = 1;
    private static final String TAG = OverviewActivity.class.getSimpleName();
    private RecyclerView rvOverview;
    private OverviewAdapter ovAdapter;
    private List<Todo> todos;
    private TodoDBHelper db;
    private boolean sortDateBased = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvOverview = findViewById(R.id.rvOverview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOverview.setLayoutManager(linearLayoutManager);
        db = new TodoDBHelper(this);
        todos = db.getAllTodos();
        initializeUIElements();
    }

    @Override
    public void onResume() {
        todos = db.getAllTodos();
        updateAdapter();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.iSort) {
            sortDateBased = item.getItemId() == R.id.iSortDate;
            updateAdapter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_NEW_TODO && resultCode == RESULT_OK &&
                data != null && data.hasExtra(AddActivity.INTENT_KEY_TODO)) {
            todos.add((Todo) data.getSerializableExtra(AddActivity.INTENT_KEY_TODO));
            updateAdapter();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Updates the todo list. In theory this method should be invoked every time the todo list had
     * been manipulated.
     */
    private void updateAdapter() {
        todos = TodoListSorter.sort(todos, sortDateBased);
        if (ovAdapter != null) {
            ovAdapter.notifyDataSetChanged();
        }
    }



    private void initializeUIElements() {
        ovAdapter = new OverviewAdapter();
        todos = TodoListSorter.sort(todos, sortDateBased);
        rvOverview.setAdapter(ovAdapter);
        findViewById(R.id.fabAddTodo).setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
            startActivityForResult(intent, REQUEST_CREATE_NEW_TODO);
        });
    }

    class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {

        @NonNull
        @Override
        public OverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OverviewViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_todo, parent, false), this);
        }

        @Override
        public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position) {
            if (todos != null &&
                    !todos.isEmpty() &&
                    position < todos.size()) {

                // Load the actual todo into the card view
                holder.loadTodo(todos.get(position), position);

                // Add a OnClickListener on the card view itself
                holder.view.setOnClickListener((View v) -> {
                    Intent detailIntent = new Intent(v.getContext(), DetailviewActivity.class);
                    detailIntent.putExtra(DetailviewActivity.INTENT_KEY_TODO, todos.get(position));
                    detailIntent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
                    v.getContext().startActivity(detailIntent);
                });
            }
        }

        @Override
        public void onBindViewHolder(@NonNull OverviewViewHolder holder, int position, @NonNull List<Object> payloads) {
            if (todos != null && !todos.isEmpty() && position < todos.size()) {
                if (payloads.isEmpty()) {
                    onBindViewHolder(holder, position);
                } else {
                    for (Object payload : payloads) {
                        if (payload instanceof Todo) {
                            holder.loadTodo((Todo) payload, position);
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (todos != null) {
                return todos.size();
            }
            return 0;
        }

        class OverviewViewHolder extends RecyclerView.ViewHolder {

            private View view;
            private OverviewAdapter adapter;
            private ImageButton ibDone;
            private TextView tvTodoTitle;
            private TextView tvTodoDate;
            private ImageView ivDateIcon;
            private ImageButton ibEdit;
            private ImageButton ibFavoriteToggle;
            private ImageButton ibDelete;
            private TodoDBHelper db;

            OverviewViewHolder(View v, OverviewAdapter a) {
                super(v);
                view = v;
                adapter = a;
                ibDone = view.findViewById(R.id.ibDone);
                tvTodoTitle = view.findViewById(R.id.tvTodoTitle);
                tvTodoDate = view.findViewById(R.id.tvTodoDate);
                ivDateIcon = view.findViewById(R.id.ivDateIcon);
                ibEdit = view.findViewById(R.id.ibEdit);
                ibFavoriteToggle = view.findViewById(R.id.ibFavouriteToggle);
                ibDelete = view.findViewById(R.id.ibDelete);
                db = new TodoDBHelper(view.getContext());
            }

            private void initTodoTitle(Todo todo) {
                tvTodoTitle.setText(todo.getName());
                tvTodoTitle.setTextColor(view.getResources().getColor(todo.isDone() ? R.color.colorTodoTitleDone : R.color.colorTodoTitleDefault, null));
            }

            private void initTodoDate(Todo todo) {
                int textColor = todo.isDone() ? R.color.colorTodoDateDefault : todo.isExpired() ? R.color.colorTodoDateExpired : R.color.colorTodoDateDefault;
                tvTodoDate.setText(todo.formatExpiry());
                tvTodoDate.setTextColor(view.getResources().getColor(textColor, null));
                ivDateIcon.setImageDrawable(view.getResources().getDrawable(todo.isDone() ? R.drawable.ic_event_note_dark_gray_24dp : todo.isExpired() ? R.drawable.ic_event_note_red_24dp : R.drawable.ic_event_note_dark_gray_24dp, null));
            }

            private void initTodoDoneToggle(Todo todo) {
                ibDone.setImageResource(todo.isDone() ? R.drawable.ic_check_circle_green_24dp : todo.isExpired() ? R.drawable.ic_error_outline_red_24dp : R.drawable.ic_radio_button_not_done_green_24dp);
                ibDone.setOnClickListener((View v) -> {
                    todo.setDone(!todo.isDone());
                    boolean dbUpdateSucceeded = db.updateTodo(todo);
                    if (dbUpdateSucceeded) {
                        todos = TodoListSorter.sort(todos, sortDateBased);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(view.getContext(), "Local error: Todo status could not be changed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void initTodoFavouriteToggle(Todo todo) {
                ibFavoriteToggle.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
                ibFavoriteToggle.setImageResource(todo.isFavourite() ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_dark_gray_24dp);
                ibFavoriteToggle.setOnClickListener((View v) -> {
                    todo.setFavourite(!todo.isFavourite());
                    boolean dbUpdateSucceeded = db.updateTodo(todo);
                    if (dbUpdateSucceeded) {
                        todos = TodoListSorter.sort(todos, sortDateBased);
                        adapter.notifyDataSetChanged();
                        ibFavoriteToggle.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
                    } else {
                        Toast.makeText(view.getContext(), "Local error: Failed to change favourite state", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void initTodoEdit(Todo todo) {
                ibEdit.setVisibility(todo.isDone() ? View.INVISIBLE : View.VISIBLE);
                ibEdit.setOnClickListener((View v) -> {
                    Intent editIntent = new Intent(view.getContext(), EditActivity.class);
                    editIntent.putExtra(EditActivity.INTENT_KEY_TODO, todo);
                    editIntent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, false);
                    ((Activity) view.getContext()).startActivityForResult(editIntent, REQUEST_EDIT_TODO);
                });
            }

            private void initTodoDelete(Todo todo, int position) {
                ibDelete.setVisibility(todo.isDone() ? View.VISIBLE : View.INVISIBLE);
                ibDelete.setOnClickListener((View v) -> {
                    todos.remove(position);
                    boolean dbDeletionSucceeded = db.deleteTodo(todo.getId());
                    if (dbDeletionSucceeded) {
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(view.getContext(), "Local error: Failed to deleteAllTodos todo", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            void loadTodo(Todo todo, int position) {
                initTodoTitle(todo);
                initTodoDate(todo);
                initTodoDoneToggle(todo);
                initTodoFavouriteToggle(todo);
                initTodoEdit(todo);
                initTodoDelete(todo, position);
            }
        }
    }
}
/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

// Write your code here
package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.todo.model.TodoRowMapper;

@Service
public class TodoH2Service implements TodoRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Todo> getTodos() {
        return (ArrayList<Todo>) db.query("select * from todolist", new TodoRowMapper());
    }

    @Override
    public Todo getTodoById(int id) {
        try {
            return db.queryForObject("select * from todolist where id=?", new TodoRowMapper(), id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Todo addTodo(Todo toodo) {
        db.update("insert into todolist(todo,status,priority) values(?,?,?)", toodo.getTodo(), toodo.getStatus(),
                toodo.getPriority());
        return db.queryForObject("select * from todolist where todo=? and status=? and priority=?", new TodoRowMapper(),
                toodo.getTodo(), toodo.getStatus(), toodo.getPriority());
    }

    @Override
    public Todo updateTodo(int id, Todo toodo) {
        if (toodo.getTodo() != null) {
            db.update("update todolist set todo=? where id=?", toodo.getTodo(), id);
        }
        if (toodo.getStatus() != null) {
            db.update("update todolist set status=? where id=?", toodo.getStatus(), id);
        }
        if (toodo.getPriority() != null) {
            db.update("update todolist set priority=? where id=?", toodo.getPriority(), id);
        }

        return getTodoById(id);
    }

    @Override
    public void deleteTodo(int id) {
        db.update("delete from todolist where id=?", id);
    }
}
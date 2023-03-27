package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@RestController
public class TodosController {

    private static final String path = "/todos";
    int id = 1;
    JSONArray todos = new JSONArray();

    private JSONArray sort(JSONArray sortedTodo, int sequence) { 

        if (sortedTodo.size() == todos.size()) {
            return sortedTodo;
        }

        for (int i = 0; i < todos.size(); i++) {
            JSONObject todo = (JSONObject)todos.get(i);
            int idx = (int) todo.get("id");

            if (sequence == idx - 1 && sortedTodo.size() != todos.size()) {
                sortedTodo.add(todo);
                sequence = sequence + 1;
                return sort(sortedTodo, sequence);
            }
        }
        sequence = sequence + 1;
        System.out.println(sortedTodo);
        System.out.println(sequence);
        return sortedTodo;
    }

    @GetMapping(path)
    public JSONArray getTodos() {
        return todos;
    }

    @PostMapping(path)
    public JSONArray createTodo(@RequestBody JSONObject todo) {
        JSONObject newTodo = new JSONObject();
        String name = (String) todo.get("name");

        newTodo.put("id", id);
        newTodo.put("name", name);
        todos.add(newTodo);

        id = id + 1;

        return todos;
    }

    @PutMapping(path + "/{id}")
    public JSONArray editTodo(@RequestBody JSONObject todo, @PathVariable("id") int editId) {

        JSONObject editedTodo = new JSONObject();
        String name = (String) todo.get("name");

        editedTodo.put("id", editId);
        editedTodo.put("name", name);

        todos.remove(todos.get(editId - 1));

        todos.add(editedTodo);

        JSONArray sortedTodo = sort(new JSONArray(), 0);

        todos.clear();

         for (int i = 0; i < sortedTodo.size(); i++) {
            todos.add(sortedTodo.get(i));
        }

        return todos;
    }

    @DeleteMapping(path + "/{id}")
    JSONArray deleteTodo(@PathVariable("id") int deleteId) {
        todos.remove(todos.get(deleteId - 1));
        return todos;
    }
}
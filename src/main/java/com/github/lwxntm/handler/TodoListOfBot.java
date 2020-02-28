package com.github.lwxntm.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.lwxntm.util.TodoList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lei
 */
public class TodoListOfBot {
    private static File file = new File("todoList.json");
    private static ObjectMapper mapper = new ObjectMapper();
    private static List<TodoList> todoLists = null;

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void add(String msg) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            todoLists = mapper.readValue(file, new TypeReference<List<TodoList>>() {
            });
        } catch (IOException ignored) {
        }
        if (todoLists == null) {
            todoLists = new ArrayList<TodoList>();
        }
        todoLists.add(new TodoList(msg));
        mapper.writeValue(file, todoLists);
    }

    public static String list() {
        int i = 0;
        try {
            todoLists = mapper.readValue(file, new TypeReference<List<TodoList>>() {
            });
        } catch (IOException e) {
            return "没有待办事项!";
        }
        StringBuilder s = new StringBuilder();
        for (TodoList todoList : todoLists) {
            i++;
            s.append(i).append(":\n");
            s.append(todoList.getInfo()).append("\n");
        }
        return s.toString();
    }

    public static void del(String num) {
        try {
            todoLists = mapper.readValue(file, new TypeReference<List<TodoList>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = Integer.parseInt(num);
        todoLists.remove(i - 1);
        try {
            mapper.writeValue(file, todoLists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

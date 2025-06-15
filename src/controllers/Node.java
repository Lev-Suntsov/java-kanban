package controllers;

import model.Task;

public class Node {
    Task task;
    Node last;
    Node next;
    Node(Task task, Node last, Node next){
        this.task = task;
        this.last = last;
        this.next = next;
    }
}

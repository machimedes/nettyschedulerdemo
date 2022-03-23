package pers.machi.task;

import pers.machi.dag.Node;
import pers.machi.flow.Flow;
import pers.machi.flow.FlowDispatcher;
import pers.machi.message.TaskFinishMessage;

import java.util.concurrent.CompletableFuture;

public abstract class Task extends Node {
    Flow flow;

    public Task setFlow(Flow flow) {
        this.flow = flow;
        return this;
    }


    public abstract Task run();

    public void runAsync() {
        CompletableFuture<?> completableFuture = CompletableFuture.supplyAsync(
                this::run, FlowDispatcher.taskexecutorService)
                .thenAccept(t -> flow.inbox.offer(new TaskFinishMessage(t)));
    }

}

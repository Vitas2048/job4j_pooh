package ru.job4j.pooh;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> QUEUE = new ConcurrentHashMap<>();


    @Override
    public Resp process(Req req) {
        if (HttpMethods.GET.equals(req.httpRequestType())) {
            return processGet(req);
        } else if (HttpMethods.POST.equals(req.httpRequestType())) {
            return processPost(req);
        }
        return null;
    }

    private Resp processPost(Req req) {
        QUEUE.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
        QUEUE.get(req.getSourceName()).add(req.getParam());
        return new Resp(req.getSourceName(), HttpStatus.SUCCESS);
    }

    private Resp processGet(Req req) {
        return QUEUE.get(req.getSourceName()).peek() != null
                ? new Resp(Objects.requireNonNull(QUEUE.get(req.getSourceName()).poll()),
                HttpStatus.SUCCESS) : new Resp("", HttpStatus.DENIED);
    }
}
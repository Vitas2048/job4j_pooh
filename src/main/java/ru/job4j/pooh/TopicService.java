package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();
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
        topics.get(req.getSourceName()).forEach((k, v) -> v.add(req.getParam()));
        return topics.get(req.getSourceName()).isEmpty() ? new Resp("", HttpStatus.DENIED)
                : new Resp(req.getSourceName(), HttpStatus.SUCCESS);
    }

    private Resp processGet(Req req) {
        topics.putIfAbsent(req.getSourceName(), addNewMap(req));
        topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
        return topics.get(req.getSourceName()).get(req.getParam()) != null
                && topics.get(req.getSourceName()).get(req.getParam()).peek() != null
                ? new Resp(topics.get(req.getSourceName()).get(req.getParam()).poll(), HttpStatus.SUCCESS)
                : new Resp("", HttpStatus.DENIED);
    }

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> addNewMap(Req req) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> newMap = new ConcurrentHashMap<>();
        newMap.put(req.getParam(), new ConcurrentLinkedQueue<>());
        return newMap;
    }
}
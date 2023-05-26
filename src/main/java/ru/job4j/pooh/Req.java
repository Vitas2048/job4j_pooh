package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String param = "";
        String[] parsed = content.split(" ");
        String httpRequestType = parsed[0];
        String poohMode = parsed[1].split("/")[1];
        String sourceName = parsed[1].split("/")[2];
        if ("POST".equals(httpRequestType)) {
            param = parsed[parsed.length - 1].split("\n")[parsed[parsed.length - 1].split("\n").length - 1];
        }
        if ("GET".equals(httpRequestType)) {
            String[] parsed2 = parsed[1].split("/");
            if (parsed2.length > 3) {
                param = parsed2[3];
            }

        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
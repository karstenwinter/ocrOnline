package ocrOnline;

public class Result {

    private final String error;
    private final String content;

    public Result(String content, String error) {
        this.content = content;
        this.error = error;
    }

    public String getContent() {
        return content;
    }

    public String getError() {
        return error;
    }

}

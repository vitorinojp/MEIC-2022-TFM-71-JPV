package asaintsever.httpsinkconnector.event.formatter;

import asaintsever.httpsinkconnector.utils.Pair;

import java.util.List;

public class EventBatch {
    private byte[] batch;
    private List<Pair<String, String>> headers;

    public EventBatch(byte[] batch, List<Pair<String, String>> headers) {
        this.batch = batch;
        this.headers = headers;
    }

    public byte[] getBatch() {
        return batch;
    }

    public void setBatch(byte[] batch) {
        this.batch = batch;
    }

    public List<Pair<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Pair<String, String>> headers) {
        this.headers = headers;
    }

    public void addHeader(Pair<String, String> header){
        this.headers.add(header);
    }
}

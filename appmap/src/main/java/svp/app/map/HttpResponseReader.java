package svp.app.map;

import com.google.api.client.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpResponseReader {
    private final HttpResponse result;

    public HttpResponseReader(HttpResponse result) {
        this.result = result;
    }

    public String readToEnd() throws IOException {
        InputStreamReader str = new InputStreamReader(result.getContent());
        BufferedReader reader = new BufferedReader(str);
        String readLine;
        StringBuffer sb = new StringBuffer();
        while (((readLine = reader.readLine()) != null)) {
            sb.append("\n").append(readLine);
        }
        return sb.toString();
    }
}

package admin.util.http;

import okhttp3.*;

import java.util.function.Consumer;

public class HttpAdminUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    // support sending file information in http format
    public static void postRunASync(RequestBody body, String finalUrl, Callback callback){
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        //Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}

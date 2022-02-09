package downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

public abstract class Downloader {
    private static final Logger LOGGER = LogManager.getLogger(Downloader.class);
    private static final int RETRY_COUNT=10;
    protected abstract void download(String movieURL) throws IOException, InterruptedException, URISyntaxException;

    protected static HttpRequest buildRequest(String rawUrl) throws URISyntaxException, MalformedURLException {
        String url=prepareURL(rawUrl);
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
    }

    private static String prepareURL(String rawUrl) throws MalformedURLException, URISyntaxException {
        URL url= new URL(rawUrl);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(url.getHost()), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        String correctEncodedURL=uri.toASCIIString();
        return correctEncodedURL;
    }

    protected static String sendRequest(HttpRequest request) throws IOException, InterruptedException {
        long start =new Date().getTime();
        String res=HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString()).body();
        LOGGER.info("CALL "+request+"" +
                "\t["+(new Date().getTime()-start)+" ms]" +
                "\t["+res.length()+" bytes]");
        return res;
    }

    protected static byte[] sendRequestAsByteArray(HttpRequest request) throws IOException, InterruptedException {
        int tryIndex=1;
        long start=new Date().getTime();
        while(tryIndex<RETRY_COUNT){
            try{
                byte[] res=  HttpClient.newBuilder()
                        .build()
                        .send(request, responseInfo -> HttpResponse.BodySubscribers.ofByteArray()).body();
                LOGGER.info("[CALL\t"+request +"]" +
                        "\t["+res.length+" bytes]" +
                        "\t["+(new Date().getTime()-start)+" ms]"+
                        ((tryIndex>1)?"\t[try "+tryIndex+" times]":"")
                );
                return res;
            }
            catch (Exception e){
                LOGGER.warn("try "+ ++tryIndex);
            }}
        throw new RuntimeException("failed request");
    }


    protected static void downloadAndAppendToFile(String fileUrl, File file) {
        long fileSizeBefore=file.length();
        byte[] firstPartByteArray= new byte[0];
        try {
            firstPartByteArray = sendRequestAsByteArray(buildRequest(fileUrl));
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        try (FileOutputStream output = new FileOutputStream(file, true)) {
            output.write(firstPartByteArray);} catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("appendToFile "+file.getAbsolutePath()+"" +
                "\t[fileSizeBefore="+fileSizeBefore+"]" +
                "\t[fileSizeAfter="+file.length()+"]");
    }

}

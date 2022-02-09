package downloader;

import downloader.model.Site;
import downloader.mp3.FoReadDownloader;
import downloader.ts.UakinoDownloader;

import java.io.IOException;
import java.net.URISyntaxException;

import static downloader.model.FileType.MP3;
import static downloader.model.FileType.TS;

public class SiteFactory {


    private String url;

    public SiteFactory(String url) {
        this.url = url;
    }

    public Site get(String host){
        switch (host) {
            case "baskino.me": {
                Site downloader = new Site();
                downloader.setHost("baskino.me");
                downloader.setFileType(TS);
                return downloader;
            }
            case "uakino.club": {
                Site downloader = new Site();
                downloader.setHost("uakino.club");
                downloader.setFileType(TS);
                downloader.setDownloader(new UakinoDownloader());
                return downloader;
            }
            case "4read.org": {
                Site downloader = new Site();
                downloader.setHost("4read.org");
                downloader.setFileType(MP3);
                downloader.setDownloader(new FoReadDownloader());
                return downloader;
            }

        }
        throw new RuntimeException("downloader for host "+host+" not implemented");
    }

    public Site getByURL(String movieURL) {
        return get(movieURL.split("://")[1].split("/")[0]);
    }

    void download() throws IOException, URISyntaxException, InterruptedException {
        getByURL(url).getDownloader().download(url);
    }
}

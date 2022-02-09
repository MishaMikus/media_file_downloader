package downloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        LOGGER.info("Client start");
        //new SiteFactory("https://uakino.club/cartoon/6024-mi-monstri.html").download();
        new SiteFactory("https://4read.org/569-ren-rozdobudko-pereformulyuvannya.html").download();

    }
}

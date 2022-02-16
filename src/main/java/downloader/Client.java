package downloader;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URISyntaxException;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        LOGGER.info("Client start");

        new SiteFactory("https://uakino.club/cartoon/11263-trol-2-svtove-turne.html").download();

        //https://4read.org/2341-bredbery-rey-marsyansky-hronyki.html
    }
}

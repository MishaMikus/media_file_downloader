package downloader;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URISyntaxException;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        LOGGER.info("Client start");

        new SiteFactory("https://uakino.club/cartoon/1944-ruffalo.html").download();

    }
}

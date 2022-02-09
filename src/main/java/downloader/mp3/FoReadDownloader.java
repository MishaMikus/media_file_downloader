package downloader.mp3;

import downloader.Downloader;
import downloader.ts.UakinoDownloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringFormatter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FoReadDownloader extends Downloader {
    private static final Logger LOGGER = LogManager.getLogger(FoReadDownloader.class);
    @Override
    protected void download(String movieURL) throws IOException, InterruptedException, URISyntaxException {
        //first page download
        String bookPage=sendRequest(buildRequest(movieURL));

        //parse bookName from bookPage
        String bookName=parseBookName(bookPage);

        //parse fileM3U from bookPage
        String fileM3U=parseFileM3U(bookPage);

        //download m3uFile
        String m3uFile=sendRequest(buildRequest(fileM3U));

        //make fileList
        List<String> fileToBeDownloadList= Arrays.stream(m3uFile.split("\r")).map(r->r.trim()).collect(Collectors.toList());
        
        //make resultDir
        File resultDir=new File(bookName);
        if(!resultDir.exists()){resultDir.mkdir();}

        //downloadAllFiles
        downloadAllFiles(resultDir,fileToBeDownloadList);
    }

    private void downloadAllFiles(File resultDir, List<String> fileToBeDownloadList) {
        Date start=new Date();
        IntStream.range(0, fileToBeDownloadList.size()-1).forEach(i->{
            String[] urlPathArray=fileToBeDownloadList.get(i).split("/");
            String fileName=urlPathArray[urlPathArray.length-1].replaceAll(" ","_");
            File file=new File(resultDir.getAbsolutePath()+File.separator+fileName);
            if(file.exists()) {file.delete();}
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.warn("can't create "+file.getAbsolutePath());
                e.printStackTrace();
            }
            downloadAndAppendToFile(fileToBeDownloadList.get(i),file);
            LOGGER.info("save "+file.length()+" bytes to "+file.getAbsolutePath());
            LOGGER.info(StringFormatter.progressLog(start,i,fileToBeDownloadList.size()));

        });
    }

    private String parseFileM3U(String bookPage) {
        String res="https://4read.org/m3u/"+bookPage.split("file:\"\\{v1}")[1].split("\"")[0];
        LOGGER.info("PARSE "+res);
        return res;
    }

    private String parseBookName(String bookPage) {
        String res=bookPage.split("\"og:title\" content=\"")[1].split("\"")[0].replaceAll(" ","_");
        LOGGER.info("PARSE "+res);
        return res;

    }
}

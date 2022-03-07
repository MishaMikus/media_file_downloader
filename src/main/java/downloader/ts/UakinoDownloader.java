package downloader.ts;

import downloader.Downloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringFormatter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class UakinoDownloader extends Downloader {

    //Example
    //https://uakino.club/cartoon/features/116-ratatuy.html

    private static final Logger LOGGER = LogManager.getLogger(UakinoDownloader.class);
@Override
     public void download(String movieURL) throws IOException, InterruptedException, URISyntaxException {
        //first page download
        String moviePage=sendRequest(buildRequest(movieURL));

        //parse movieName from moviePage
        String movieName=parseMovieName(moviePage);

        //parse movieSourceURL from moviePage
        String movieSourceURL=parseMovieSourceURL(moviePage);

        //download movieSourcePage
        String movieSourcePage=sendRequest(buildRequest(movieSourceURL));

        //parse movieFile from movieSourcePage
        String movieFile=parseMovieFile(movieSourcePage);

        //download ResolutionFile index
        String resolutionFileIndex=sendRequest(buildRequest(movieFile));

        //parse bestResolutionFile index from resolutionFileIndex
        String bestResolutionFileIndexFileName=parseBestResolutionFileIndex(resolutionFileIndex);

        //parse bestResolutionFile index from resolutionFileIndex
        String bestResolutionFileIndexFullFileName=buildBestResolutionFileIndexFileName(movieFile,bestResolutionFileIndexFileName);

        //download bestResolutionFileIndex
        String bestResolutionFileIndex=sendRequest(buildRequest(bestResolutionFileIndexFullFileName));

        //make segmentFileList
        List<String> segmentFileList=makeSegmentFileList(bestResolutionFileIndexFullFileName,bestResolutionFileIndex);

        //make emptyFile
        File file = new File(movieName+".ts");
        if(file.exists()){file.delete();}
        LOGGER.info(file.getAbsolutePath() + " "+(file.createNewFile()?"created":"can't create"));

        //download first part
        downloadAndSave(segmentFileList,file);
    }

    private static void downloadAndSave(List<String> segmentFileList, File file){
        Date start=new Date();
        IntStream.range(0, segmentFileList.size()-1).forEach(i->{
            downloadAndAppendToFile(segmentFileList.get(i),file);
            LOGGER.info(StringFormatter.progressLog(start,i,segmentFileList.size()));
        });

    }
    
    private static List<String> makeSegmentFileList(String movieFile, String bestResolutionFileIndex) {
        List<String> res=new ArrayList<>();
        String prefix=movieFile.split("index")[0];
        AtomicBoolean itIsFirstRow= new AtomicBoolean(true);
        Arrays.stream(bestResolutionFileIndex.split(",")).forEach(r->{
                    if(!itIsFirstRow.get()){
                        r=prefix+r.split("ts")[0].trim()+"ts";
                        res.add(r);  }
                    else {
                        itIsFirstRow.set(false);}
                }
        );
        LOGGER.info("PARSE parseMovieName "+res.size()+" rows");
        return res;
    }

    private static String buildBestResolutionFileIndexFileName(String movieFile, String bestResolutionFileIndex) {
        String res=movieFile.split("/hls/")[0]+"/hls/"+bestResolutionFileIndex;
        LOGGER.info("PARSE buildBestResolutionFileIndexFileName "+res);
        return res;
    }

    private static String parseBestResolutionFileIndex(String movieSourcePage) {
        //LOGGER.info(movieSourcePage);
        String res=movieSourcePage.split("\\./")[1].split("#")[0].trim();
        LOGGER.info("PARSE parseBestResolutionFileIndex "+res);
        return res;
    }

    private static String parseMovieFile(String movieSourcePage) {
        if(movieSourcePage.contains("Змініть країну перегляду")) {
            LOGGER.warn("please use Ukrainian VPN");
        }
        String res="";
        LOGGER.info("TRY PARSE parseMovieFile");
        try{
         res=movieSourcePage.split("file:\"")[1].split("\"")[0];
        LOGGER.info("PARSE parseMovieFile "+res);
        }catch (Exception e){
            System.err.println("can't parse "+movieSourcePage);
            e.printStackTrace();
        }
        return res;
    }

    private static String parseMovieSourceURL(String moviePage) {
        String res=moviePage.split("iframe id=\"pre\"")[1].split("src=\"")[1].split("\"")[0];
        LOGGER.info("PARSE parseMovieSourceURL "+res);
        return res;
    }

    private static String parseMovieName(String moviePage) {
        String res=moviePage.split("origintitle")[1].split("<i>")[1].split("</i>")[0]
                .replaceAll(" ","_");
        LOGGER.info("PARSE parseMovieName "+res);
        return res;
    }

}

package downloader.ts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldShellTSDownloader {

    public static void main(String[] args) throws IOException {
        nerklq();
    }

    //baskino
    private static void baskino() throws IOException {
        //http://185.38.12.43/sec/1559842853/32373433003864190c76beccc631ea5fa97dc9c1bcc95269/ivs/7d/d7/624da4c20c9e/hls/tracks-4,5/segment1146.ts
        List<String> commandList = new ArrayList<>();
        commandList.add(curl("http://185.38.12.43/sec/1559842853/32373433003864190c76beccc631ea5fa97dc9c1bcc95269/ivs/7d/d7/624da4c20c9e/hls/tracks-4,5/segment",
                "1", "1146"));
        commandList.add(copyNameRange(1, 1146, 1, 0));
        commandList.add(copyNameRangeAll(1, 1));
        commandList.add(mpeg("Black_Mirror_Bandersnatch"));
        commandList.add(del());
        commandList.forEach(System.out::println);
        org.apache.commons.io.FileUtils.writeLines(new File("downloader.bat"), "UTF-8", commandList);
        execute();
    }

    //kinokrad
    private static void kinokrad() throws IOException {
        List<String> commandList = new ArrayList<>();
        commandList.add(curl("https://hls.kinokrad.co/hls/BlacKkKlansman.2018.BDRip/part", "0000", "2028"));
        commandList.add(copyNameRange(0, 1020, 1, 4));
        commandList.add(copyNameRange(1020, 2000, 2, 4));
        commandList.add(copyNameRange(2000, 2029, 3, 4));
        commandList.add(copyNameRangeAll(1, 3));
        commandList.add(mpeg("BlacKkKlansman"));
        commandList.add(del());
        commandList.forEach(System.out::println);
        org.apache.commons.io.FileUtils.writeLines(new File("downloader.bat"), "UTF-8", commandList);
        execute();
    }

    //https://nerklq.cyou/content/stream/new4/cloudy_with_a_chance_of_meatballs_2_2013_bdrip_1080p_ukr_eng_hurtom_7101/hls/1080/segment1115.ts
    //nerklq
    private static void nerklq() throws IOException {
        List<String> commandList = new ArrayList<>();
        //download all ts
        commandList.add(curl("https://nerklq.cyou/content/stream/new4/" +
                "cloudy_with_a_chance_of_meatballs_2_2013_bdrip_1080p_ukr_eng_hurtom_7101/hls/1080/segment", "1", "22"));

        //marge it all
        commandList.add(copyNameRange(1, 22, 1, 0));

        //decode
        commandList.add(mpeg("cloudy_with_a_chance_of_meatballs_2"));

        //del
        commandList.add(del());

        //marge all commands
        commandList.forEach(System.out::println);
        //FileUtils.writeLines(new File("downloader.bat"), "UTF-8", commandList);

        //execute
        //execute();
    }
    private static void execute() throws IOException {
        Runtime.getRuntime().exec("cmd /c start \"\" downloader.bat");
    }

    private static String del() {
        return "del *.ts\ndel downloader.bat";
    }

    private static String mpeg(String fileName) {
        return "ffmpeg -i all.ts -acodec copy -vcodec copy "+fileName+".mp4";
    }

    private static String copyNameRangeAll(int a, int b) {
        StringBuilder text = new StringBuilder("all");
        for (int i = a; i <= b; i++) {
            text.append(i).append(".ts+all");
        }
        return "copy /b " + text.substring(0, text.length() - 4) + " all.ts";
    }

    private static String copyNameRange(int a, int b, int index, int indexLength) {
        StringBuilder text = new StringBuilder();
        for (int i = a; i < b; i++) {
            if (indexLength > 0) {
                text.append(String.format("%0" + indexLength + "d", i)).append(".ts+");
            } else {
                text.append(i).append(".ts+");
            }
        }
        return "copy /b " + text.substring(0, text.length() - 1) + " all" + index + ".ts";
    }

    private static String curl(String prefix, String a, String b) {
        return "curl \"" + prefix + "[" + a + "-" + b + "].ts\" -o \"s####1.ts\"";
    }
}

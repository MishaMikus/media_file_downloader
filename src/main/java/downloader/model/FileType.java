package downloader.model;

public enum FileType {
    TS("ts"),
    MP3("mp3");

    private String extension;

    FileType(String extension) {
        this.extension=extension;
    }
}

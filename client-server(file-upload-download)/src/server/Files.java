package server;

public class Files {
        private int fileNumber;
        private String filenames;
        private byte[] fileData;
        private String fileExtension;

    public Files(int fileNumber, String filenames, byte[] fileData, String fileExtension) {
        this.fileNumber = fileNumber;
        this.filenames = filenames;
        this.fileData = fileData;
        this.fileExtension = fileExtension;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFilenames() {
        return filenames;
    }

    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}

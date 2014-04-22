public class DirectoryBuilder {

    public static String getDirectory(int bookNumber) {
        //Pull from Database
        Integer fileNumber = bookNumber;
        String fileS = fileNumber.toString();
        String[] subD = fileS.split("");

        String fileD = "";
        for (int i = 1; i < subD.length - 1; i++) {
            fileD += "/" + subD[i];
        }
        fileD = fileD + "/" + fileS + "/" + fileS + ".txt";

        return fileD;
    }
}

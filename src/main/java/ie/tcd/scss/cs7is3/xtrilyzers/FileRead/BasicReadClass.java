package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;


import java.io.*;

public class BasicReadClass {
    /**
     * @param file filePath
     * @return
     */
    public String readFileContent(File file) {


        StringBuilder sb = new StringBuilder();
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            while ((strLine = br.readLine()) != null) {

                sb.append(strLine);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String fileContent = "";

//        try {
//
//            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
//
//            try {
//                while (it.hasNext()) {
//                    String line = it.nextLine();
//                    sb.append(line);
////                    fileContent = fileContent.concat(line);
//                    // do something with line
//                }
//            } finally {
//                it.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return sb.toString();
    }

    /**
     * @param file filePath
     * @return
     */
    public String readFileContentFR(File file) {


        StringBuilder sb = new StringBuilder();

        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line;
            while ((line = br.readLine()) != null) {
                while (true) {
                    if (line.contains("-- PJG")) {
                        line = line.replace(line.substring(line.indexOf("<!-- PJG"), line.indexOf(">") + 1), "");
                    } else {
                        break;
                    }
                }
                if (line.contains("/&blank;")) {
                    line = line.replace("/&blank;", " ");
                }
                if (line.contains("&blank;")) {
                    line = line.replace("&blank;", " ");
                }

                if (line.contains("&hyph;")) {
                    line = line.replace("&hyph;", "-");
                }


//                    String input = "Test Match Sunday, January 15, 2012 at 7:37pm EST";
//                    String regex = "(\\w+,\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s+at\\s+\\d+:\\d+(pm|am)\\s+\\w{3,4})";

                sb.append(line);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//
//            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
//
//            try {
//                while (it.hasNext()) {
//                    String line = it.nextLine();
//                    while (true) {
//                        if (line.contains("-- PJG")) {
//                            line = line.replace(line.substring(line.indexOf("<!-- PJG"), line.indexOf(">") + 1), "");
//                        } else {
//                            break;
//                        }
//                    }
//                    if (line.contains("/&blank;")) {
//                        line = line.replace("/&blank;", " ");
//                    }
//                    if (line.contains("&blank;")) {
//                        line = line.replace("&blank;", " ");
//                    }
//
//                    if (line.contains("&hyph;")) {
//                        line = line.replace("&hyph;", "-");
//                    }
//
//
////                    String input = "Test Match Sunday, January 15, 2012 at 7:37pm EST";
////                    String regex = "(\\w+,\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s+at\\s+\\d+:\\d+(pm|am)\\s+\\w{3,4})";
//
//                    sb.append(line);
//                }
//            } finally {
//                it.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return sb.toString();
    }
}

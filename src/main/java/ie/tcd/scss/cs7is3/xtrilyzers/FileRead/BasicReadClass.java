package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicReadClass {
    /**
     * @param file filePath
     * @return
     */
    public String readFileContent(File file) {


        String fileContent = "";

        try {

            LineIterator it = FileUtils.lineIterator(file, "UTF-8");

            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    fileContent = fileContent.concat(line);
                    // do something with line
                }
            } finally {
                it.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    /**
     * @param file filePath
     * @return
     */
    public String readFileContentFR(File file) {


        String fileContent = "";

        try {

            LineIterator it = FileUtils.lineIterator(file, "UTF-8");

            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    while(true) {
                        if (line.contains("-- PJG")) {
                            line = line.replace(line.substring(line.indexOf("<!-- PJG"), line.indexOf(">") + 1), "");
                        }else {
                            break;
                        }
                    }
                    if(line.contains("/&blank;")){
                        line=line.replace("/&blank;"," ");
                    }
                    if(line.contains("&blank;")){
                        line=line.replace("&blank;"," ");
                    }

                    if(line.contains("&hyph;")){
                        line=line.replace("&hyph;","-");
                    }


//                    String input = "Test Match Sunday, January 15, 2012 at 7:37pm EST";
//                    String regex = "(\\w+,\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s+at\\s+\\d+:\\d+(pm|am)\\s+\\w{3,4})";


                    //date


//                    String regex = "(\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s)";
//                    Matcher matcher = Pattern.compile(regex).matcher(line);
//                    if (matcher.find()) {
//                        String date = matcher.group(1);
//                        date = date.trim();
//                        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
//                        try{
//
////                            System.out.println(date);
//                            df.parse(date);
////                            System.out.println(date);
//                            fileContent = fileContent+"<DATE1>"+date+"</DATE1>";
//                        }catch (ParseException e){
//
//
////                            System.out.println("Error : "+date);
//                            fileContent = fileContent+line;
//                        }
////                        System.out.println(fileContent);
//                        continue;
//                    }
                    fileContent = fileContent.concat(line);
                    // do something with line
                }
            } finally {
                it.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileContent;
    }
}

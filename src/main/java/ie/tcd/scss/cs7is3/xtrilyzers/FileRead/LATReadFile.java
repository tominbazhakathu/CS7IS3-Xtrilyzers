package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LATReadFile {

    private ArrayList<ContentBean> fullContent = new ArrayList<>();

    public void iterateFiles() {


        String docPath = "corpus/latimes";
        File doc = new File(docPath);
        System.out.println("Reading Los Angeles Times...");
        if (doc.exists() && doc.isDirectory()) {
            File[] files = doc.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && !files[i].getName().contains("read")) {
//                    System.out.println("Reading File " + files[i].getName() + " in LATimes...");
                    BasicReadClass read = new BasicReadClass();
                    String content = read.readFileContent(files[i]);
//                    System.out.println(content);

                    ArrayList<ContentBean> contentBean = parseFBIS(content);
                    fullContent.addAll(contentBean);

//                    Gson gson =  new GsonBuilder().setPrettyPrinting().create();
//                    System.out.println(gson.toJson(contentBean));

//                    break;

                }
            }
        }


        System.out.println("Total Number of Documents "+fullContent.size());

    }

    private ArrayList<ContentBean> parseFBIS(String content) {

        content = content.replace("<P>", "");
        content = content.replace("</P>", "");
        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            try {

                String doc = docs[i];
//            System.out.println(doc);

                String newsPaper = "The Los Angeles Times - LATimes";

                if (!doc.contains("<DOCNO>")) {
                    continue;
                }

                String docNumber = doc.substring(doc.indexOf("<DOCNO>"), doc.indexOf("</DOCNO>")).replace("<DOCNO>", "").trim();
                String date = doc.substring(doc.indexOf("<DATE>"), doc.indexOf("</DATE>")).replace("<DATE>", "").trim();
                if (!doc.contains("<HEADLINE>")) {
                    continue;
                }
                String title = doc.substring(doc.indexOf("<HEADLINE>"), doc.indexOf("</HEADLINE>")).replace("<HEADLINE>", "").trim();
                if (!doc.contains("<TEXT>")) {
                    continue;
                }
                String textContent = doc.substring(doc.indexOf("<TEXT>"), doc.indexOf("</TEXT>")).replace("<TEXT>", "").trim();
//            System.out.println(title);

                String date1 = "";
                String regex = "(\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s,)";
                Matcher matcher = Pattern.compile(regex).matcher(doc);
                if (matcher.find()) {
                    date1 = matcher.group(1);
                    date1 = date1.trim();
                    DateFormat df = new SimpleDateFormat("MMMM dd, yyyy,");
                    DateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                    try {

//                    System.out.println(date);
                        date = sdf.format(df.parse(date1));
                    } catch (ParseException e) {


//                    System.out.println("Error : " + date);
                    }
                }

                ContentBean contentBean = new ContentBean(newsPaper, title, textContent, date, docNumber);
                arrayList.add(contentBean);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
        return arrayList;


    }

    public ArrayList<ContentBean> getResult() {
        iterateFiles();
        return fullContent;
    }

    public void setArrayDefault() {
        fullContent = new ArrayList<>();
    }

}

package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FBISReadFile {


    private ArrayList<ContentBean> fullContent = new ArrayList<>();

    public void iterateFiles() {


        String docPath = "corpus/fbis";
        File doc = new File(docPath);
        System.out.println("Reading Foreign Broadcast Information Service...");
        if (doc.exists() && doc.isDirectory()) {
            File[] files = doc.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && !files[i].getName().startsWith("read")) {
//                    System.out.println("Reading File " + files[i].getName() + " in FBIS...");
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

        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            try {

                String doc = docs[i];

                String newsPaper = "Foreign Broadcast Information Service - FBIS";

                if (!doc.contains("<DOCNO>")) {
                    continue;
                }

                String docNumber = doc.substring(doc.indexOf("<DOCNO>"), doc.indexOf("</DOCNO>")).replace("<DOCNO>", "").trim();
                String date = doc.substring(doc.indexOf("<DATE1>"), doc.indexOf("</DATE1>")).replace("<DATE1>", "").trim();
                String title = doc.substring(doc.indexOf("<TI>"), doc.indexOf("</TI>")).replace("<TI>", "").trim();
                String textContent = doc.substring(doc.indexOf("<TEXT>"), doc.indexOf("</TEXT>")).replace("<TEXT>", "").trim();
//            System.out.println(title);

                DateFormat df = new SimpleDateFormat("MMMM dd yyyy");
                DateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

                try {
                    date = sdf.format(df.parse(date));
                } catch (ParseException e) {
                    DateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
                    try {
                        date = sdf.format(df1.parse(date));
                    } catch (ParseException e1) {
                        DateFormat df2 = new SimpleDateFormat("dd MMMM");
                        try {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(df2.parse(date));
                            cal.set(Calendar.YEAR, 1994);
                            date = sdf.format(cal.getTime());
                        } catch (ParseException e2) {
//                            e2.printStackTrace();
                        }
                    }
//                e.printStackTrace();
                }

                if (!textContent.contains("[Text]")) {

                    if (textContent.contains("[Passage omitted]")) {
                        textContent = textContent.substring(textContent.indexOf("[Passage omitted]")).replace("[Passage omitted]", "").trim();
                    } else if (textContent.contains("[Excerpts]")) {
                        textContent = textContent.substring(textContent.indexOf("[Excerpts]")).replace("[Excerpts]", "").trim();
                    } else {
//                    System.out.println(textContent);
//                    break;
                    }
                } else {
                    textContent = textContent.substring(textContent.indexOf("[Text]")).replace("[Text]", "").trim();
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

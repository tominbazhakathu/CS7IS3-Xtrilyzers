package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FTReadFile {


    private ArrayList<ContentBean> fullContent = new ArrayList<>();


    public void iterateFiles() {


        String docPath = "corpus/ft";
        File doc = new File(docPath);
        System.out.println("Reading Financial Times...");
        if (doc.exists() && doc.isDirectory()) {
            File[] subDocs = doc.listFiles();
            for (int i = 0; i < subDocs.length; i++) {
                if (subDocs[i].isDirectory() && !subDocs[i].getName().startsWith("read")) {
//                    System.out.println("Reading Folder " + i + " in ft...");
                    File[] files = subDocs[i].listFiles();
                    for (int j = 0; j < files.length; j++) {

//                        System.out.println("Reading File " + files[j].getName() + " in ft folder " + i);
                        if (files[j].isFile() && !files[j].getName().startsWith("read")) {
                            BasicReadClass read = new BasicReadClass();
                            String content = read.readFileContent(files[j]);
//                            System.out.println(content);

                            ArrayList<ContentBean> contentBean = parseFT(content);
                            fullContent.addAll(contentBean);

//                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                            System.out.println(gson.toJson(contentBean));

//                            break;

                        }
                    }
                }

//                break;

            }
        }


        System.out.println("Total Number of Documents "+fullContent.size());


    }

    private ArrayList<ContentBean> parseFT(String content) {

        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            try {

                String doc = docs[i];

                String newsPaper = "The Financial Times - FT";

                if (!doc.contains("<DOCNO>")) {
                    continue;
                }

                String docNumber = doc.substring(doc.indexOf("<DOCNO>"), doc.indexOf("</DOCNO>")).replace("<DOCNO>", "").trim();
                String date = doc.substring(doc.indexOf("<DATE>"), doc.indexOf("</DATE>")).replace("<DATE>", "").trim();
                String title = "";
                if (doc.contains("<HEADLINE>")) {
                    title = doc.substring(doc.indexOf("<HEADLINE>"), doc.indexOf("</HEADLINE>")).replace("<HEADLINE>", "").trim();
                    title = title.substring(title.indexOf("/") + 1).trim();
                } else {
                    title = "The Financial Times - " + date;
                }

                String textContent = "";

                if(!doc.contains("<TEXT>")){
                    continue;
                }

                textContent = doc.substring(doc.indexOf("<TEXT>"), doc.indexOf("</TEXT>")).replace("<TEXT>", "").trim();
//            System.out.println(title);

                DateFormat df = new SimpleDateFormat("yyMMdd");
                DateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

                try {
                    date = sdf.format(df.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
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

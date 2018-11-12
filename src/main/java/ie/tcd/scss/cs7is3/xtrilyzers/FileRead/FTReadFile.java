package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.io.File;
import java.util.ArrayList;

public class FTReadFile {


    private  ArrayList<ContentBean> fullContent = new ArrayList<>();


    public void iterateFiles() {


        String docPath = "data/files/ft";
        File doc = new File(docPath);
        if (doc.exists() && doc.isDirectory()) {
            File[] subDocs = doc.listFiles();
            for (int i = 0; i < subDocs.length; i++) {
                if (subDocs[i].isDirectory() && !subDocs[i].getName().startsWith("read")) {
                    System.out.println("Reading Folder " + i + " in ft...");
                    File[] files = subDocs[i].listFiles();
                    for (int j = 0; j < files.length; j++) {

                        System.out.println("Reading File " + j + " in ft folder "+ i);
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


    }

    private ArrayList<ContentBean> parseFT(String content) {

        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            String doc = docs[i];

            String newsPaper = "The Financial Times - FT";

            String docNumber = doc.substring(doc.indexOf("<DOCNO>"), doc.indexOf("</DOCNO>")).replace("<DOCNO>", "").trim();
            String date = doc.substring(doc.indexOf("<DATE>"), doc.indexOf("</DATE>")).replace("<DATE>", "").trim();
            String title = doc.substring(doc.indexOf("<HEADLINE>"), doc.indexOf("</HEADLINE>")).replace("<HEADLINE>", "").trim();
            title = title.substring(title.indexOf("/")+1).trim();
            String textContent = doc.substring(doc.indexOf("<TEXT>"), doc.indexOf("</TEXT>")).replace("<TEXT>", "").trim();
//            System.out.println(title);

            ContentBean contentBean = new ContentBean(newsPaper, title, textContent, date, docNumber);
            arrayList.add(contentBean);

        }
        return arrayList;


    }

    public ArrayList<ContentBean> getResult() {
        iterateFiles();
        return fullContent;
    }


    public static void main(String[] args) {

        FTReadFile ftReadFile = new FTReadFile();
        ftReadFile.iterateFiles();

    }
}

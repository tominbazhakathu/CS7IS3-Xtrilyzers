package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.io.File;
import java.util.ArrayList;

public class LATReadFile {

    private  ArrayList<ContentBean> fullContent = new ArrayList<>();

    public void iterateFiles(){


        String docPath = "data/files/latimes";
        File doc =  new File(docPath);
        if(doc.exists() && doc.isDirectory()){
            File[] files = doc.listFiles();
            for (int i = 0; i < files.length; i++) {
                if(files[i].isFile() && !files[i].getName().startsWith("read")){
                    System.out.println("Reading File "+i+" in LATimes...");
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


    }

    private ArrayList<ContentBean> parseFBIS(String content) {

        content =  content .replace("<P>","");
        content =  content .replace("</P>","");
        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            String doc = docs[i];
//            System.out.println(doc);

            String newsPaper = "The Los Angeles Times - LATimes";

            String docNumber = doc.substring(doc.indexOf("<DOCNO>"),doc.indexOf("</DOCNO>")).replace("<DOCNO>","").trim();
            String date = doc.substring(doc.indexOf("<DATE>"),doc.indexOf("</DATE>")).replace("<DATE>","").trim();
            if(!doc.contains("<HEADLINE>")){
                continue;
            }
            String title = doc.substring(doc.indexOf("<HEADLINE>"),doc.indexOf("</HEADLINE>")).replace("<HEADLINE>","").trim();
            if(!doc.contains("<TEXT>")){
                continue;
            }
            String textContent = doc.substring(doc.indexOf("<TEXT>"),doc.indexOf("</TEXT>")).replace("<TEXT>","").trim();
//            System.out.println(title);

            ContentBean contentBean = new ContentBean(newsPaper,title,textContent,date,docNumber);
            arrayList.add(contentBean);

        }
        return arrayList;


    }

    public ArrayList<ContentBean> getResult() {
        iterateFiles();
        return fullContent;
    }


    public static void main(String[] args) {

        LATReadFile latReadFile = new LATReadFile();
        latReadFile.iterateFiles();

    }
}

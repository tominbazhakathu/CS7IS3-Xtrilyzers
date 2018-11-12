package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.ArrayList;

public class FBISReadFile {


    private  ArrayList<ContentBean> fullContent = new ArrayList<>();

    public void iterateFiles(){


        String docPath = "data/files/fbis";
        File doc =  new File(docPath);
        if(doc.exists() && doc.isDirectory()){
            File[] files = doc.listFiles();
            for (int i = 0; i < files.length; i++) {
                if(files[i].isFile() && !files[i].getName().startsWith("read")){
                    System.out.println("Reading File "+i+" in FBIS...");
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

        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            String doc = docs[i];

            String newsPaper = "Foreign Broadcast Information Service - FBIS";

            String docNumber = doc.substring(doc.indexOf("<DOCNO>"),doc.indexOf("</DOCNO>")).replace("<DOCNO>","").trim();
            String date = doc.substring(doc.indexOf("<DATE1>"),doc.indexOf("</DATE1>")).replace("<DATE1>","").trim();
            String title = doc.substring(doc.indexOf("<TI>"),doc.indexOf("</TI>")).replace("<TI>","").trim();
            String textContent = doc.substring(doc.indexOf("<TEXT>"),doc.indexOf("</TEXT>")).replace("<TEXT>","").trim();
//            System.out.println(title);

            if(!textContent.contains("[Text]")){

                if(textContent.contains("[Passage omitted]")){
                    textContent = textContent.substring(textContent.indexOf("[Passage omitted]")).replace("[Passage omitted]", "").trim();
                }else if(textContent.contains("[Excerpts]")){
                    textContent = textContent.substring(textContent.indexOf("[Excerpts]")).replace("[Excerpts]", "").trim();
                }else {
//                    System.out.println(textContent);
//                    break;
                }
            }else {
                textContent = textContent.substring(textContent.indexOf("[Text]")).replace("[Text]","").trim();
            }
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

        FBISReadFile fbisReadFile = new FBISReadFile();
        fbisReadFile.iterateFiles();

    }
}

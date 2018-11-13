package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gate.sgml.Sgml2Xml;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;
import jdk.internal.org.xml.sax.InputSource;


import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FR94ReadFile {


    private ArrayList<ContentBean> fullContent = new ArrayList<>();

    public void iterateFiles() {


        String docPath = "data/files/fr94";
        File doc = new File(docPath);
        if (doc.exists() && doc.isDirectory()) {
            File[] subDocs = doc.listFiles();
            for (int i = 0; i < subDocs.length; i++) {
                if (subDocs[i].isDirectory() && !subDocs[i].getName().startsWith("read")) {
                    System.out.println("Reading Folder " + i + " in FR94...");
                    File[] files = subDocs[i].listFiles();
                    for (int j = 0; j < files.length; j++) {

                        System.out.println("Reading File " + files[j].getName() + " in FR94 folder " + i);
                        if (files[j].isFile() && !files[j].getName().startsWith("read")) {
                            BasicReadClass read = new BasicReadClass();
                            String content = read.readFileContentFR(files[j]);
//                            System.out.println(content);

                            ArrayList<ContentBean> contentBean = parseFBIS(content);

                            fullContent.addAll(contentBean);

//                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                            System.out.println(gson.toJson(contentBean));
//
//                            break;
                        }
                    }
                }
//
//                break;

            }
        }

    }

    public ArrayList<ContentBean> getResult() {
        iterateFiles();
        return fullContent;
    }

    private ArrayList<ContentBean> parseFBIS(String content) {


        String[] replace = {"<USDEPT>", "<AGENCY>", "<USBUREAU>", "<DOCTITLE>", "<ADDRESS>", "<FURTHER>",
                "<SUMMARY>", "<ACTION>", "<SIGNER>", "<SIGNJOB>", "<SUPPLEM>", "<BILLING>", "<FRFILING>",
                "<DATE>", "<CFRNO>", "<RINDOCK>", "</USDEPT>", "</AGENCY>", "</USBUREAU>", "</DOCTITLE>",
                "</ADDRESS>", "</FURTHER>", "</SUMMARY>", "</ACTION>", "</SIGNER>", "</SIGNJOB>", "</SUPPLEM>",
                "</BILLING>", "</FRFILING>", "</DATE>", "</CFRNO>", "</RINDOCK>", "<DATE1>", "</DATE1>"};

//        System.out.println(content);

        content.replace(".", " .");

        String[] docs = content.split("</DOC>");
        ArrayList<ContentBean> arrayList = new ArrayList<>();

        for (int i = 0; i < docs.length; i++) {

            try {

                String doc = docs[i];

                String date = "";
//            System.out.println(doc);

                String regex = "(\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s)";
                Matcher matcher = Pattern.compile(regex).matcher(doc);
                if (matcher.find()) {
                    date = matcher.group(1);
                    date = date.trim();
                    DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                    try {

//                    System.out.println(date);
                        df.parse(date);
                    } catch (ParseException e) {


//                    System.out.println("Error : " + date);
                    }
                }

                String regex1 = "(\\s+\\w+\\s+\\d+\\,\\s+\\d+\\s.)";
                Matcher matcher1 = Pattern.compile(regex1).matcher(doc);
                if (matcher1.find()) {
                    date = matcher1.group(1);
                    date = date.trim();
                    date.replace(".", "").trim();
                    DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                    try {

//                    System.out.println(date);
                        df.parse(date);
                    } catch (ParseException e) {


//                    System.out.println("Error : " + date);
                    }
                }

//            System.out.println(date);

//            if(date.equals("")){
//                System.out.println(doc);
//                System.exit(0);
//            }
                String newsPaper = "Federal Register - FR";

                if (!doc.contains("<DOCNO>")) {
                    continue;
                }
                String docNumber = doc.substring(doc.indexOf("<DOCNO>"), doc.indexOf("</DOCNO>")).replace("<DOCNO>", "").trim();
//            String date = doc.substring(doc.indexOf("<DATE1>"), doc.indexOf("</DATE1>")).replace("<DATE1>", "").trim();
                String title = "";
                if (doc.contains("<DOCTITLE>")) {
                    title = doc.substring(doc.indexOf("<DOCTITLE>"), doc.indexOf("</DOCTITLE>")).replace("<DOCTITLE>", "").trim();
                    if (title.contains("<DATE1>")) {
                        title = title.replace("</DATE1>", " ");
                        title = title.replace("<DATE1>", " ");
                    }
                } else {
                    title = "Federal Register : " + date;
                }
                String textContent = doc.substring(doc.indexOf("<TEXT>"), doc.indexOf("</TEXT>")).replace("<TEXT>", "").trim();
//            System.out.println(title);

//            ContentBean contentBean = new ContentBean(newsPaper, title, textContent, date, docNumber);
//            arrayList.add(contentBean);
                for (int j = 0; j < replace.length; j++) {
                    if (textContent.contains(replace[j])) {
                        textContent = textContent.replace(replace[j], " ");
                    }
                }
                ContentBean contentBean = new ContentBean(newsPaper, title, textContent, date, docNumber);
                arrayList.add(contentBean);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }


//            break;

        }
        return arrayList;


    }


    public static void main(String[] args) {

        FR94ReadFile fr94ReadFile = new FR94ReadFile();
        fr94ReadFile.iterateFiles();

    }

    public void setArrayDefault() {
        fullContent = new ArrayList<>();
    }
}

package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;

import java.util.ArrayList;

public class ReadAndWrite2JSON {

    private ArrayList<ContentBean> beans = new ArrayList<>();

    private void readFiles() {
        FR94ReadFile fr94ReadFile = new FR94ReadFile();
        FTReadFile ftReadFile = new FTReadFile();
        FBISReadFile fbisReadFile = new FBISReadFile();
        LATReadFile latReadFile = new LATReadFile();


        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ArrayList<ContentBean> beansFR94 = fr94ReadFile.getResult();
        fr94ReadFile.setArrayDefault();

        ArrayList<ContentBean> beansFBIS = fbisReadFile.getResult();
        fbisReadFile.setArrayDefault();

        ArrayList<ContentBean> beansFT = ftReadFile.getResult();
        ftReadFile.setArrayDefault();

        ArrayList<ContentBean> beansLat = latReadFile.getResult();
        latReadFile.setArrayDefault();


        beans.addAll(beansFR94);
        beans.addAll(beansFBIS);
        beans.addAll(beansFT);
        beans.addAll(beansLat);

//        System.out.println(gson.toJson(beans));

//        try (FileWriter file = new FileWriter("corpus/corpus.json")) {
//            file.write(gson.toJson(beans));
//            System.out.println("Successfully Copied JSON Object to File...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("Completed parsing................" + beans.size()+" Documents Found");


    }

    public ArrayList<ContentBean> returnCorpus(){
        readFiles();
        return beans;
    }

    public void garbageCollection(){
        beans.clear();
    }

//    public static void main(String[] args) {
//        ReadAndWrite2JSON readAndWrite2JSON = new ReadAndWrite2JSON();
//        ArrayList<ContentBean> corpus = readAndWrite2JSON.returnCorpus();
//
//
//    }
}

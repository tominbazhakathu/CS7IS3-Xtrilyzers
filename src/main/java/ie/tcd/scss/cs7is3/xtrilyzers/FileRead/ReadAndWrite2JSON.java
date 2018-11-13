package ie.tcd.scss.cs7is3.xtrilyzers.FileRead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;
import sun.jvm.hotspot.jdi.ConcreteMethodImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;

public class ReadAndWrite2JSON {

    private ArrayList<ContentBean> beans = new ArrayList<>();

    private void readFiles() {
        FR94ReadFile fr94ReadFile = new FR94ReadFile();
        FTReadFile ftReadFile = new FTReadFile();
        FBISReadFile fbisReadFile = new FBISReadFile();
        LATReadFile latReadFile = new LATReadFile();

//        fbisReadFile.iterateFiles();
//        fr94ReadFile.iterateFiles();
//        ftReadFile.iterateFiles();
//        latReadFile.iterateFiles();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ArrayList<ContentBean> beansFR94 = fr94ReadFile.getResult();
//        fr94ReadFile.setArrayDefault();
//        try (FileWriter file = new FileWriter("data/fr94.json")) {
//            file.write(gson.toJson(beansFR94));
//            System.out.println("Successfully Copied JSON Object to File...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ArrayList<ContentBean> beansFBIS = fbisReadFile.getResult();
        fr94ReadFile.setArrayDefault();

//        try (FileWriter file = new FileWriter("data/FBIS.json")) {
//            file.write(gson.toJson(beansFBIS));
//            System.out.println("Successfully Copied JSON Object to File...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ArrayList<ContentBean> beansFT = ftReadFile.getResult();
        fr94ReadFile.setArrayDefault();

//        try (FileWriter file = new FileWriter("data/ft.json")) {
//            file.write(gson.toJson(beansFT));
//            System.out.println("Successfully Copied JSON Object to File...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ArrayList<ContentBean> beansLat = latReadFile.getResult();
        fr94ReadFile.setArrayDefault();

//        try (FileWriter file = new FileWriter("data/lat.json")) {
//            file.write(gson.toJson(beansLat));
//            System.out.println("Successfully Copied JSON Object to File...");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        beans.addAll(beansFR94);
        beans.addAll(beansFBIS);
        beans.addAll(beansFT);
        beans.addAll(beansLat);

//        System.out.println(gson.toJson(beans));

        try (FileWriter file = new FileWriter("corpus/corpus.json")) {
            file.write(gson.toJson(beans));
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Completed parsing................");


    }

    public ArrayList<ContentBean> returnCorpus(){
        readFiles();
        return beans;
    }


    public static void main(String[] args) {
        ReadAndWrite2JSON readAndWrite2JSON = new ReadAndWrite2JSON();
        ArrayList<ContentBean> corpus = readAndWrite2JSON.returnCorpus();


    }
}

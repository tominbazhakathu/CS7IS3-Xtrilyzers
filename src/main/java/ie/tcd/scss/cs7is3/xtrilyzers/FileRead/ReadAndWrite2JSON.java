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

        beans.addAll(fr94ReadFile.getResult());
        beans.addAll(fbisReadFile.getResult());
        beans.addAll(ftReadFile.getResult());
        beans.addAll(latReadFile.getResult());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(beans));

        try (FileWriter file = new FileWriter("data/inputfile.json")) {
            file.write(gson.toJson(beans));
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        ReadAndWrite2JSON readAndWrite2JSON = new ReadAndWrite2JSON();
        readAndWrite2JSON.readFiles();

    }
}

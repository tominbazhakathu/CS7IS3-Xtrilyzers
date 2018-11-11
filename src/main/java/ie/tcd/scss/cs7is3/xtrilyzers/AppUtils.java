package ie.tcd.scss.cs7is3.xtrilyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ie.tcd.scss.cs7is3.xtrilyzers.ParseDoc.DocField;

class ParseDoc {
  public static final int ID_MARK_OFFSET = 3;
  public static final int FIELD_MARK_LENGTH = 2;
  private String id;
  private String paperName;
  private String title;
  private String date;
  private String content;
  
  public enum DocField {
    //ID(".I", "Id"), TITLE(".T", "Title"), AUTHOR(".A", "Author"), BIBLIO(".B", "Bibliography"), WORDS(".W", "Words");
    ID("documentID", "Id"), NAME("newsPaperName", "paperName"), TITLE("documentTitle", "Title"), DATE("documentDate", "Date"), CONTENT("aticleContent", "Content"); 

    private String attribute = "";
    private String label = "";

    public String getAttribute() {
      return this.attribute;
    }
    
    public String getLabel() {
      return this.label;
    }

    private DocField(String attribute, String label) {
      this.attribute = attribute;
      this.label = label;
    }
    
    public static DocField getDocField(String strField) {
      DocField docField;
      return null;
//      if (strField.equals(DocField.TITLE.getMark())) {
//        docField = DocField.TITLE;
//      }
//      else if (strField.equals(DocField.AUTHOR.getMark())){
//        docField = DocField.AUTHOR;
//      }
//      else if (strField.equals(DocField.BIBLIO.getMark())){
//        docField = DocField.BIBLIO;
//      }
//      else if (strField.equals(DocField.WORDS.getMark())){
//        docField = DocField.WORDS;
//      }
//      else {
//        throw new RuntimeException();
//      }
//      return docField;
    }
  }

  public ParseDoc(String id, String paperName, String title, String date, String content) {
    this.id = id;
    this.paperName = paperName;
    this.title = title;
    this.date = date;
    this.content = content;
  }
  
  public String getId() {
    return id;
  }

  public String getPaperName() {
    return paperName;
  }
  
  public String getTitle() {
    return title;
  }

  public String getDate() {
    return date;
  }

  public String getContent() {
    return content;
  }
}

class ParseQuery {
  public static final int ID_MARK_OFFSET = 3;
  public static final int FIELD_MARK_LENGTH = 2;

  private int querySeq;
  private String queryId;
  private StringBuilder words;
  private List<QueryResult> results;

  public enum QueryField {
    ID(".I"), WORDS(".W");

    private String mark = "";

    public String getMark() {
      return this.mark;
    }

    private QueryField(String mark) {
      this.mark = mark;
    }
  }
  
  public void setQuerySeq(int querySeq) {
    this.querySeq = querySeq;
  }

  public int getQuerySeq() {
    return querySeq;
  }

  public ParseQuery(String queryId) {
    this.queryId = queryId;
    this.words = new StringBuilder();
  }

  public String getQueryId() {
    return queryId;
  }

  public void addWords(StringBuilder words) {
    this.words.append(" ");
    this.words.append(words);
  }

  public String getWords() {
    return this.words.toString();
  }

  public void setResults(List<QueryResult> results) {
    this.results = results;
  }

  public List<QueryResult> getResults() {
    return results;
  }

  public static boolean isValidField(String fieldMark) {
    boolean validField = fieldMark.equals(ParseQuery.QueryField.ID.getMark()) ||
                         fieldMark.equals(ParseQuery.QueryField.WORDS.getMark());
    return validField;
  }
}

class ParseTopic {
  public static final int ID_MARK_OFFSET = 3;
  public static final int FIELD_MARK_LENGTH = 2;

  private int querySeq;
  private String queryId;
  private StringBuilder words;
  private List<QueryResult> results;

  public enum QueryField {
    ID(".I"), WORDS(".W");

    private String mark = "";

    public String getMark() {
      return this.mark;
    }

    private QueryField(String mark) {
      this.mark = mark;
    }
  }
  
  public void setQuerySeq(int querySeq) {
    this.querySeq = querySeq;
  }

  public int getQuerySeq() {
    return querySeq;
  }

  public ParseTopic(String queryId) {
    this.queryId = queryId;
    this.words = new StringBuilder();
  }

  public String getQueryId() {
    return queryId;
  }

  public void addWords(StringBuilder words) {
    this.words.append(" ");
    this.words.append(words);
  }

  public String getWords() {
    return this.words.toString();
  }

  public void setResults(List<QueryResult> results) {
    this.results = results;
  }

  public List<QueryResult> getResults() {
    return results;
  }

  public static boolean isValidField(String fieldMark) {
    boolean validField = fieldMark.equals(ParseQuery.QueryField.ID.getMark()) ||
                         fieldMark.equals(ParseQuery.QueryField.WORDS.getMark());
    return validField;
  }
}


class QueryResult {
  private String docId;
  private String score;

  public QueryResult(String docId, String score) {
    this.docId = docId;
    this.score = score;
  }

  public String getDocId() {
    return docId;
  }

  public String getScore() {
    return score;
  }
}

class AppUtils {
  
  public static List<ParseDoc> parseCorpus(String path) throws IOException {
    List<ParseDoc> docs = new ArrayList<ParseDoc>();
    
    try {
      JsonArray jsonObjects = (JsonArray) new JsonParser().parse(new FileReader(path));
       for (int i = 0; i < jsonObjects.size(); i++) {
         String id = ((JsonObject)jsonObjects.get(i)).get(DocField.ID.getAttribute()).getAsString();
         String paperName = ((JsonObject)jsonObjects.get(i)).get(DocField.NAME.getAttribute()).getAsString();
         String title = ((JsonObject)jsonObjects.get(i)).get(DocField.TITLE.getAttribute()).getAsString();
         String date = ((JsonObject)jsonObjects.get(i)).get(DocField.DATE.getAttribute()).getAsString();
         String content = ((JsonObject)jsonObjects.get(i)).get(DocField.CONTENT.getAttribute()).getAsString();
         ParseDoc doc = new ParseDoc(id, paperName, title, date, content);
         docs.add(doc);
       }     
    }  
    catch (JsonIOException e) {
       e.printStackTrace();
   } catch (JsonSyntaxException e) {
       e.printStackTrace();
   } catch (FileNotFoundException e) {
       e.printStackTrace();
   }
    return docs;
  }

  public static List<ParseQuery> parseQueries(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String lastLineRead;
    
    lastLineRead = reader.readLine();
    List<ParseQuery> queries = new ArrayList<ParseQuery>();
    while (true) {
      lastLineRead = parseQuery(lastLineRead, reader, queries);
      if (lastLineRead == null) {
        break;
      }
    }
    // Close file
    reader.close();
    return queries;
  }

  // shouldn't be called with lastLineRead null
  private static String parseQuery(String lastLineRead, BufferedReader reader, List<ParseQuery> queries) throws IOException {
    // Create a new query object
    ParseQuery qry;
    
    // process id header
    if (lastLineRead.startsWith(ParseQuery.QueryField.ID.getMark())) {
      qry = new ParseQuery(lastLineRead.substring(ParseDoc.ID_MARK_OFFSET));
    }
    else {
      throw new RuntimeException();
    }

    // reading query content past id field
    lastLineRead = reader.readLine();
    
    if (lastLineRead == null) {
      // there should be content post query id 
      throw new RuntimeException();
    }
    
    // while there are valid fields within this query doc
    while (true) {
      lastLineRead = parseQueryField(lastLineRead, reader, qry);
      
      if (lastLineRead == null || lastLineRead.startsWith(ParseQuery.QueryField.ID.getMark())) {
        queries.add(qry);
        break;
      }
    }
    return lastLineRead;
  }

  // throws exception if field doesn't follow format
  private static String parseQueryField(String lastLineRead, BufferedReader reader, ParseQuery parseQuery) throws IOException {
    //processing field header
    String strField = lastLineRead.substring(0, ParseQuery.FIELD_MARK_LENGTH);
    
    if (!ParseQuery.isValidField(strField)) {
      throw new RuntimeException();
    }

    StringBuilder sb = new StringBuilder();
    
    // processing field content, while field doesn't end
    String currentLine;
    while (true) {
      currentLine = reader.readLine();
      if (currentLine == null) {
        break;
      }

      strField = currentLine.substring(0, ParseQuery.FIELD_MARK_LENGTH);
      if (ParseQuery.isValidField(strField)) {
        break;
      }
      sb.append(" ");
      sb.append(currentLine);
    }
    parseQuery.addWords(sb);
    return currentLine;
  }

  public static List<ParseQuery> parseTopics(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String lastLineRead;
    
    lastLineRead = reader.readLine();
    List<ParseQuery> topics = new ArrayList<ParseQuery>();
    while (true) {
      lastLineRead = parseQuery(lastLineRead, reader, topics);
      if (lastLineRead == null) {
        break;
      }
    }
    // Close file
    reader.close();
    return topics;
  }
  
  public static void writeResults(String path, List<ParseQuery> queries) throws IOException {
    File file = new File(path);
    FileOutputStream fos = new FileOutputStream(file);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    for (ParseQuery qry : queries) {
      List<QueryResult> results = qry.getResults();
      for (QueryResult result : results) {
        StringBuilder sb = new StringBuilder();
        sb.append(qry.getQuerySeq());
        sb.append(" ");
        sb.append("0");
        sb.append(" ");
        sb.append(result.getDocId());
        sb.append(" ");
        sb.append("1");
        sb.append(" ");
        sb.append(result.getScore());
        sb.append(" ");
        sb.append("STANDARD");
        bw.write(sb.toString());
        bw.newLine();
      }
    }
    bw.close();
  }
}
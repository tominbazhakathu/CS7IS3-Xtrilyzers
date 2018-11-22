package ie.tcd.scss.cs7is3.xtrilyzers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ie.tcd.scss.cs7is3.xtrilyzers.BeanClass.ContentBean;
import ie.tcd.scss.cs7is3.xtrilyzers.FileRead.ReadAndWrite2JSON;

class ParseDoc {
  private String id;
  private String paperName;
  private String title;
  private String date;
  private String content;

  public enum DocField {
    ID("documentID", "Id"), NAME("newsPaperName", "paperName"), TITLE("documentTitle", "Title"),
    DATE("documentDate", "Date"), CONTENT("aticleContent", "Content");

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

  public ParseQuery(String queryId, StringBuilder words) {
    this.words = words;
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
    boolean validField = fieldMark.equals(ParseQuery.QueryField.ID.getMark())
        || fieldMark.equals(ParseQuery.QueryField.WORDS.getMark());
    return validField;
  }
}

class ParseTopic {
  public static final String TOPIC_START_TAG = "<top>";
  public static final String TOPIC_END_TAG = "</top>";

  private int seq;
  private String num;
  private String title;
  private String description;
  private String narrative;

  private List<QueryResult> results;

  public enum TopicField {
    NUM("<num>", "</num>"), TITLE("<title>", "</title>"), DESCRIPTION("<desc>", "</desc>"),
    NARRATIVE("<narr>", "</narr>");

    private String startTag = "";
    private String endTag = "";

    public String getStartTag() {
      return startTag;
    }

    public String getEndTag() {
      return endTag;
    }

    private TopicField(String startTag, String endTag) {
      this.startTag = startTag;
      this.endTag = endTag;
    }
  }

  public ParseTopic(String num, String title, String description, String narrative) {
    if (num.contains("Number:")) {
      num = num.substring(num.indexOf(":") + 1).trim();
    }
    this.num = num;
    this.title = title;
    this.description = description;
    this.narrative = narrative;
  }

  public void setSeq(int seq) {
    this.seq = seq;
  }

  public int getSeq() {
    return seq;
  }

  // ???why?
  public String getNum() {
    if (num.contains("Number:")) {
      num = num.substring(num.indexOf(":") + 1).trim();
    }
    return num;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getNarrative() {
    return narrative;
  }
  
  public void setResults(List<QueryResult> results) {
    this.results = results;
  }

  public List<QueryResult> getResults() {
    return results;
  }

  public static boolean isValidField(String fieldMark) {
    boolean validField = fieldMark.equals(ParseQuery.QueryField.ID.getMark())
        || fieldMark.equals(ParseQuery.QueryField.WORDS.getMark());
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

  public static List<ParseDoc> parseCorpus() throws IOException {
    List<ParseDoc> docs = new ArrayList<ParseDoc>();

    ArrayList<ContentBean> docBeans;
    ReadAndWrite2JSON readAndWrite2JSON = new ReadAndWrite2JSON();
    docBeans = readAndWrite2JSON.returnCorpus();
    System.out.println(docBeans.size());
    // readAndWrite2JSON.garbageCollection();

    for (int i = 0; i < docBeans.size(); i++) {

      String id = docBeans.get(i).getDocumentID();
      String title = docBeans.get(i).getDocumentTitle();
      String newspaper = docBeans.get(i).getNewsPaperName();
      String date = docBeans.get(i).getDocumentDate();
      String content = docBeans.get(i).getAticleContent();
      ParseDoc doc = new ParseDoc(id, newspaper, title, date, content);

      docs.add(doc);

    }

    return docs;
  }

  /*
   * public static List<ParseQuery> parseQueries(String path) throws IOException {
   * BufferedReader reader = new BufferedReader(new FileReader(path)); String
   * lastLineRead;
   * 
   * lastLineRead = reader.readLine(); List<ParseQuery> queries = new
   * ArrayList<ParseQuery>(); while (true) { lastLineRead =
   * parseQuery(lastLineRead, reader, queries); if (lastLineRead == null) { break;
   * } } // Close file reader.close(); return queries; }
   * 
   * // shouldn't be called with lastLineRead null private static String
   * parseQuery(String lastLineRead, BufferedReader reader, List<ParseQuery>
   * queries) throws IOException { // Create a new query object ParseQuery qry;
   * 
   * // process id header if
   * (lastLineRead.startsWith(ParseQuery.QueryField.ID.getMark())) { qry = new
   * ParseQuery(lastLineRead.substring(ParseDoc.ID_MARK_OFFSET)); } else { throw
   * new RuntimeException(); }
   * 
   * // reading query content past id field lastLineRead = reader.readLine();
   * 
   * if (lastLineRead == null) { // there should be content post query id throw
   * new RuntimeException(); }
   * 
   * // while there are valid fields within this query doc while (true) {
   * lastLineRead = parseQueryField(lastLineRead, reader, qry);
   * 
   * if (lastLineRead == null ||
   * lastLineRead.startsWith(ParseQuery.QueryField.ID.getMark())) {
   * queries.add(qry); break; } } return lastLineRead; }
   * 
   * // throws exception if field doesn't follow format private static String
   * parseQueryField(String lastLineRead, BufferedReader reader, ParseQuery
   * parseQuery) throws IOException { //processing field header String strField =
   * lastLineRead.substring(0, ParseQuery.FIELD_MARK_LENGTH);
   * 
   * if (!ParseQuery.isValidField(strField)) { throw new RuntimeException(); }
   * 
   * StringBuilder sb = new StringBuilder();
   * 
   * // processing field content, while field doesn't end String currentLine;
   * while (true) { currentLine = reader.readLine(); if (currentLine == null) {
   * break; }
   * 
   * strField = currentLine.substring(0, ParseQuery.FIELD_MARK_LENGTH); if
   * (ParseQuery.isValidField(strField)) { break; } sb.append(" ");
   * sb.append(currentLine); } parseQuery.addWords(sb); return currentLine; }
   */

  public static List<ParseTopic> parseTopics(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    StringBuffer sbTopicsLines = new StringBuffer(); 
    while ((line = reader.readLine()) != null) {
      // NOTE: adding a " " character to account for words that were only separated by EOLs, which get consumed by readLine() 
      sbTopicsLines.append(line + " ");
    }
    // Close file
    reader.close();

    List<ParseTopic> topics = new ArrayList<ParseTopic>();
    int seq = 1;
    do {
      int start = sbTopicsLines.indexOf(ParseTopic.TOPIC_START_TAG);
      int end = sbTopicsLines.indexOf(ParseTopic.TOPIC_END_TAG);
      if (start == -1 || end == -1) {
        break;
      }
      StringBuffer topicText = new StringBuffer(sbTopicsLines.substring(start + ParseTopic.TOPIC_START_TAG.length(), end));
      ParseTopic topic = parseTopic(topicText);
      topic.setSeq(seq++);
      topics.add(topic);
      sbTopicsLines = new StringBuffer(sbTopicsLines.substring(end + ParseTopic.TOPIC_END_TAG.length()));

    } while (true);
    return topics;
  }

  public static ParseTopic parseTopic(StringBuffer sb) {
    int startNum, startTitle, startDescription, startNarrative;

    startNum = sb.indexOf(ParseTopic.TopicField.NUM.getStartTag());
    startTitle = sb.indexOf(ParseTopic.TopicField.TITLE.getStartTag());
    startDescription = sb.indexOf(ParseTopic.TopicField.DESCRIPTION.getStartTag());
    startNarrative = sb.indexOf(ParseTopic.TopicField.NARRATIVE.getStartTag());
    
    if (startNum == -1 || startTitle == -1 || startDescription == -1 || startNarrative == -1) {
      // throw new RuntimeException();
    }

    /*
     * return new ParseTopic( sb.substring(startNum, startTitle),
     * sb.substring(startTitle, startNarrative), sb.substring(startDescription,
     * startNarrative), sb.substring(startNarrative) );
     */

    String num = sb.substring(startNum + ParseTopic.TopicField.NUM.getStartTag().length(), startTitle);
    num = num.replace("Number:", " ");
    String title = sb.substring(startTitle + ParseTopic.TopicField.TITLE.getStartTag().length(), startDescription);
    String description = sb.substring(startDescription + ParseTopic.TopicField.DESCRIPTION.getStartTag().length(), startNarrative);
    description = description.replace("Description:", " ");
    String narrative = sb.substring(startNarrative + ParseTopic.TopicField.NARRATIVE.getStartTag().length());
    narrative = narrative.replace("Narrative:", " ");
    
    ParseTopic pt = new ParseTopic(
        num,
        title,
        description,
        narrative);
    return pt;
  }
  
  public static String[] splitNarrative(String narrative) {
    // Replacing the slash character by a space. Slash appears to divide certain words 
    // Sentences, and pieces of sentences on the topic narrative seems to be separated by either comma, semicolon, and dot
    //System.out.println("narrative before-> " + narrative);
    String[] sentences = narrative.replace("/", " ").split("[\\.|,|;]");
    Pattern p = Pattern.compile(".*not relevant.*");
    
    List<String> sentencesToInclude =
      Arrays.stream(sentences).filter((String sentence) -> !p.matcher(sentence).matches()).collect(Collectors.toList());
    
    List<String> sentencesToNOTInclude =
        Arrays.stream(sentences).filter((String sentence) -> p.matcher(sentence).matches()).collect(Collectors.toList());
    
    StringBuilder sb1 = new StringBuilder();
    for(String sentence : sentencesToInclude) {
        sb1.append(sentence);
    }
    
    StringBuilder sb2 = new StringBuilder();
    for(String sentence : sentencesToNOTInclude) {
        sb2.append(sentence);
    }
    
    //System.out.println("narrative after-> " + sb.toString());
    //Much more logic could be used to "interpret" the narrative
    return new String[] { sb1.toString(), sb2.toString() };
  }

  public static void writeResults(String path, List<ParseTopic> topics) throws IOException {
    File file = new File(path);
    FileOutputStream fos = new FileOutputStream(file);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    for (ParseTopic topic : topics) {
      List<QueryResult> results = topic.getResults();
      for (QueryResult result : results) {
        StringBuilder sb = new StringBuilder();
        sb.append(topic.getNum());
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
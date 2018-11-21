package ie.tcd.scss.cs7is3.xtrilyzers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class App {
  // Directory where the search index will be saved
  private final static String INDEX_DIRECTORY = "index";
  private final static String CORPUS_PATH = "corpus/sample.json";
  private final static String TOPICS_PATH = "topics/CS7IS3-Assignment2-Topics";
  private final static String SEARCH_RESULTS_PATH = "results/results.txt";

  // Limit the number of search results we get
  private static int MAX_RESULTS = 1000;

  private static Analyzer analyzer;
  private static Similarity similarity;
  private static Directory directory;

  public static enum FIELD_COMBINATION {
    JUST_CONTENT, ALL_FIELDS, TITLE_AND_CONTENT
  }

  public static void init(int indexAnalyzer, int indexSimilarity) throws IOException {
    Analyzer[] analyzers = new Analyzer[] { 
        new StandardAnalyzer(), 
        new EnglishAnalyzer(), 
        new ClassicAnalyzer(),
        new CustomAnalyzer(), 
        new CustomAnalyzer(new CharArraySet(
            Arrays.asList("with", "were", "also", "some", "when", "over", "other", "both", "into"), false)) 
      };
    Similarity[] similarities = new Similarity[] { 
        new BM25Similarity(), 
        new ClassicSimilarity(),
        new MultiSimilarity(new Similarity[] { new ClassicSimilarity(), new BM25Similarity() }) 
      };

    analyzer = analyzers[indexAnalyzer];
    similarity = similarities[indexSimilarity];
    // analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
    // analyzer = new GPVAnalyzer(EnglishAnalyzer.getDefaultStopSet());
    // analyzer = new EnglishAnalyzer();
    // analyzer = new StandardAnalyzer();
    // analyzer = new ClassicAnalyzer(EnglishAnalyzer.getDefaultStopSet());
    // analyzer = new ClassicAnalyzer();

    // similarity = new ClassicSimilarity();
    // similarity = new BM25Similarity();
    // similarity = new MultiSimilarity(new Similarity[] {new ClassicSimilarity(),
    // new BM25Similarity()});
    directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
  }

  public static void buildIndex(FIELD_COMBINATION fc) {
    // Analyzer that is used to process TextField
    try {
      // To store an index in memory
      // Directory directory = new RAMDirectory();
      // To store an index on disk
      IndexWriterConfig config = new IndexWriterConfig(analyzer);
      if (similarity != null) {
        config.setSimilarity(similarity);
        // config.setSimilarity(new ClassicSimilarity());
        // config.setSimilarity(new BM25Similarity());
        // config.setSimilarity(new MultiSimilarity(new Similarity[] {new
        // ClassicSimilarity(), new BM25Similarity()}));
      }

      config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

      IndexWriter iwriter = new IndexWriter(directory, config);
      List<ParseDoc> parseDocs = AppUtils.parseCorpus();

      System.out.println(parseDocs.size() + " Parsed Document");

      for (ParseDoc parseDoc : parseDocs) {
        Document doc = new Document();
        doc.add(new StringField(ParseDoc.DocField.ID.getLabel(), parseDoc.getId(), Field.Store.YES));

        StringBuilder sb = new StringBuilder();
        // just title and words give lower score than everything combined
        switch (fc) {
          case JUST_CONTENT:
            sb.append(parseDoc.getContent());
            break;
          case ALL_FIELDS:
            sb.append(parseDoc.getTitle());
            sb.append(" ");
            sb.append(parseDoc.getPaperName());
            sb.append(" ");
            sb.append(parseDoc.getDate());
            sb.append(" ");
            sb.append(parseDoc.getContent());
            break;
          case TITLE_AND_CONTENT:
            sb.append(parseDoc.getTitle());
            sb.append(" ");
            sb.append(parseDoc.getContent());
        }
        doc.add(new TextField(ParseDoc.DocField.CONTENT.getLabel(), sb.toString(), Field.Store.YES));
        /*
         * doc.add(new TextField(ParseDoc.DocField.TITLE.getLabel(),
         * parseDoc.getTitle(), Field.Store.YES)); doc.add(new
         * TextField(ParseDoc.DocField.AUTHOR.getLabel(), parseDoc.getAuthor(),
         * Field.Store.YES)); doc.add(new TextField(ParseDoc.DocField.BIBLIO.getLabel(),
         * parseDoc.getBiblio(), Field.Store.YES)); doc.add(new
         * TextField(ParseDoc.DocField.WORDS.getLabel(), parseDoc.getWords(),
         * Field.Store.YES));
         */
        iwriter.addDocument(doc);
      }
      iwriter.close();
      // directory.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Pending question: can you split the search vs title and content, and combine
  // them in some way?
  public static void evaluateTopics() {
    try {
      List<ParseTopic> topics = AppUtils.parseTopics(TOPICS_PATH);
      // System.out.println("Number topics: " + parseTopics.size());
      // Open the folder that contains our search index
      // Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

      // create objects to read and search across the index
      DirectoryReader ireader = DirectoryReader.open(directory);
      IndexSearcher isearcher = new IndexSearcher(ireader);
      if (similarity != null) {
        isearcher.setSimilarity(similarity);
        // isearcher.setSimilarity(new ClassicSimilarity());
        // isearcher.setSimilarity(new BM25Similarity());
        // isearcher.setSimilarity(new MultiSimilarity(new Similarity[] {new
        // ClassicSimilarity(), new BM25Similarity()}));
      }

      for (ParseTopic topic : topics) {
        // generate queries
        List<ParseQuery> queries = generateQueries(topic);

        QueryParser parser = new QueryParser(ParseDoc.DocField.CONTENT.getLabel(), analyzer);

        for (ParseQuery qry : queries) {
          // parse the query with the parser
          // System.out.println("Query Id: " + qry.getQueryId());
          // System.out.println("Query Id: " + qry.getWords());
          Query query = parser.parse(QueryParser.escape(qry.getWords().trim().replace("?", "\\?")));

          // Get the set of results
          ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;

          List<QueryResult> results = new ArrayList<QueryResult>();

          // System.out.println("Documents: " + hits.length);
          for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            String docId = hitDoc.get(ParseDoc.DocField.ID.getLabel());
            String score = Float.toString(hits[i].score);
            QueryResult result = new QueryResult(docId, score);
            // System.out.println(i + ") " + hitDoc.get(ParseDoc.DocField.ID.getLabel()) + "
            // " + hits[i].score);
            results.add(result);
          }
          qry.setResults(results);
        }
        // TODO: Need additional code to merge queries and results in case a topic
        // generates multiple queries
        topic.setResults(queries.get(0).getResults());
      }
      AppUtils.writeResults(SEARCH_RESULTS_PATH, topics);
      // AppUtils.writeResults(RESULTS_PATH + "_f" + indexFieldCombination + "a"+
      // indexAnalyzer + "s" + indexSimilarity, topics);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException pe) {
      pe.printStackTrace();
    }
  }

  // TODO: Need a way to generate more elaborated queries
  public static List<ParseQuery> generateQueries(ParseTopic parseTopic) {
    List<ParseQuery> queries = new ArrayList<ParseQuery>();
    //try below without escaping manually
    StringBuilder sb = new StringBuilder(parseTopic.getTitle().replace("\"", ""));
    sb.append(" ");
    sb.append(parseTopic.getDescription().replace("\"", ""));
    //sb.append(" ");
    //sb.append(parseTopic.getNarrative().replace("\"", ""));
    queries.add(new ParseQuery("1", sb));
    //Many ways of generating the query like taking only the description, description and title, description and narrative, understanding narrative, etc.
    return queries;
  }

  /*
   * //Pending question: can you split the search vs title and content, and
   * combine them in some way? public static void evaluateQueries() { try {
   * List<ParseQuery> queries = AppUtils.parseQueries(QUERIES_PATH);
   * //System.out.println("Number queries: " + queries.size()); //Open the folder
   * that contains our search index //Directory directory =
   * FSDirectory.open(Paths.get(INDEX_DIRECTORY));
   * 
   * // create objects to read and search across the index DirectoryReader ireader
   * = DirectoryReader.open(directory); IndexSearcher isearcher = new
   * IndexSearcher(ireader); if (similarity != null) {
   * isearcher.setSimilarity(similarity); //isearcher.setSimilarity(new
   * ClassicSimilarity()); //isearcher.setSimilarity(new BM25Similarity());
   * //isearcher.setSimilarity(new MultiSimilarity(new Similarity[] {new
   * ClassicSimilarity(), new BM25Similarity()})); }
   * 
   * QueryParser parser = new QueryParser(ParseDoc.DocField.CONTENT.getLabel(),
   * analyzer);
   * 
   * int seq = 1; for (ParseQuery qry : queries) { // parse the query with the
   * parser //System.out.println("Query Id: " + qry.getQueryId());
   * //System.out.println("Query Id: " + qry.getWords()); qry.setQuerySeq(seq);
   * Query query = parser.parse(qry.getWords().trim().replace("?","\\?"));
   * 
   * // Get the set of results ScoreDoc[] hits = isearcher.search(query,
   * MAX_RESULTS).scoreDocs;
   * 
   * List<QueryResult> results = new ArrayList<QueryResult>();
   * 
   * // Print the results //System.out.println("Documents: " + hits.length); for
   * (int i = 0; i < hits.length; i++) { Document hitDoc =
   * isearcher.doc(hits[i].doc); String docId =
   * hitDoc.get(ParseDoc.DocField.ID.getLabel()); String score =
   * Float.toString(hits[i].score); QueryResult result = new QueryResult(docId,
   * score); //System.out.println(i + ") " +
   * hitDoc.get(ParseDoc.DocField.ID.getLabel()) + " " + hits[i].score);
   * results.add(result); } qry.setResults(results); seq++; }
   * AppUtils.writeResults(RESULTS_PATH, queries);
   * //AppUtils.writeResults(RESULTS_PATH + "_f" + indexFieldCombination + "a"+
   * indexAnalyzer + "s" + indexSimilarity, queries); } catch (IOException e) {
   * e.printStackTrace(); } catch (ParseException pe) { pe.printStackTrace(); } }
   */

  public static void shutdown() throws IOException {
    directory.close();
  }

  public static void main(String[] args) throws IOException {
    int indexFieldCombination = -1;
    int indexAnalyzer = -1;
    int indexSimilarity = -1;

    for (int i = 0; i < args.length; i++) {
      switch (args[i].charAt(0)) {
        case '-':
          if (args[i].length() < 2) {
            System.out.println("here1");
            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
          }
          if (args[i].charAt(1) != 'f' && args[i].charAt(1) != 'a' && args[i].charAt(1) != 's') {
            System.out.println("here2");
            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
          }
          if (args[i + 1].length() != 1) {
            System.out.println("::" + args[i + 1] + "::");
            System.out.println("here3");
            throw new IllegalArgumentException("Not a valid argument: " + args[i + 1]);
          }
          if (args[i].charAt(1) == 'f') {
            indexFieldCombination = Integer.parseInt(args[i + 1]);
          }
          if (args[i].charAt(1) == 'a') {
            indexAnalyzer = Integer.parseInt(args[i + 1]);
          }
          if (args[i].charAt(1) == 's') {
            indexSimilarity = Integer.parseInt(args[i + 1]);
          }
          i++;
          break;
        default:
          System.out.println("here4");
          throw new IllegalArgumentException("Not a valid argument: " + args[i]);
      }
    }
    if (indexFieldCombination == -1 || indexAnalyzer == -1 || indexSimilarity == -1) {
      throw new IllegalArgumentException("Not enough arguments");
    }
//    indexFieldCombination = 0;
//    indexAnalyzer = 0;
//    indexSimilarity = 0;
    init(indexAnalyzer, indexSimilarity);
    buildIndex(FIELD_COMBINATION.values()[indexFieldCombination]);
    evaluateTopics();
    shutdown();
    System.out.println("Processing done");
  }
}
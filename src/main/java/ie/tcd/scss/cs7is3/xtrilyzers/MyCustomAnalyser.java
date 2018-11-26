package ie.tcd.scss.cs7is3.xtrilyzers;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyCustomAnalyser extends StopwordAnalyzerBase {

    private final Path currentRelativePath = Paths.get("").toAbsolutePath();
    private BufferedReader countries;

    @Override
    protected TokenStreamComponents createComponents(String s) {
        final Tokenizer tokenizer = new StandardTokenizer();
        String[] stopWords = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
        TokenStream tokenStream = new StandardFilter(tokenizer);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new TrimFilter(tokenStream);
        tokenStream = new FlattenGraphFilter(new SynonymGraphFilter(tokenStream, createSynonymMap(), true));
        tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(stopWords,true));
        tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());
        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    private SynonymMap createSynonymMap() {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        try {
            countries = new BufferedReader(new FileReader(currentRelativePath + "/corpus/countries.txt"));

            final SynonymMap.Builder builder = new SynonymMap.Builder(true);
            String country = countries.readLine();

            while(country != null) {
                builder.add(new CharsRef("country"), new CharsRef(country), true);
                builder.add(new CharsRef("countries"), new CharsRef(country), true);
                country = countries.readLine();
            }

            synMap = builder.build();
        } catch (Exception e) {
            System.out.println(String.format("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create synonym map"));
        }
        return synMap;
    }
}

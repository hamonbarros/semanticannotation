package br.com.sann.service.similarity;
import java.io.IOException;
import java.util.*;
 
import org.apache.commons.math3.linear.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;
 
public class CosineDocumentSimilarity {
 
    public static final String CONTENT = "Content";
 
    private final Set<String> terms = new HashSet<>();
    private final RealVector v1;
    private final RealVector v2;
 
    CosineDocumentSimilarity(String s1, String s2) throws IOException {
        Directory directory = createIndex(s1, s2);
        IndexReader reader = DirectoryReader.open(directory);
        Map<String, Integer> f1 = getTermFrequencies(reader, 0);
        Map<String, Integer> f2 = getTermFrequencies(reader, 1);
        reader.close();
        v1 = toRealVector(f1);
        v2 = toRealVector(f2);
    }
 
    Directory createIndex(String s1, String s2) throws IOException {
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_9,
                analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);
        addDocument(writer, s1);
        addDocument(writer, s2);
        writer.close();
        return directory;
    }
 
    void addDocument(IndexWriter writer, String content) throws IOException {
        Document doc = new Document();
        doc.add(new VecTextField(CONTENT, content, Store.YES));
        writer.addDocument(doc);
    }
 
    double getCosineSimilarity() {
        return (v1.dotProduct(v2)) / (v1.getNorm() * v2.getNorm());
    }
 
    public static double getCosineSimilarity(String s1, String s2)
            throws IOException {
    	
    	double cosine = 0.0;
    	try {

    		cosine = new CosineDocumentSimilarity(s1, s2).getCosineSimilarity();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return cosine;
    }
 
    Map<String, Integer> getTermFrequencies(IndexReader reader, int docId)
            throws IOException {
        Terms vector = reader.getTermVector(docId, CONTENT);
        TermsEnum termsEnum = null;
        termsEnum = vector.iterator(termsEnum);
        Map<String, Integer> frequencies = new HashMap<>();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, freq);
            terms.add(term);
        }
        return frequencies;
    }
 
    RealVector toRealVector(Map<String, Integer> map) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        for (String term : terms) {
            int value = map.containsKey(term) ? map.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return (RealVector) vector.mapDivide(vector.getL1Norm());
    }
    
    public static void main(String[] args) throws IOException {
		
    	System.out.println(getCosineSimilarity("o rato roeu a roupa do rei de roma","rei de roma"));
	}
}
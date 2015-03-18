package br.com.sann.service.processing.text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import br.com.sann.main.Main;

/**
 * Classe responsável por pré-processar um determinado texto.
 * 
 * @author Hamon
 *
 */
public class PreProcessingText {
	
	private static PreProcessingText instance;
	
	private String pathTreeTagger = "";
	private final String[] commonWords = {"area", "level", "point", "polygon", "line", "zoon"};

	/**
	 * M�todo construtor privado.
	 */
	private PreProcessingText() {
		loadPropertyTreeTagger();
	}
	
	public static synchronized PreProcessingText getInstance() {
		if (instance == null) {
			instance = new PreProcessingText();
		}
		return instance;
	}
	
	/**
	 * Realiza o pr�-processamento do texto passado como par�metro.
	 * @param text O texto a ser pr�-processado.
	 * @return O texto pr�-processado.
	 */
	public List<String> preProcessing(String text) {
		
		text = extractScale(text);
		List<String> tokens = tokenizing(text, Version.LUCENE_4_9);
		return extractNounsAndAdjectives(tokens);
		
	}
	
	/**
	 * Realiza o pr�-processamento do texto passado como par�metro sem a
	 * extra��o de escalas.
	 * @param text O texto a ser pr�-processado.
	 * @return O texto pr�-processado.
	 */
	public String preProcessingWithoutExtractScale(String text) {
		
		List<String> tokens = tokenizing(text, Version.LUCENE_4_9);
		return tokensToString(extractNounsAndAdjectives(tokens));
		
	}
	
	/**
	 * Realiza a extra��o dos substantivos e adjetivos de um texto
	 * sem fazer uso de stem.
	 * @param text O texto a ser pr�-processado.
	 * @return Os substantivos e adjetivos do texto.
	 */
	public String extractNounsAndAdjectivesWithoutStem(String text) {
		
		String textWithouScale = extractScale(text);
		List<String> tokens = tokenizingText(textWithouScale);
		return tokensToString(extractNounsAndAdjectives(tokens));
		
	}
	/**
	 * M�todo para carregar a propriedade que identifica a localiza��o do
	 * TreeTagger no sistema operaciona.
	 */
	private void loadPropertyTreeTagger() {
		try {
			InputStream in = new Main().getClass().getClassLoader().getResourceAsStream("config.properties");  
			Properties props = new Properties();  
			props.load(in);
			in.close();
			pathTreeTagger = props.getProperty("PATH_TREETAGGER");
		} catch (IOException e) {
			System.err.println("N�o foi poss�viel localizar a intala��o do TreeTagger.");
		}
	}
	
	/**
	 * M�todo que tokeniza o texto passado como parametro e retira as stopwords 
	 * e realiza a opera��o de Stemming.
	 * 
	 * @param text O texto a ser tokenizado.
	 * @param luceneVersion A vers�o do lucene.
	 * @return Uma lista contendo o texto tokenizado.
	 */
	private List<String> tokenizing(String text, Version luceneVersion) {

		List<String> tokens = new ArrayList<String>();
		StandardAnalyzer sa = new StandardAnalyzer(luceneVersion);
				
		TokenStream tokenStream = new StandardTokenizer(luceneVersion, new StringReader(text));
		tokenStream = new StopFilter(luceneVersion, tokenStream, sa.getStopwordSet());
		tokenStream = new PorterStemFilter(tokenStream);
		CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

		try {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				tokens.add(token.toString());
			}
			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> wordsWithYFinish = getWordsWithYFinish(text);
		for (String word : wordsWithYFinish) {
			replaceSimilarWordWithYFinish(word, tokens);
		}
		
		return extractWordsDefault(tokens);
	}

	/**
	 * Recupera as palavras de um texto que terminam com y ou ies.
	 * @param text O texto a ser verificado.
	 * @return A lista das palavras que terminam com y ou ies.
	 */
	protected List<String> getWordsWithYFinish(String text) {
		List<String> result = new ArrayList<String>();
		if (text != null) {			
			String[] tokens = text.split(" ");
			for (String token : tokens) {
				if (token.toString().endsWith("ies")) {					
					result.add(token.toString().replace("ies", "y"));
				}
				if (token.toString().endsWith("y")) {
					result.add(token.toString());
				}
			}
		}
		return result;
	}
	
	/**
	 * Substitui uma palavra em uma lista que contenha o mesmo radical de outra palavra com y no final.
	 * @param word A palavra terminada com y cujo radical ser� pesquisado na lista.
	 * @param tokens A lista de palavras que ser� pesquisada.
	 */
	protected void replaceSimilarWordWithYFinish(String word, List<String> tokens) {
		if (word != null && word.length() > 3 && tokens != null) {			
			String radical = word.substring(0, word.length() -2);
			for (int i = 0; i < tokens.size(); i++) {
				if (tokens.get(i).startsWith(radical)) {
					tokens.set(i, word);
				}
			}		
		}
	}
	

	/**
	 * M�todo para extrair os sujeitos e os adjetivos da lista de tokens passada como par�metro.
	 * 
	 * @param tokens Os tokens a serem filtrados.
	 * @param pathTreeTagger O caminho da instala��o do treeTagger.
	 * @return A lista de tokens que s�o sujeitos e adjetivos.
	 */
	public List<String> extractNounsAndAdjectives(List<String> tokens) { 
		final List<String> nounsAndAdjectives = new ArrayList<String>();
		System.setProperty("treetagger.home", pathTreeTagger);
		TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
		try {
			tt.setModel(pathTreeTagger + File.separator + "lib" + File.separator + "english-utf8.par");
			tt.setHandler(new TokenHandler<String>() {
				public void token(String token, String pos, String lemma) {
					if (pos.startsWith("NN") || pos.startsWith("JJ") || pos.startsWith("NP")) {
						nounsAndAdjectives.add(lemma);
					}
				}
			});
			tt.process(tokens);
		} catch (IOException io) {
			System.err.println("N�o foi poss�viel localizar a intala��o do TreeTagger.");
		} catch (TreeTaggerException e) {
			System.err.println("Houve um problema no TreeTagger.");
			e.printStackTrace();
		} finally {
			tt.destroy();
		}
		return nounsAndAdjectives;
	}
	
	/**
	 * Monta uma string com todos os tokens da lista separados por um espa�o.
	 * 
	 * @param tokens Os tokens a serem impressos.
	 * @return Uma string com os tokens da lista.
	 */
	public String tokensToString(Collection<String> tokens) {		
		String tokenString = "";
		for (String token : tokens) {
			tokenString += token + " ";
		}		
		return tokenString.trim();
	}
	
	/**
	 * M�todo que quebra uma palavra que tenha pelo menos uma letra mai�scula no meio em duas ou mais palavras.
	 * Ex.: SnowIce ser� quebrada em Snow e Ice.
	 * @param text O texto a ser quebrado, caso possua um ou mais caracteres mai�sculos no meio.
	 * @return Os tokens separados por espa�o, caso possua um ou mais caracteres mai�sculos no meio.
	 */
	public String tokenizingTextWithUppercase(String text) {
		if(!text.matches("[a-zA-Z][a-z]*[A-Z][a-zA-Z]*")) {
			return text;
		} else {			
			String textReturn = text;
			Set<String> tokens = new LinkedHashSet<String>();
			if (text.length() > 1) {		
				int inicio = 0;
				for(int i = 1; i < text.length(); i++){
					Character charac = text.charAt(i);
					if(Character.isUpperCase(charac)){
						String sFirst = text.substring(inicio, i);
						textReturn = text.substring(i);
						tokens.add(sFirst);
						inicio = i;
					}
				}   
				tokens.add(textReturn);
			}
			return tokensToString(tokens);
		}
	}
	
	/**
	 * Insere os tokens separados por espa�os de um texto em um lista de tokens.
	 * @param text O texto a ser tokenizado.
	 * @return Uma lista de tokens.
	 */
	public List<String> tokenizingText(String text) {
		String[] tokens = text.split(" ");
		List<String> listReturn = new ArrayList<String>();
		for (String token : tokens) {
			listReturn.add(token);
		}
		return listReturn;
	}
	
	/**
	 * Insere os tokens separados por espa�os de uma lista de strings em um conjunto de tokens.
	 * @param text O texto a ser tokenizado.
	 * @return Um conjunto de tokens.
	 */
	public Set<String> tokenizingTextList(List<String> texts) {
		
		Set<String> setReturn = new HashSet<String>();
		for (String text : texts) {
			String[] tokens = text.split(" ");
			for (String token : tokens) {				
				setReturn.add(token);
			}
		}
		return setReturn;
	}

	/**
	 * Extrai as informa��es referentes a escala dos texto passado.
	 * @param text Texto cujas informa��es de escala ser�o extra�das, caso exista.
	 * @return O texto com as informa��es de escala extra�das, caso exista.
	 */
	public String extractScale(String text) {
		String textReturn = text.replaceAll("\\(*[\\d]:([\\d]+[\\D])*[\\d]*\\)*[\\s]*", "");
		textReturn = textReturn.replaceAll("[\\d]+[\\D][\\s]", "");
		return textReturn;
	}

	/**
	 * Extrai palavras referentes aos termos comuns da �rea de dados geogr�ficos.
	 * @param tokens Os tokens cujos termos comuns ser�o extra�dos, caso exista.
	 * @return Os tokens com os termos comuns extra�dos, caso exista.
	 */
	public List<String> extractWordsDefault(List<String> tokens) {
		
		List<String> tokensResult = new ArrayList<String>();
		for (String token : tokens) {
			boolean find = false;
			for (String commonWord : commonWords) {
				if (token.equalsIgnoreCase(commonWord)) {
					find = true;
					break;
				}
			}
			if (!find) {
				tokensResult.add(token);
			}
		}
		return tokensResult;
	}
	
	/**
	 * M�todo para normalizar um determinado texto colocando-o para min�sculo e 
	 * separando as palavras compostas.
	 * 
	 * @param text O texto a ser normalizado.
	 * @return O texto normalizado.
	 */
	public String normalizeText(String text) {

		return tokenizingTextWithUppercase(text).toLowerCase();
	}

}

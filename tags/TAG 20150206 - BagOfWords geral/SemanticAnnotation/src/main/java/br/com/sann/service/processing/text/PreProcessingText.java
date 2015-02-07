package br.com.sann.service.processing.text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
	
	private String pathTreeTagger = "";
	private final String[] commonWords = {"area", "level", "point", "polygon", "line", "zoon"};

	/**
	 * Método construtor.
	 */
	public PreProcessingText() {
		loadPropertyTreeTagger();
	}
	
	/**
	 * Realiza o pré-processamento do texto passado como parâmetro.
	 * @param text O texto a ser pré-processado.
	 * @return O texto pré-processado.
	 */
	public List<String> preProcessing(String text) {
		
		text = extractScale(text);
		List<String> tokens = tokenizing(text, Version.LUCENE_4_9);
		return extractNounsAndAdjectives(tokens);
		
	}
	
	/**
	 * Realiza o pré-processamento do texto passado como parâmetro sem a
	 * extração de escalas.
	 * @param text O texto a ser pré-processado.
	 * @return O texto pré-processado.
	 */
	public String preProcessingWithoutExtractScale(String text) {
		
		List<String> tokens = tokenizing(text, Version.LUCENE_4_9);
		return tokensToString(extractNounsAndAdjectives(tokens));
		
	}
	
	/**
	 * Método para carregar a propriedade que identifica a localização do
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
			System.err.println("Não foi possíviel localizar a intalação do TreeTagger.");
		}
	}
	
	/**
	 * Método que tokeniza o texto passado como parametro e retira as stopwords 
	 * e realiza a operação de Stemming.
	 * 
	 * @param text O texto a ser tokenizado.
	 * @param luceneVersion A versão do lucene.
	 * @return Uma lista contendo o texto tokenizado.
	 */
	private List<String> tokenizing(String text, Version luceneVersion) {

		StandardAnalyzer sa = new StandardAnalyzer(luceneVersion);
		TokenStream tokenStream = new StandardTokenizer(luceneVersion, new StringReader(text));
		List<String> tokens = new LinkedList<String>();
		tokenStream = new StopFilter(luceneVersion, tokenStream, sa.getStopwordSet());
		tokenStream = new PorterStemFilter(tokenStream);

		CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
		try {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				tokens.add(token.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return extractWordsDefault(tokens);
	}

	/**
	 * Método para extrair os sujeitos e os adjetivos da lista de tokens passada como parâmetro.
	 * 
	 * @param tokens Os tokens a serem filtrados.
	 * @param pathTreeTagger O caminho da instalação do treeTagger.
	 * @return A lista de tokens que são sujeitos e adjetivos.
	 */
	public List<String> extractNounsAndAdjectives(List<String> tokens) { 
		final List<String> nounsAndAdjectives = new LinkedList<String>();
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
			System.err.println("Não foi possíviel localizar a intalação do TreeTagger.");
		} catch (TreeTaggerException e) {
			System.err.println("Houve um problema no TreeTagger.");
			e.printStackTrace();
		} finally {
			tt.destroy();
		}
		return nounsAndAdjectives;
	}
	
	/**
	 * Monta uma string com todos os tokens da lista separados por um espaço.
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
	 * Método que quebra uma palavra que tenha pelo menos uma letra maiúscula no meio em duas ou mais palavras.
	 * Ex.: SnowIce será quebrada em Snow e Ice.
	 * @param text O texto a ser quebrado, caso possua um ou mais caracteres maiúsculos no meio.
	 * @return Os tokens separados por espaço, caso possua um ou mais caracteres maiúsculos no meio.
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
	 * Insere os tokens separados por espaços de um texto em um lista de tokens.
	 * @param text O texto a ser tokenizado.
	 * @return Uma lista de tokens.
	 */
	public List<String> tokenizingText(String text) {
		String[] tokens = text.split(" ");
		List<String> listReturn = new LinkedList<String>();
		for (String token : tokens) {
			listReturn.add(token);
		}
		return listReturn;
	}
	
	/**
	 * Insere os tokens separados por espaços de uma lista de strings em um conjunto de tokens.
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
	 * Extrai as informações referentes a escala dos texto passado.
	 * @param text Texto cujas informações de escala serão extraídas, caso exista.
	 * @return O texto com as informações de escala extraídas, caso exista.
	 */
	public String extractScale(String text) {
		String textReturn = text.replaceAll("\\(*[\\d]:([\\d]+[\\D])*[\\d]*\\)*[\\s]*", "");
		textReturn = textReturn.replaceAll("[\\d]+[\\D][\\s]", "");
		return textReturn;
	}

	/**
	 * Extrai palavras referentes aos termos comuns da área de dados geográficos.
	 * @param tokens Os tokens cujos termos comuns serão extraídos, caso exista.
	 * @return Os tokens com os termos comuns extraídos, caso exista.
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
	 * Método para normalizar um determinado texto colocando-o para minúsculo e 
	 * separando as palavras compostas.
	 * 
	 * @param text O texto a ser normalizado.
	 * @return O texto normalizado.
	 */
	public String normalizeText(String text) {

		return tokenizingTextWithUppercase(text).toLowerCase();
	}

}

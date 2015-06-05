package br.com.sann.loader.ontology;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

public class LoaderOntology {

	private static final String PATH_FILE_NT = "C:\\yago_en.nt";
	public static final String PATH_TDB = "E:\\tdb";

	/**
	 * Realiza a carga dos registros RDF de um arquivo em um conjunto de dados
	 * indexado em disco.
	 * 
	 * @param pathFile
	 *            O caminho do arquivo que contêm os registros RDF.
	 * @param pathDisk
	 *            O caminho onde o conjunto de dados indexado será persistido em
	 *            disco.
	 * @param lang
	 *            A extensão do arquivo de registros RDF.
	 * @throws FileNotFoundException
	 *             Exceção lançada quando houver problemas no paths passados.
	 */
	public static void loadOntoloyByFileInDisk(String pathFile, String pathDisk, Lang lang) throws FileNotFoundException {
    	Dataset dataset = TDBFactory.createDataset(pathDisk);   	
		Model dbpedia = dataset.getDefaultModel();
		RDFDataMgr.read(dbpedia, new FileInputStream(pathFile), lang);
	}
	
	/**
	 * Recupera as superclasses de um determinado conceito no conjunto de dados
	 * indexado e persistido em disco.
	 * 
	 * @param pathDisk
	 *            O diretório onde o conjunto de dados indexado está persistido.
	 * @param uri
	 *            A uri do conceito a ser pesquisado.
	 * @return As superclasses do conceito.
	 */
	public static Set<String> getSuperClasses(String uri) {
		Set<String> superClasses = new HashSet<String>();
		Dataset dataset = TDBFactory.createDataset(PATH_TDB);
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getDefaultModel();
		Resource resource = model.getResource(uri);
		StmtIterator stmts = model.listStatements(resource, RDFS.subClassOf, (RDFNode) null);
		while (stmts.hasNext()) {
			Statement stmt = stmts.next();
			superClasses.add(stmt.getObject().toString());
		}
		dataset.end();
		return superClasses;
	}
	
	/**
	 * Recupera os conceitos de um determinado tema no conjunto de dados
	 * indexado e persistido em disco.
	 * 
	 * @param pathDisk
	 *            O diretório onde o conjunto de dados indexado está persistido.
	 * @param thema
	 *            Um tema a ser pesquisado.
	 * @return Os conceitos referentes ao tema pesquisado.
	 */
	public static Set<RDFNode> getConcepts(String thema) {
		Set<RDFNode> superClasses = new HashSet<RDFNode>();
		Dataset dataset = TDBFactory.createDataset(PATH_TDB);
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getDefaultModel();
		
		StmtIterator stmts = model.listStatements((Resource) null, RDFS.label, thema);
		while (stmts.hasNext()) {
			Statement stmt = stmts.next();
			superClasses.add(stmt.getSubject());
		}
		dataset.end();
		return superClasses;
	}
	
	public static void main(String[] args) {

//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name106333653"));
//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name114438788"));
//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name107972279"));
//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name110344443"));
//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name101139636"));
//		System.out.println(getSuperClasses(PATH_TDB, "http://dbpedia.org/class/yago/Name106720964"));
		System.out.println(getSuperClasses("[http://dbpedia.org/class/yago/Group100031264"));
		
//		for(RDFNode node : getConcepts(PATH_TDB, "group")) {
//			System.out.println(getSuperClasses(PATH_TDB, node.toString()));
//		}
		
		
		//TODO Faz a carga de um arquivo NT para o conjunto de dados indexado em disco.
//		try {
//			loadOntoloyByFileInDisk(PATH_FILE_NT, PATH_TDB, Lang.NTRIPLES);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
}

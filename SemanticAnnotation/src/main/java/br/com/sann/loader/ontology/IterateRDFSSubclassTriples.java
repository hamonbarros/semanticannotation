package br.com.sann.loader.ontology;
import java.io.FileNotFoundException;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

public class IterateRDFSSubclassTriples {
    public static void main(String[] args) throws FileNotFoundException {
    	
    	Dataset dataset = TDBFactory.createDataset("E:\\tdb") ;    	
    	//TODO Comentar esta linha quando for carregar os dados em Disco.
		dataset.begin(ReadWrite.READ);
		// Get model inside the transaction
		Model dbpedia = dataset.getDefaultModel();

		//TODO Descomentar esta linha quando for carregar os dados em Disco.
		//  RDFDataMgr.read(dbpedia, new FileInputStream("E:\\yago_en.nt"), Lang.NTRIPLES) ;
        Resource resource = dbpedia.getResource("http://dbpedia.org/class/yago/OrganizationsEstablishedIn1994");
        
        final StmtIterator stmts = dbpedia.listStatements(resource, RDFS.subClassOf, (RDFNode) null);
        while ( stmts.hasNext() ) {
            final Statement stmt = stmts.next();
            System.out.println( stmt.getSubject() + " is a subclass of " + stmt.getObject() );
        }
        dataset.end();
    }
}
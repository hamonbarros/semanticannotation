package br.com.sann.service.ontology;

import java.util.ArrayList;
import java.util.List;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.service.OntologyConceptService;
import br.com.sann.service.impl.OntologyConceptServiceImpl;
import br.com.sann.service.processing.text.PreProcessingText;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OntologyManager {
	
	public static void main(String[] args) {

		try {

			OntologyConceptService service = new OntologyConceptServiceImpl();
			List<OntologyConcept> concepts = new ArrayList<OntologyConcept>();

			PreProcessingText preProcessingText = new PreProcessingText();
			
			String url = "http://www.daml.org/services/owl-s/1.0DL/Process.owl";
			String path = "C:/dbpedia_2014.owl";

			OntologyParser parser = new OntologyParser(path,
					OntologyParser.LOCAL_FILE_MODE, OntologyParser.SIMPLE_MODEL);
			ExtendedIterator<OntClass> imports = parser.getModel()
					.listNamedClasses();
			System.out.println("Concepts");
			int cont = 0;
			while (imports.hasNext()) {
				OntClass conceptClass = imports.next();
				OntologyConcept concept = new OntologyConcept();
				concept.setConcept(conceptClass.getURI());
				concept.setConceptName(conceptClass.getLocalName());
				concept.setNormalizedName(preProcessingText.normalizeText(conceptClass.getLocalName()));
				List<OntologyConcept> persistedConepts = service.recoveryOntolgyConceptByTerm(concept.getConceptName());
				if (persistedConepts == null || persistedConepts.isEmpty()) {
					persistedConepts = service.recoveryOntolgyConceptByTerm(concept.getNormalizedName());
					if (persistedConepts == null || persistedConepts.isEmpty()) {
						concepts.add(concept);
						System.out.println(concept.getConceptName());
						cont++;						
					}
				}
			}
			System.out.println("\nNumber of concepts: " + cont);
			service.saveOntologyConcepts(concepts);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

}

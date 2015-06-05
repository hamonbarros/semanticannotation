package br.com.sann.performance.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SpatialData;

/**
 * Classe para receber as informações referente ao arquivo de teste de desempenho.
 * 
 * @author Hamon
 */
public class ExpectedResult {

	private String title;
	private String annotationType;
	private String bagOfWords;

	private List<OntologyConcept> relevantConcepts;
	private Set<String> retrievedConcepts;
	private Set<String> relevantRetrievedConcepts;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<OntologyConcept> getRelevantConcepts() {
		return relevantConcepts;
	}

	public void setRelevantConcepts(List<OntologyConcept> relevantConcepts) {
		this.relevantConcepts = relevantConcepts;
	}

	public String getBagOfWords() {
		return bagOfWords;
	}

	public void setBagOfWords(String bagOfWords) {
		this.bagOfWords = bagOfWords;
	}

	public Set<String> getRetrievedConcepts() {
		return retrievedConcepts;
	}

	public void setRetrievedConcepts(Set<String> retrievedConcepts) {
		this.retrievedConcepts = retrievedConcepts;
	}

	public Set<String> getRelevantRetrievedConcepts() {
		return relevantRetrievedConcepts;
	}

	public void setRelevantRetrievedConcepts(Set<String> relevantRetrievedConcepts) {
		this.relevantRetrievedConcepts = relevantRetrievedConcepts;
	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}
	
	public void convertAnnotationType(Integer typeAnnotation) {
		if (typeAnnotation != null && typeAnnotation.equals(SpatialData.SANN_LOD)) {
			setAnnotationType("Anotação via LOD");
		} else if (typeAnnotation != null && typeAnnotation.equals(SpatialData.SANN_WORDS)) {
			setAnnotationType("Anotação via Casamento de Strings");
		} else {
			setAnnotationType("Não anotado");
		}
	}

	public List<String> getConcepts(List<OntologyConcept> ontologyConcepts) {
		
		List<String> concepts = new ArrayList<String>();
		for (OntologyConcept concept : ontologyConcepts) {
			concepts.add(concept.getConcept());
		}
		return concepts;
	}
	
	public List<String> getConceptNames(List<OntologyConcept> ontologyConcepts) {
		
		List<String> concepts = new ArrayList<String>();
		for (OntologyConcept concept : ontologyConcepts) {
			concepts.add(concept.getConceptName() + "-" + concept.getId());
		}
		return concepts;
	}
	
	public String toString() {
		String text = "Título: " + title + "\n";
		if (relevantConcepts != null) {			
			text += "Conceitos relevantes: " + getConcepts(relevantConcepts) + "\n";
		}
		if (retrievedConcepts != null) {
			text += "Conceitos recuperados: " + retrievedConcepts + "\n";
		}
		if (relevantRetrievedConcepts != null) {
			text += "Conceitos relevantes recuperados: " + relevantRetrievedConcepts + "\n";
		}
		return text;
	}
}

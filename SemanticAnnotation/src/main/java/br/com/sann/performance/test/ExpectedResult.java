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
	private SpatialData spatialData;

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

	public SpatialData getSpatialData() {
		return spatialData;
	}
	
	public void setSpatialData(SpatialData spatialData) {
		this.spatialData = spatialData;
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

	public List<String> getConcepts(List<OntologyConcept> ontologyConcepts) {
		
		List<String> concepts = new ArrayList<String>();
		for (OntologyConcept concept : ontologyConcepts) {
			concepts.add(concept.getConcept());
		}
		return concepts;
	}
	
	public String toString() {
		String text = "Título: " + title + "\n";
		if (spatialData != null) {
			text += "Feature type: '" + spatialData.getTitle() + "' Nome: '" + spatialData.getName() + "'" + "\n";
		}
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

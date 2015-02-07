package br.com.sann.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsável por armazenar as informações semânticas extraídas
 * a partir de um título/token.
 * 
 * @author Hamon
 */
public class Extractor {

	private String title;
	private Set<String> classes;
	private Set<String> categories;
	private Set<String> similarityClasses;
	private Set<String> similarityCategories;
	private Set<OntologyConcept> ontologyClasses;
	private Set<OntologyConcept> ontologyCategories;
	
	public Extractor() {
		
		classes = new HashSet<String>();
		categories = new HashSet<String>();
		similarityCategories = new HashSet<String>();;
		similarityClasses = new HashSet<String>();
		ontologyCategories = new HashSet<OntologyConcept>();
		ontologyClasses = new HashSet<OntologyConcept>();
		
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<String> getClasses() {
		return classes;
	}
	public void setClasses(Set<String> classes) {
		this.classes = classes;
	}
	public Set<String> getCategories() {
		return categories;
	}
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	public Set<String> getSimilarityClasses() {
		return similarityClasses;
	}
	public void setSimilarityClasses(Set<String> similarityClasses) {
		this.similarityClasses = similarityClasses;
	}
	public Set<String> getSimilarityCategories() {
		return similarityCategories;
	}
	public void setSimilarityCategories(Set<String> similarityCategories) {
		this.similarityCategories = similarityCategories;
	}
	public Set<OntologyConcept> getOntologyClasses() {
		return ontologyClasses;
	}
	public void setOntologyClasses(Set<OntologyConcept> ontologyClasses) {
		this.ontologyClasses = ontologyClasses;
	}
	public Set<OntologyConcept> getOntologyCategories() {
		return ontologyCategories;
	}
	public void setOntologyCategories(Set<OntologyConcept> ontologyCategories) {
		this.ontologyCategories = ontologyCategories;
	}
	
}

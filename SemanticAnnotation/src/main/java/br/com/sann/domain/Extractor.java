package br.com.sann.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe respons�vel por armazenar as informa��es sem�nticas extra�das
 * a partir de um t�tulo/token.
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
	private Map<String, List<String>> classesAndCategoriesURL;
	
	public Extractor() {
		
		classes = new HashSet<String>();
		categories = new HashSet<String>();
		similarityCategories = new HashSet<String>();;
		similarityClasses = new HashSet<String>();
		ontologyCategories = new HashSet<OntologyConcept>();
		ontologyClasses = new HashSet<OntologyConcept>();
		classesAndCategoriesURL = new HashMap<String, List<String>>();
		
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
	
	public Map<String, List<String>> getClassesAndCategoriesURL() {
		return classesAndCategoriesURL;
	}

	public void setClassesAndCategoriesURL(Map<String, List<String>> classesAndCategoriesURL) {
		this.classesAndCategoriesURL = classesAndCategoriesURL;
	}
	
	public void putAllURLs(Map<String, List<String>> labelAndURLConcepts) {
		this.classesAndCategoriesURL.putAll(labelAndURLConcepts);
	}
	
	public void putExistingURLs(Map<String, List<String>> labelAndURLConcepts) {
		for (String label : labelAndURLConcepts.keySet()) {			
			List<String> urls = classesAndCategoriesURL.get(label);
			if (urls == null) {
				urls = new ArrayList<String>();
			}
			urls.addAll(labelAndURLConcepts.get(label));
			classesAndCategoriesURL.put(label, urls);
		}			
	}
	
	public Set<String> getClassesAndCategories() {
		Set<String> result = new HashSet<String>();
		if (classes != null) {
			result.addAll(classes);
		}
		if (categories != null) {
			result.addAll(categories);
		}
		return result;
	}
	
}

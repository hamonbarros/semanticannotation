package br.com.sann.domain;

/**
 * Classe para medir o desempenho da descoberta de conceitos.
 * 
 * @author Hamon
 *
 */
public class Sumary {

	private Integer countConcepts = 0;
	private Integer countConceptsSimilarity = 0;
	private Integer countConceptsAnnotated = 0;
	private Integer countCategories = 0;
	private Integer countCategoriesSimilarity = 0;
	private Integer countCategoriesAnnotated = 0;
	private Integer countFeature = 0;
	private Integer countFeatureNotAnnotated = 0;
	
	public Integer getCountConcepts() {
		return countConcepts;
	}
	
	public void setCountConcepts(Integer countConcepts) {
		this.countConcepts += countConcepts;
	}
	
	public Integer getCountConceptsSimilarity() {
		return countConceptsSimilarity;
	}
	
	public void setCountConceptsSimilarity(Integer countConceptsSimilarity) {
		this.countConceptsSimilarity += countConceptsSimilarity;
	}
	
	public Integer getCountCategories() {
		return countCategories;
	}
	
	public void setCountCategories(Integer countCategories) {
		this.countCategories += countCategories;
	}
	
	public Integer getCountCategoriesSimilarity() {
		return countCategoriesSimilarity;
	}
	
	public void setCountCategoriesSimilarity(Integer countCategoriesSimilarity) {
		this.countCategoriesSimilarity += countCategoriesSimilarity;
	}
	
	public Integer getCountFeatureNotAnnotated() {
		return countFeatureNotAnnotated;
	}
	
	public void setCountFeatureNotAnnotated(Integer countFeatureNotAnnotated) {
		this.countFeatureNotAnnotated += countFeatureNotAnnotated;
	}

	public Integer getCountConceptsAnnotated() {
		return countConceptsAnnotated;
	}

	public void setCountConceptsAnnotated(Integer countConceptsAnnotated) {
		this.countConceptsAnnotated += countConceptsAnnotated;
	}

	public Integer getCountCategoriesAnnotated() {
		return countCategoriesAnnotated;
	}

	public void setCountCategoriesAnnotated(Integer countCategoriesAnnotated) {
		this.countCategoriesAnnotated += countCategoriesAnnotated;
	}

	public Integer getCountFeature() {
		return countFeature;
	}

	public void setCountFeature(Integer countFeature) {
		this.countFeature += countFeature;
	}
	
	/**
	 * Método para possibilitar a sumarização consolidada dos resultados.
	 * 
	 * @param sumary A classe sumary.
	 * @param extractor A classe extratora.
	 */
	public void summarizeResults(Extractor extractor) {
		
		if (extractor != null) {
			setCountCategories(extractor.getCategories().size());
			setCountCategoriesSimilarity(extractor.getSimilarityCategories().size());
			setCountCategoriesAnnotated(extractor.getOntologyCategories().size());
			setCountConcepts(extractor.getClasses().size());
			setCountConceptsSimilarity(extractor.getSimilarityClasses().size());
			setCountConceptsAnnotated(extractor.getOntologyClasses().size());
		}
		
	}
	
	/**
	 * Método to String.
	 */
	public String toString() {
		
		String textResult = "--------------------- SUMÁRIO ---------------------\n";
		textResult += "Quantidade de Títulos de Feature Types: " + countFeature + "\n";
		textResult += "Quantidade de Títulos não anotados:     " + countFeatureNotAnnotated + "\n";
		textResult += "Quantidade de conceitos:                " + countConcepts + "\n";
		textResult += "Quantidade de conceitos similares:      " + countConceptsSimilarity + "\n";
		textResult += "Quantidade de conceitos anotados:       " + countConceptsAnnotated + "\n";
		textResult += "Quantidade de categorias:               " + countCategories + "\n";
		textResult += "Quantidade de categorias similares:     " + countCategoriesSimilarity + "\n";
		textResult += "Quantidade de categorias anotadas:      " + countCategoriesAnnotated + "\n";
		
		return textResult;
	}

}

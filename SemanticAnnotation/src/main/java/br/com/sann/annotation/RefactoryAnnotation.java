package br.com.sann.annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.domain.SpatialData;
import br.com.sann.loader.ontology.LoaderOntology;
import br.com.sann.service.AttributeSpatialDataService;
import br.com.sann.service.FeatureService;
import br.com.sann.service.SemanticAnnotationAttrService;
import br.com.sann.service.SemanticAnnotationService;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationAttrServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationServiceImpl;

public class RefactoryAnnotation {
	
	private static final int COUNT_MAX_OCURRENCES = 5;
	public static Logger log;
	
	/**
	 * Monta um mapa contendo o nome do conceito e lista de URLs relacionadas a
	 * ele.
	 * 
	 * @param sannFTs
	 *            A lista de anotações semânticas do feature type.
	 * @return O mapa de conceitos de suas respectivas URLs.
	 */
	private static Map<String, List<String>> mountOccurrences(List<SemanticAnnotation> sannFTs) {
		Map<String, List<String>> ocurrences = new HashMap<String, List<String>>();
		for (SemanticAnnotation sann : sannFTs) {
			String conceptName = sann.getConceptid().getConceptName();
			String concept = sann.getConceptid().getConcept();
			List<String> concepts = ocurrences.get(conceptName);
			if (concepts == null) {
				List<String> urls = new ArrayList<String>();
				urls.add(concept);
				ocurrences.put(conceptName, urls);
			} else {
				concepts.add(concept);
			}
		}
		return ocurrences;
	}
	
	/**
	 * Monta um mapa contendo o nome do conceito e lista de URLs relacionadas a
	 * ele.
	 * 
	 * @param sannAttrs
	 *            A lista de anotações semânticas do atributos.
	 * @return O mapa de conceitos de suas respectivas URLs.
	 */
	private static Map<String, List<String>> mountOccurrencesAttrs(List<SemanticAnnotationAttribute> sannAttrs) {
		Map<String, List<String>> ocurrences = new HashMap<String, List<String>>();
		for (SemanticAnnotationAttribute sannAttr : sannAttrs) {
			String conceptName = sannAttr.getOntologyConcept().getConceptName();
			String concept = sannAttr.getOntologyConcept().getConcept();
			List<String> concepts = ocurrences.get(conceptName);
			if (concepts == null) {
				List<String> urls = new ArrayList<String>();
				urls.add(concept);
				ocurrences.put(conceptName, urls);
			} else {
				concepts.add(concept);
			}
		}
		return ocurrences;
	}

	/**
	 * Procura os conceitos gerais das URLs que estão relacionadas com o mesmo
	 * nome. Para cada superclasse são associados os conceitos relacionados. 
	 * 
	 * @param equalsConcepts
	 *            A lista de URLs relacionadas com o mesmo nome.
	 * @return O conjunto de URLs dos conceitos mais genericos.
	 */
	private static Map<String, Set<String>> findSuperClasses(List<String> equalsConcepts) {
		Map<String, Set<String>> mapSuperClasses = new HashMap<String, Set<String>>();
		for (String concept : equalsConcepts) {
			Set<String> superClasses = LoaderOntology.getSuperClasses(concept);
			for (String superClass : superClasses) {
				if (mapSuperClasses.get(superClass) == null) {
					Set<String> concepts = new HashSet<String>();
					concepts.add(concept);
					mapSuperClasses.put(superClass, concepts);
				} else {
					mapSuperClasses.get(superClass).add(concept);
				}
			}
		}
		return mapSuperClasses;
	}
	
	/**
	 * Verifica a existência de alguma superclasse entre os conceitos.
	 * @param equalsConcepts O conceitos a serem verificados.
	 * @param mapSuperClasses O mapa das superclasses com os respectivos conceitos.
	 * @return As superclasses que estão contidas nos conceitos.
	 */
	private static Set<String> findSuperClassesInConcepts(List<String> equalsConcepts, 
		Map<String, Set<String>> mapSuperClasses) {
		Set<String> result = new HashSet<String>();
		for (String concept : equalsConcepts) {
			for (String superClass : mapSuperClasses.keySet()) {
				if (superClass.equals(concept)) {
					result.add(superClass);
				}
			}
		}
		return result;
	}
	
	/**
	 * Verifica a existência de alguma superclasse com mais de um conceito associado.
	 * @param mapSuperClasses O mapa das superclasses com os respectivos conceitos.
	 * @return As superclasses com mais de um conceito associado.
	 */
	private static Set<String> findSuperClassesWithConcepts(Map<String, Set<String>> mapSuperClasses) {
		Set<String> result = new HashSet<String>();
		for (String superClass : mapSuperClasses.keySet()) {
			if (mapSuperClasses.get(superClass).size() > 1) {
				result.add(superClass);
			}
		}
		return result;
	}
	
	/**
	 * Extrai o nome de um conceito a partir da URI.
	 * @param concept A URI do conceito.
	 * @return O nome de um conceito.
	 */
	private static String extractNameConcept(String concept) {
		String[] tokens = concept.split("/");
		return tokens[tokens.length-1];
	}
	
	/**
	 * Verifica se entre os conceitos existe algum com o nome constituído do 
	 * label + s ou do label + números.
	 * 
	 * @param label O label que corresponde ao nome do conceito.
	 * @param equalsConcepts As URIs dos conceitos com o mesmo label.
	 * @return Os conceitos cujo nome é constituído do label + s, caso não haja nenhum, são
	 * retornados os conceitos cujo nome é constituído do label + números.
	 */
	private static Set<String> findSpecificConcepts(String label, List<String> equalsConcepts) {
		Set<String> labelWithS = new HashSet<String>();
		Set<String> labelWithNumber = new HashSet<String>();
		for (String concept : equalsConcepts) {			
			if (extractNameConcept(concept).equalsIgnoreCase(label+"s")) {
				labelWithS.add(concept);
			} else if (extractNameConcept(concept).matches("(?i)" + label + "[0-9]*")) {
				labelWithNumber.add(concept);
			}
		}
		if (!labelWithS.isEmpty()) {
			return labelWithS;
		}
		return labelWithNumber;
	}
	
	/**
	 * Extrai os conceitos mais genéricos dos conceitos que possuem mais de uma URI para o mesmo label.
	 * @param label O nome do conceito.
	 * @param equalsConcepts As URIs dos conceitos com o mesmo label.
	 * @return Os conceitos mais genéricos.
	 */
	public static Set<String> extractGenericConcepts(String label, List<String> equalsConcepts) {
		Set<String> genericConcepts = new HashSet<String>();
		Map<String, Set<String>> mapSuperClasses = findSuperClasses(equalsConcepts);
		genericConcepts.addAll(findSuperClassesInConcepts(equalsConcepts, mapSuperClasses));
		if (genericConcepts.isEmpty()) {
			genericConcepts.addAll(findSuperClassesWithConcepts(mapSuperClasses));
			if (genericConcepts.isEmpty()) {
				genericConcepts.addAll(findSpecificConcepts(label, equalsConcepts));
			}
		}
		return genericConcepts;
	}
	
	public static void main(String[] args) {

		FeatureService service = new FeatureServiceImpl();
		SemanticAnnotationService sannService = new SemanticAnnotationServiceImpl();
		SemanticAnnotationAttrService sannAttrService = new SemanticAnnotationAttrServiceImpl();
		AttributeSpatialDataService attrService = new AttributeSpatialDataServiceImpl();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		log = Logger.getLogger(RefactoryAnnotation.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));		
		log.info("["+ df.format(new Date()) + "] "+ "Realizando a leitura das feature types... ");
		List<SpatialData> spatialDataList = service.recoverAllSpatialData();
//		String ids = "7180";
//		List<SpatialData> spatialDataList = service.recoverySpatialDataByIDs(ids);
		log.info("["+ df.format(new Date()) + "] "+ "Fim da leitura das feature types... ");
		

		PrintWriter refactorySann = null;
		try {
			DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
			String dateFormated = dformat.format(new Date());

			refactorySann = new PrintWriter(new FileWriter(new File("RefactorySann" + dateFormated + ".txt")));
			for (SpatialData featureType : spatialDataList) {
				List<SemanticAnnotation> sannFTs = sannService.recoveryAnnotations(featureType.getId());
				if (!sannFTs.isEmpty()) {					
					refactorySann.println("###############################################################################");
					refactorySann.println("FEATURE TYPE: " + featureType.getTitle());
					Map<String, List<String>> ocurrences = mountOccurrences(sannFTs);
					Set<String> generalConcepts = new HashSet<String>();
					for (String conceptName : ocurrences.keySet()) {
						refactorySann.println();
						refactorySann.println("   NOME DO CONCEITO: " + conceptName);
						refactorySann.println("   OCORRENCIAS: " + ocurrences.get(conceptName).size());
						refactorySann.println("   URLS: " + ocurrences.get(conceptName));
						if (ocurrences.get(conceptName).size() >= COUNT_MAX_OCURRENCES) {
							Set<String> generalConceptsLabel = extractGenericConcepts(conceptName, ocurrences.get(conceptName));
							generalConcepts.addAll(generalConceptsLabel);
							refactorySann.println("   OCORRENCIAS CONCEITOS GERAIS: " + generalConceptsLabel.size());
							refactorySann.println("   URLS CONCEITOS GERAIS: " + generalConceptsLabel);		
						}
					}
					if (!generalConcepts.isEmpty()) {
						sannService.removeAnnotations(featureType.getId());
						sannService.saveSemanticAnnotations(featureType, generalConcepts);
					}
					refactorySann.flush();
				}
				List<AttributeSpatialData> attrs = attrService.recoverAttributesBySpatialData(featureType.getId());
				if (!attrs.isEmpty()) {
					for (AttributeSpatialData attr : attrs) {
						List<SemanticAnnotationAttribute> sannAttrs = sannAttrService.recoveryAnnotations(attr.getId());
						Map<String, List<String>> ocurrences = mountOccurrencesAttrs(sannAttrs);
						for (String conceptName : ocurrences.keySet()) {
							refactorySann.println();
							refactorySann.println("   	NOME DO ATRIBUTO: " + conceptName);
							refactorySann.println("   	OCORRENCIAS: " + ocurrences.get(conceptName).size());
							refactorySann.println("   	URLS: " + ocurrences.get(conceptName));
							if (ocurrences.get(conceptName).size() >= COUNT_MAX_OCURRENCES) {
								Set<String> generalConceps = extractGenericConcepts(conceptName, ocurrences.get(conceptName));
								refactorySann.println("   	OCORRENCIAS CONCEITOS GERAIS: " + generalConceps.size());
								refactorySann.println("   	URLS CONCEITOS GERAIS: " + generalConceps);		
								if (!generalConceps.isEmpty()) {
									sannAttrService.removeAnnotations(attr.getId());
									sannAttrService.saveSemanticAnnotations(attr, generalConceps);
								}
							}
						}			
						refactorySann.flush();
					}					
				}
			}
		
			refactorySann.flush();
			refactorySann.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("Fim do processamento!");
		log.info("Data Final: " + df.format(new Date()));
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

	}


}

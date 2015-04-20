package br.com.sann.performance.test;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.SemanticAnnotation;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.domain.SpatialData;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.impl.FeatureServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationAttrServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationServiceImpl;

public class ExtractResultAnnotation {
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Início da extração dos resultados.");
		DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
		String dateFormated = dformat.format(new Date());

		PrintWriter resultSann = new PrintWriter(new FileWriter(
				new File("Resultado_anotacao_semantica_" + dateFormated + ".txt")));
		
		FeatureServiceImpl featureService = new FeatureServiceImpl();
		AttributeSpatialDataServiceImpl attributeService = new AttributeSpatialDataServiceImpl();
		SemanticAnnotationServiceImpl sannService = new SemanticAnnotationServiceImpl();
		SemanticAnnotationAttrServiceImpl sannAttrService = new SemanticAnnotationAttrServiceImpl();
		
		Integer countFTAnnotated = 0;
		Integer countFTNotAnnotated = 0;
		Integer countAttAnnotated = 0;
		Integer countAttNotAnnotated = 0;
		List<SpatialData> features = featureService.recoverAllSpatialData();
		for (SpatialData feature : features) {
			resultSann.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			resultSann.println("FEATURE TYPE: " + feature.getTitle());
			System.out.println("FEATURE TYPE: " + feature.getTitle());
			resultSann.println("CONCEITOS ANOTADOS PARA O FEATURE TYPE:");
			List<SemanticAnnotation> sanns = sannService.recoveryAnnotations(feature.getId());
			if (sanns == null || sanns.isEmpty()) {
				countFTNotAnnotated++;
			} else {
				countFTAnnotated++;
			}
			for (SemanticAnnotation sann : sanns) {
				resultSann.println(" * " + sann.getConceptid().getConceptName() + " - " + sann.getConceptid().getConcept());
			}
			List<AttributeSpatialData> attributes = attributeService.recoverAttributesBySpatialData(feature.getId());
			resultSann.println("ATRIBUTOS:");		
			System.out.println("LENDO ATRIBUTOS...");
			for (AttributeSpatialData attribute : attributes) {
				List<SemanticAnnotationAttribute> sannAttrs = sannAttrService.recoveryAnnotations(attribute.getId());
				if (sannAttrs == null || sannAttrs.isEmpty()) {
					countAttNotAnnotated++;
				} else {
					countAttAnnotated++;
				}
				String concepts = "";
				for (SemanticAnnotationAttribute sannAttr : sannAttrs) {
					concepts += sannAttr.getOntologyConcept().getConceptName() + ", ";
				}
				if (!concepts.isEmpty()) {					
					concepts = concepts.substring(0, concepts.length() - 2);
				}
				resultSann.println(" # " + attribute.getName() + " - Conceitos anotados: " + concepts);
			}
			resultSann.flush();
		}
		resultSann.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		resultSann.println();
		resultSann.println("CONCEITOS ANOTADOS: " + countFTAnnotated);
		resultSann.println("CONCEITOS NÃO ANOTADOS: "+ countFTNotAnnotated);
		resultSann.println("ATRIBUTOS ANOTADOS: " + countAttAnnotated);
		resultSann.println("ATRIBUTOS NÃO ANOTADOS: " + countAttNotAnnotated);
		resultSann.flush();
		resultSann.close();
		System.out.println("Fim da extração dos resultados.");
		
	}
}

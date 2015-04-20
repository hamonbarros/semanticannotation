package br.com.sann.attribute.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.main.Main;
import br.com.sann.process.ParentProcess;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationAttrServiceImpl;

public class AttributeListProcess extends ParentProcess{
	
	public void execute(List<AttributeSpatialData> attributesList) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		Logger log = Logger.getLogger(Main.class);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info("Iniciando o processamento...");
		log.info("Data Inicial: " + df.format(new Date()));
		SemanticAnnotationAttrServiceImpl sannAttrService = new SemanticAnnotationAttrServiceImpl();
		AttributeSpatialDataServiceImpl attributeServiceService = new AttributeSpatialDataServiceImpl();
		
		try {			
			int countGC = 0;
			for (AttributeSpatialData attribute : attributesList) {
				log.info("[INICIO] Início do processamento para o atributo: " + attribute.getName());
				Set<OntologyConcept> concepts = getSimilaryConcepts(attribute);
				if (!concepts.isEmpty()) {						
					Set<SemanticAnnotationAttribute> sanns = setSemanticAnnotationAttr(
							attribute, concepts);
					sannAttrService.saveSemanticAnnotationsAttrs(sanns);
					attribute.setAnnotated(true);
					attributeServiceService.updateAttributeService(attribute);
				}					
				log.info("[FIM] Fim do processamento para o atributo: " + attribute.getName());
					
				countGC++;
				if (countGC == 25) {
					System.gc();
					countGC = 0;
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("Fim do processamento!");
		log.info("Data Final: " + df.format(new Date()));
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

	}	
	
}

	
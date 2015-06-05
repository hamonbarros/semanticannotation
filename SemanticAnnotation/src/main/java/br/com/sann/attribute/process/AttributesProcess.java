package br.com.sann.attribute.process;

import java.util.List;
import java.util.Set;

import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.domain.OntologyConcept;
import br.com.sann.domain.SemanticAnnotationAttribute;
import br.com.sann.process.ParentProcess;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;
import br.com.sann.service.impl.SemanticAnnotationAttrServiceImpl;

public class AttributesProcess extends ParentProcess{
	
	public void execute(List<AttributeSpatialData> attributesList) {
		
		SemanticAnnotationAttrServiceImpl sannAttrService = new SemanticAnnotationAttrServiceImpl();
		AttributeSpatialDataServiceImpl attributeServiceService = new AttributeSpatialDataServiceImpl();
		
		try {			
			int countGC = 0;
			for (AttributeSpatialData attribute : attributesList) {
				Set<OntologyConcept> concepts = getSimilaryConcepts(attribute);
				if (!concepts.isEmpty()) {						
					Set<SemanticAnnotationAttribute> sanns = setSemanticAnnotationAttr(
							attribute, concepts);
					sannAttrService.saveSemanticAnnotationsAttrs(sanns);
					attribute.setAnnotated(true);
					attributeServiceService.updateAttributeService(attribute);
				}										
				countGC++;
				if (countGC == 25) {
					System.gc();
					countGC = 0;
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
}

	
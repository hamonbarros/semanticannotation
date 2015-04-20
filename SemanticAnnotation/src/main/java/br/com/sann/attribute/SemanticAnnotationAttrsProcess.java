package br.com.sann.attribute;

import java.util.List;

import br.com.sann.attribute.process.AttributeListProcess;
import br.com.sann.domain.AttributeSpatialData;
import br.com.sann.service.impl.AttributeSpatialDataServiceImpl;

public class SemanticAnnotationAttrsProcess {

	public static void main(String[] args) {
		// Lista os atributos persistidos
		AttributeSpatialDataServiceImpl attributeSDService = new AttributeSpatialDataServiceImpl();
		List<AttributeSpatialData> attributes = attributeSDService.recoverAllAttributes(); 
		
		// Pesquisa os conceitos da ontologia que representam os atributos
		AttributeListProcess process = new AttributeListProcess();
		process.execute(attributes);
		
	}
}

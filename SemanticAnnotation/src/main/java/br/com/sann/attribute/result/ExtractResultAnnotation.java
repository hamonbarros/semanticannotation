package br.com.sann.attribute.result;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.sann.service.impl.SemanticAnnotationAttrServiceImpl;

public class ExtractResultAnnotation {
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Início da extração dos resultados.");
		DateFormat dformat = new SimpleDateFormat("YYYYMMddHHmm");
		String dateFormated = dformat.format(new Date());

		PrintWriter resultSannAttr = new PrintWriter(new FileWriter(
				new File("Resultado_anotacao_semantica_atributo" + dateFormated + ".csv")));
		resultSannAttr.println("NOME_ATRIBUTO,NOME_CONCEITO_NORMALIZADO,NOME_CONCEITO,CONCEITO");
		
		
		SemanticAnnotationAttrServiceImpl sannAttrService = new SemanticAnnotationAttrServiceImpl();
		String query = " SELECT a.name, o.normalizedName, o.conceptName, o.concept " 
					 + " FROM SemanticAnnotationAttribute sa, OntologyConcept o, AttributeService a "
					 + " WHERE sa.ontologyConcept.id = o.id "
					 + "	AND sa.attributeService.id = a.id"
					 + " GROUP BY a.name, o.normalizedName, o.conceptName, o.concept ";
		List<Object[]> annotatios = sannAttrService.executeQuery(query);
		int count = 0;
		for (Object[] annotation : annotatios) {
			resultSannAttr.println(annotation[0] + "," + annotation[1] + "," + annotation[2] + "," + annotation[3]);
			count++;
			if (count > 50) {
				resultSannAttr.flush();
				count = 0;
			}
		}
		resultSannAttr.flush();
		resultSannAttr.close();
		System.out.println("Fim da extração dos resultados.");
		
	}
}

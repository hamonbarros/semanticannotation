package br.com.sann.process;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import br.com.sann.domain.OntologyConcept;
import br.com.sann.performance.test.ExpectedResult;
import br.com.sann.service.processing.text.BagOfWords;

public class ExpectedResultProcess extends ParentProcess {

	public void execute(ExpectedResult eResult) {
		
		try {
			Set<String> retrievedConcepts = executeSimilarity(eResult.getTitle(),
					eResult.getBagOfWords());
			eResult.setRetrievedConcepts(retrievedConcepts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

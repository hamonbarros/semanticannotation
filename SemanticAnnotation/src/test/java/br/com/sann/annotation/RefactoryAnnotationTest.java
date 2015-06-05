package br.com.sann.annotation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class RefactoryAnnotationTest extends TestCase {

	public void testFindSuperClassesInConcepts() {
		List<String> equalsConcepts = new LinkedList<String>();
		equalsConcepts.add("http://dbpedia.org/class/yago/OceanBasins");
		equalsConcepts.add("http://dbpedia.org/class/yago/SedimentaryBasins");
		equalsConcepts.add("http://dbpedia.org/class/yago/Basin102801525");
		equalsConcepts.add("http://dbpedia.org/class/yago/Basin113765086");
		equalsConcepts.add("http://dbpedia.org/class/yago/Basin109215437");
		equalsConcepts.add("http://dbpedia.org/class/yago/RiverBasin108518940");
		equalsConcepts.add("http://dbpedia.org/class/yago/Washbasin104553920");
		equalsConcepts.add("http://dbpedia.org/class/yago/AnoxicBasins");
		equalsConcepts.add("http://dbpedia.org/class/yago/CatalanCoastalBasins");
		equalsConcepts.add("http://dbpedia.org/class/yago/EndorheicBasins");
		String label = "basin";
		String extractedConcept = "http://dbpedia.org/class/yago/Basin102801525";
		Set<String> extractedConcepts = new HashSet<String>();
		extractedConcepts.add(extractedConcept);
		assertEquals(extractedConcepts, RefactoryAnnotation.extractGenericConcepts(label, equalsConcepts));		
	}
	
	public void testFindSuperClassesInConcepts2() {
		List<String> equalsConcepts = new LinkedList<String>();
		equalsConcepts.add("http://dbpedia.org/class/yago/GladiatorTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/ComicsTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/JunctionTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/HeraTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/HousingTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/SoundSynthesisTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/ShipTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/BoatTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/SailboatTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/StarTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/DogTypes");
		equalsConcepts.add("http://dbpedia.org/class/yago/Type106825120");
		equalsConcepts.add("http://dbpedia.org/class/yago/Type108111419");
		equalsConcepts.add("http://dbpedia.org/class/yago/Type105840188");
		String label = "type";
		Set<String> extractedConcepts = new HashSet<String>();
		extractedConcepts.add("http://dbpedia.org/class/yago/Type105840188");
		assertEquals(extractedConcepts, RefactoryAnnotation.extractGenericConcepts(label, equalsConcepts));		
	}
	
	public void testFindSuperClassesInConcepts3() {
		List<String> equalsConcepts = new LinkedList<String>();
		equalsConcepts.add("http://dbpedia.org/class/yago/ChibaLotteMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/Marine110294318");
		equalsConcepts.add("http://dbpedia.org/class/yago/UnitedStatesMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/PuertoRicanMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/SpaceMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/HonoraryUnitedStatesMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/RoyalMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/Marines");
		equalsConcepts.add("http://dbpedia.org/class/yago/ContinentalMarines");
		equalsConcepts.add("http://dbpedia.org/class/yago/UnionMarines");
		String label = "marine";
		Set<String> extractedConcepts = new HashSet<String>();
		extractedConcepts.add("http://dbpedia.org/class/yago/Marine110294318");
		assertEquals(extractedConcepts, RefactoryAnnotation.extractGenericConcepts(label, equalsConcepts));		
	}
	
	public void testFindSuperClassesWithConcepts() {
		List<String> equalsConcepts = new LinkedList<String>();
		equalsConcepts.add("http://dbpedia.org/class/yago/NiagaraRegionalRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/PeelRegionalRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/MalaysianFederalRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/Pre-1945FloridaStateRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/RomanRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/DrovingRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/TollRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/Non-freewayTollRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/OttawaRoads");
		equalsConcepts.add("http://dbpedia.org/class/yago/DelhiRoads");
		String label = "road";
		Set<String> extractedConcepts = new HashSet<String>();
		extractedConcepts.add("http://dbpedia.org/class/yago/Road104096066");
		assertEquals(extractedConcepts, RefactoryAnnotation.extractGenericConcepts(label, equalsConcepts));		
	}
	
	public void testFindConceptsWithNumber() {
		List<String> equalsConcepts = new LinkedList<String>();
		equalsConcepts.add("http://dbpedia.org/class/yago/WikiProjectPeerReviews");
		equalsConcepts.add("http://dbpedia.org/class/yago/BookReviews");
		equalsConcepts.add("http://dbpedia.org/class/yago/Review106597758");
		equalsConcepts.add("http://dbpedia.org/class/yago/Review101197258");
		equalsConcepts.add("http://dbpedia.org/class/yago/Inspection100879271");
		String label = "review";
		Set<String> extractedConcepts = new HashSet<String>();
		extractedConcepts.add("http://dbpedia.org/class/yago/Review106597758");
		extractedConcepts.add("http://dbpedia.org/class/yago/Review101197258");
		assertEquals(extractedConcepts, RefactoryAnnotation.extractGenericConcepts(label, equalsConcepts));		
	}
	
}

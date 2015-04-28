package br.com.sann.service.ontology;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import br.com.sann.domain.OntologyConcept;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.ConversionException;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * Classe respons�vel por realizar a leitura de uma ontologia.
 * 
 * @author Fabio
 * @author Hamon
 */
public class OntologyParser {

	public static final int LOCAL_FILE_MODE = 0;
	public static final int HTTP_URL_MODE = 1;
	public static final int SIMPLE_MODEL = 0;
	public static final int INFERENCE_MODEL = 1;

	private String ontologyPath;
	private OntModel model;
	private int mode;

	public OntologyParser(String ontologyPath, int mode, int modelType)
			throws InvalidOntologyPathException {
		this.ontologyPath = ontologyPath;
		this.mode = mode;
		this.model = createModel(modelType);
		try {
			if (mode == OntologyParser.HTTP_URL_MODE) {
				model.read(ontologyPath);

			} else
				model.read(new FileInputStream(ontologyPath), null);
		} catch (FileNotFoundException e) {
			throw new InvalidOntologyPathException(ontologyPath);
		}
	}

	private OntModel createModel(int modelType) {
		if (modelType == SIMPLE_MODEL)
			return ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		return ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
	}

	public String getOntologyPath() {
		return ontologyPath;
	}

	public String getClassUri(String className)
			throws ResourceNotFoundException {
		Iterator<OntClass> it = model.listClasses();
		while (it.hasNext()) {
			OntClass currentClass = it.next();
			if (currentClass.getLocalName() != null) {
				if (currentClass.getLocalName().equals(className))
					return currentClass.getURI();
			}
		}
		throw new ResourceNotFoundException(className);
	}

	public OntClass getOntClass(String className)
			throws ResourceNotFoundException {
		Iterator<OntClass> classes = model.listClasses();
		while (classes.hasNext()) {
			OntClass currentClass = classes.next();
			if (currentClass.getLocalName() != null)
				if (currentClass.getLocalName().equalsIgnoreCase(className)) {
					return currentClass;
				}
		}
		throw new ResourceNotFoundException(className);
	}

	public OntClass getOntClassByURI(String className)
			throws ResourceNotFoundException {
		Iterator<OntClass> classes = model.listClasses();
		while (classes.hasNext()) {
			OntClass currentClass = classes.next();
			if (currentClass.getURI() != null)
				if (currentClass.getURI().equals(className)) {
					return currentClass;
				}
		}
		throw new ResourceNotFoundException(className);
	}

	public DatatypeProperty getDataProperty(String propertyName)
			throws ResourceNotFoundException {
		Iterator<DatatypeProperty> properties = model.listDatatypeProperties();
		while (properties.hasNext()) {
			DatatypeProperty currentProperty = properties.next();
			if (currentProperty.getLocalName() != null) {
				String propName = propertyName.trim().toLowerCase();
				String curPropName = currentProperty.getLocalName().trim()
						.toLowerCase();
				if (curPropName.equals(propName))
					return currentProperty;
			}
		}
		throw new ResourceNotFoundException(propertyName);
	}

	public ObjectProperty getObjectProperty(String propertyName)
			throws ResourceNotFoundException {
		Iterator<ObjectProperty> properties = model.listObjectProperties();
		while (properties.hasNext()) {
			ObjectProperty currentProperty = properties.next();
			if (currentProperty.getLocalName() != null) {
				String propName = propertyName.trim().toLowerCase();
				String curPropName = currentProperty.getLocalName().trim()
						.toLowerCase();
				if (curPropName.equals(propName))
					return currentProperty;
			}
		}
		throw new ResourceNotFoundException(propertyName);
	}

	public OntResource getResource(String resourceName, Class resourceClass)
			throws ResourceNotFoundException {
		if (resourceClass.getSimpleName().equals("OntClass"))
			return this.getOntClass(resourceName);
		if (resourceClass.getSimpleName().equals("DatatypeProperty"))
			return this.getDataProperty(resourceName);
		if (resourceClass.getSimpleName().equals("ObjectProperty"))
			return this.getObjectProperty(resourceName);
		throw new ResourceNotFoundException(resourceName);
	}

	public boolean isRootClass(OntClass someClass) {
		OntClass superClass = someClass.getSuperClass();
		if (superClass == null) {			
			return true;
		}
		if (superClass.getLocalName() == null) {			
			return true;
		}
		if (superClass.getLocalName().equals("Thing")) {			
			return true;
		}
		return false;
	}

	public List<OntologyConcept> discoveryConcept(String resourceName)
			throws ResourceNotFoundException {
		List<OntologyConcept> result = new ArrayList();
		if (!containsConcept(resourceName))
			return result;

		String uri = this.getURI(resourceName);
		OntResource resource = model.getOntResource(uri);
		if (isOntolyClass(resource)) {
			return this.getEquivalentConcepts((OntClass) resource);
		}

		return result;
	}

	public boolean isSubclass(String subclassURI, String superClassURI)
			throws ResourceNotFoundException {
		OntClass ontClass = this.getOntClassByURI(superClassURI);
		Iterator<OntClass> it = ontClass.listSubClasses();
		while (it.hasNext()) {
			OntClass currentClass = it.next();
			if (currentClass.getURI().equals(subclassURI))
				return true;
		}
		return false;
	}

	public Iterator getConceptNames() {

		Collection<String> names = new ArrayList<String>();

		Iterator<OntClass> classes = model.listNamedClasses();
		String ontologyURi = model.listOntologies().next().getURI();
		while (classes.hasNext()) {
			names.add(classes.next().getLocalName());
		}
		return names.iterator();
	}

	public Iterator getImports() {

		return model.listImportedOntologyURIs().iterator();
	}

	public Iterator getConcepts() {
		
		return model.listNamedClasses();
	}

	public OntModel getModel() {
		return model;
	}

	public Individual getIndividual(String uri) {
		return model.getIndividual(uri);
	}

	public boolean containsResource(String uri) {
		if (model.getResource(uri) != null)
			return true;
		return false;
	}

	public boolean hasSuperclass(String classURI)
			throws ResourceNotFoundException {
		OntClass someClass = (OntClass) this.getResource(classURI,
				OntClass.class);
		if (this.isRootClass(someClass))
			return false;
		return true;
	}

	public String getURI(String resourceName) throws ResourceNotFoundException {
		Iterator<OntClass> classes = model.listNamedClasses();
		while (classes.hasNext()) {
			OntClass currentClass = classes.next();
			if (currentClass.getLocalName().toLowerCase()
					.equals(resourceName.toLowerCase()))
				return currentClass.getURI();
		}
		Iterator<OntProperty> properties = model.listOntProperties();
		while (properties.hasNext()) {
			OntProperty currentProperty = properties.next();
			if (currentProperty.getLocalName().toLowerCase()
					.equals(resourceName.toLowerCase()))
				return currentProperty.getURI();
		}
		throw new ResourceNotFoundException(resourceName);

	}

	public Iterator<String> getSubclassNames(String className)
			throws ResourceNotFoundException {
		OntClass desiredClass = (OntClass) this.getOntClass(className);
		Iterator<OntClass> subclasses = desiredClass.listSubClasses();
		Collection<String> names = new Vector();
		while (subclasses.hasNext()) {
			OntClass currentClass = subclasses.next();
			if (currentClass.getURI() != null)
				names.add(currentClass.getURI());
		}
		return names.iterator();
	}

	public Iterator<String> getSubclassNamesByURI(String classURI)
			throws ResourceNotFoundException {
		OntClass desiredClass = (OntClass) this.getOntClassByURI(classURI);
		Iterator<OntClass> subclasses = desiredClass.listSubClasses();
		Collection<String> names = new Vector();
		while (subclasses.hasNext()) {
			OntClass currentClass = subclasses.next();
			if (currentClass.getURI() != null)
				names.add(currentClass.getURI());
		}
		return names.iterator();
	}

	public Iterator<String> getSuperclasses(String uri)
			throws ResourceNotFoundException {
		OntClass desiredClass = (OntClass) this
				.getResource(uri, OntClass.class);

		Collection<String> result = new Vector<String>();
		Iterator<OntClass> it = desiredClass.listSuperClasses();

		Collection<String> equivalentClasses = new ArrayList<String>();
		it = desiredClass.listEquivalentClasses();
		while (it.hasNext()) {
			equivalentClasses.add(it.next().getLocalName());
		}

		while (it.hasNext()) {
			OntClass sc = it.next();
			if (!sc.isOntLanguageTerm()) {
				result.add(sc.getURI());
				if (!equivalentClasses.contains(sc.getLocalName())) {
					Iterator<String> superClasses = this.getSuperclasses(sc
							.getLocalName());
					while (superClasses.hasNext()) {
						String c = superClasses.next();
						result.add(c);

					}
				}
			}
		}

		return result.iterator();
	}

	private List<OntologyConcept> getEquivalentConcepts(OntClass someClass)
			throws ResourceNotFoundException {
		List<OntologyConcept> result = new Vector();
		result.add(new OntologyConcept(someClass.getURI()));
		Iterator<OntClass> it = someClass.listEquivalentClasses();
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();

		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		while (it.hasNext()) {
			OntClass currentClass = it.next();
			if (currentClass.getURI() != null)
				result.add(new OntologyConcept(currentClass.getURI()));
		}

		Iterator<OntClass> subclasses = someClass.listSubClasses();
		while (subclasses.hasNext()) {
			result.add(new OntologyConcept(subclasses.next().getURI()));
		}

		return result;
	}

	public boolean containsConcept(String concept) {
		Iterator<String> concepts = this.getConceptNames();
		while (concepts.hasNext()) {
			if (concepts.next().toLowerCase().equals(concept.toLowerCase()))
				return true;
		}
		return false;
	}

	public String getConcept(String concept) {
		Iterator<String> concepts = this.getConceptNames();
		while (concepts.hasNext()) {
			String currentConcept = concepts.next();
			if (currentConcept.toLowerCase().equals(concept.toLowerCase()))
				return currentConcept;
		}
		return null;
	}

	public void saveOntology() throws InvalidModeException {
		if (mode == HTTP_URL_MODE)
			throw new InvalidModeException();
		try {
			FileOutputStream fos = new FileOutputStream(new File(
					this.ontologyPath));
			model.write(fos);
		} catch (FileNotFoundException e) {
		}

	}

	public Individual createIndividual(String className)
			throws ResourceNotFoundException {
		OntClass desiredClass = this.getOntClass(className);
		String instanceURI = desiredClass.getURI() + System.currentTimeMillis();
		return model.createIndividual(instanceURI, desiredClass);
	}

	private boolean isOntolyClass(OntResource resource) {
		return resource instanceof OntClass;
	}

	public Set<String> listSuperclasses(String concept)
			throws ResourceNotFoundException {
		
		Set<String> result = new HashSet<String>(); 
		OntClass c = (OntClass) getResource(concept, OntClass.class);
		try {
			Iterator<OntClass> it = c.listSuperClasses();
			while (it.hasNext()) {
				result.add(it.next().getLocalName());				
			}			
		} catch (ConversionException e) {
			System.err.println("N�o foi poss�vel recuperar as superclasses de " + c.getLocalName());
		}
		try {
			Iterator<OntClass> it = c.listSubClasses();
			while (it.hasNext()) {
				result.add(it.next().getLocalName());	
			}
		} catch (ConversionException e) {
			System.err.println("N�o foi poss�vel recuperar as subclasses de " + c.getLocalName());
		}
		return result;
	}

	public Set<String> listSuperProperies(String concept)
			throws ResourceNotFoundException {
		
		Set<String> result = new HashSet<String>(); 
		ObjectProperty c = (ObjectProperty) getResource(concept, ObjectProperty.class);
		try {
			ExtendedIterator<ObjectProperty> it = (ExtendedIterator<ObjectProperty>) c.listSuperProperties();
			while (it.hasNext()) {
				result.add(it.next().getLocalName());				
			}			
		} catch (ConversionException e) {
			System.err.println("N�o foi poss�vel recuperar as superclasses de " + c.getLocalName());
		}
		try {
			Iterator<ObjectProperty> it = (ExtendedIterator<ObjectProperty>) c.listSubProperties();
			while (it.hasNext()) {
				result.add(it.next().getLocalName());	
			}
		} catch (ConversionException e) {
			System.err.println("N�o foi poss�vel recuperar as subclasses de " + c.getLocalName());
		}
		return result;
	}
	
	public static void main(String[] args) {

//	    String fileNameOrUri = "C:/yago_en.nt";
//	    Model model = ModelFactory.createDefaultModel();
//	    InputStream is = FileManager.get().open(fileNameOrUri);
//	    if (is != null) {
//	        model.read(is, null, "N-TRIPLE");
//	        model.write(System.out, "TURTLE");
//	    } else {
//	        System.err.println("cannot read " + fileNameOrUri);;
//	    }			

//		try {
//			String url = "http://www.daml.org/services/owl-s/1.0DL/Process.owl";
//			String url = "http://dbpedia.org/sparql";
//			String path = "C:/dbpedia_2014.owl";
//
//			OntologyParser parser = new OntologyParser(url,
//					OntologyParser.HTTP_URL_MODE, OntologyParser.SIMPLE_MODEL);
//			ExtendedIterator<OntClass> imports = parser.getModel()
//					.listNamedClasses();
//			System.out.println("Concepts");
//			int cont = 0;
//			while (imports.hasNext()) {
//				OntClass ontol = imports.next();
//				System.out.println("   " + ontol);
//				cont++;
//				try {					
//					System.out.println("    ---> " + ontol.getSuperClass());
//					cont++;
//				} catch (ConversionException e) {
//					System.err.println("N�o foi poss�vel recuperar a superclasse de " + ontol.getLocalName());
//				}
//			}
//			System.out.println("\nNumber of concepts: " + cont);
//			System.out.println(parser.getOntClass("http://dbpedia.org/ontology/Agent"));
//			System.out.println(parser.listSuperclasses("http://dbpedia.org/ontology/Agent"));
//		}
//
//		catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}

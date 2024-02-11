package elplusplus;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
public class Application 
{
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//IRI pizzaontology = IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
        File file = new File("C:\\Users\\Pc\\Desktop\\java projects\\elplusplus\\stress_test.rdf");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        ELPlusPlusReasoner reasoner = new ELPlusPlusReasoner(ontology);
        reasoner.execute();
        System.out.println("Algorithm finished");
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        IRI conceptA = IRI.create(ontologyIRI.toString() + "#A16");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#A26");
        // Ottenere la classe specifica dall'ontologia
        OWLClass owlConceptA = manager.getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = manager.getOWLDataFactory().getOWLClass(conceptB);

        // Verifica se la classe esiste nell'ontologia
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, owlConceptB));
        } else {
        	System.out.println("They don't exist");
        }
        /*Set<GCI> gcis = Utilities.getGCIs(ontology, reasoner);
        for(GCI gci: gcis)
        	System.out.println(gci.toString());
        ElPlusPlusNormalizer normalizer = new ElPlusPlusNormalizer(ontology, gcis);
        normalizer.execute();
        Set<GCI> normalizedExpressions = normalizer.getNormalizedExpressions();
        Set<OWLIndividual> individuals = Utilities.getIndividualsFromCBox(gcis);
        System.out.println("Normalized Expressions-----------------------------------");
        for(GCI gci: normalizedExpressions)
        	System.out.println(gci.toString());
        System.out.println("Individuals-----------------------------------------------");
        for(OWLIndividual individual : individuals)
        	System.out.println(individual);*/
	}
}

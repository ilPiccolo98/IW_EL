package elplusplus;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
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
        File file = new File("C:\\Users\\Pc\\Desktop\\java projects\\elplusplus\\test.rdf");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        System.out.println(ontologyIRI);
        IRI conceptA = IRI.create(ontologyIRI.toString() + "#A");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#B");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#C");
        // Ottenere la classe specifica dall'ontologia
        OWLClass owlConceptA = manager.getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = manager.getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = manager.getOWLDataFactory().getOWLClass(conceptC);
        
        Set<OWLClassExpression> intersezioneClassi = new HashSet<>();
        intersezioneClassi.add(owlConceptA);
        intersezioneClassi.add(owlConceptB);

        OWLClassExpression intersezione = manager.getOWLDataFactory().getOWLObjectIntersectionOf(intersezioneClassi);
        
        ELPlusPlusReasoner reasoner = new ELPlusPlusReasoner(ontology);
        // Verifica se la classe esiste nell'ontologia
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(intersezione, owlConceptC));
            reasoner.printMappingS();
            reasoner.printMappingR();
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

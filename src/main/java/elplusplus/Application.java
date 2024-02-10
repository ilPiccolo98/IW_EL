package elplusplus;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.HasObjectPropertiesInSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class Application 
{
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//IRI pizzaontology = IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
        File file = new File("C:\\Users\\Pc\\Desktop\\java projects\\elplusplus\\test.rdf");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        OWLReasonerFactory rf = new ReasonerFactory();
        OWLReasoner reasoner = rf.createReasoner(ontology);
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        Set<OWLClass> classes = ontology.getClassesInSignature();
        Set<GCI> gcis = Utilities.getGCIs(ontology, reasoner);
        for(GCI gci: gcis)
        	System.out.println(gci.toString());
        Normalizer normalizer = new Normalizer(ontology, gcis);
        normalizer.execute();
        Set<GCI> normalizedExpressions = normalizer.getNormalizedExpressions();
        Set<OWLIndividual> individuals = Utilities.getIndividualFromNormalizedCbox(normalizedExpressions);
        System.out.println("Normalized Expressions-----------------------------------");
        for(GCI gci: normalizedExpressions)
        	System.out.println(gci.toString());
	}
}

package elplusplus;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.HasObjectPropertiesInSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class Application 
{
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		IRI pizzaontology = IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
		OWLOntology o = man.loadOntology(pizzaontology);
        OWLReasonerFactory rf = new ReasonerFactory();
        OWLReasoner reasoner = rf.createReasoner(o);
     // Ottieni le classi coinvolte
        OWLClass classA = o.getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create("http://www.semanticweb.org/ontology#ClassA"));
        OWLClass classB = o.getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create("http://www.semanticweb.org/ontology#ClassB"));
        OWLClass classC = o.getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create("http://www.semanticweb.org/ontology#ClassC"));
        // Crea un'intersezione di classi
        OWLObjectIntersectionOf intersection1 = o.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(classA, classB);

        OWLObjectIntersectionOf intersection2 = o.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(intersection1, classC);
         
        // Crea un'asserzione di sottoclasse
        OWLSubClassOfAxiom subClassOfAxiom = o.getOWLOntologyManager().getOWLDataFactory().getOWLSubClassOfAxiom(intersection2, classC);

        // Aggiungi l'asserzione all'ontologia
        man.applyChange(new AddAxiom(o, subClassOfAxiom));

        Set<GCI> gcis = Utilities.getGCIs(o);
        for(GCI gic : gcis)
        {
        	System.out.println("Type of subclass: " + gic.getSubClass().getClassExpressionType() + " is it in BC: " + Utilities.isInBC(gic.getSubClass()));
        	System.out.println("Type of superclass: " + gic.getSuperClass().getClassExpressionType() + " is it in BC: " + Utilities.isInBC(gic.getSuperClass()));	
        }
	}
}

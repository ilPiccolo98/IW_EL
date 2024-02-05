package elplusplus;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.HasObjectPropertiesInSignature;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class Application 
{
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		IRI pizzaontology = IRI.create("https://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl");
		OWLOntology o = man.loadOntology(pizzaontology);
        OWLReasonerFactory rf = new ReasonerFactory();
        OWLReasoner reasoner = rf.createReasoner(o);
        Set<GCI> gcis = Utilities.getGCIs(o);
        for(GCI gic : gcis)
        {
        	System.out.println("Type of subclass: " + gic.getSubClass().getClassExpressionType() + " is it in BC: " + Utilities.isInBC(gic.getSubClass()));
        	System.out.println("Type of superclass: " + gic.getSuperClass().getClassExpressionType() + " is it in BC: " + Utilities.isInBC(gic.getSuperClass()));	
        }
	}
}

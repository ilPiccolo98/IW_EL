package elplusplus;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

public class VeicoliQueries 
{
	public static void isScuderiaCampionatoF1SubClassOfScuderia(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#ScuderiaCampionatoF1");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Scuderia");
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, owlConceptB));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isPilotaDiGrigliaSubClassOfScuderia(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#PilotaDiGriglia");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Scuderia");
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, owlConceptB));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isTeamPrincipalSubClassOfiscrittoASomeCampionatoF1(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#TeamPrincipal");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#CampionatoF1");
        IRI role = IRI.create(ontologyIRI.toString() + "#iscrittoA");
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLObjectPropertyExpression property = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(role);
        OWLObjectSomeValuesFrom existential = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(property, owlConceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsObjectPropertyInSignature(role)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, existential));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isPilotaF1SubClassOfPerson(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#PilotaF1");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Persona");
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, owlConceptB));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isPilotaDiGrigliaSubClassOfAdulto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#PilotaDiGriglia");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Adulto");
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, owlConceptB));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isPilotaDiGrigliaSubClassOftrasfertaSomeMonza(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#PilotaDiGriglia");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#monza");
        IRI role = IRI.create(ontologyIRI.toString() + "#trasferta");
        OWLIndividual monza = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(conceptB);
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLObjectOneOf owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectOneOf(monza);
        OWLObjectPropertyExpression property = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(role);
        OWLObjectSomeValuesFrom existential = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(property, owlConceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsIndividualInSignature(conceptB) &&
        		ontology.containsObjectPropertyInSignature(role)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, existential));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isPilotaCollaudatoreSubClassOftrasfertaSomeMonza(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#PilotaCollaudatore");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#monza");
        IRI role = IRI.create(ontologyIRI.toString() + "#trasferta");
        OWLIndividual monza = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNamedIndividual(conceptB);
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLObjectOneOf owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectOneOf(monza);
        OWLObjectPropertyExpression property = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(role);
        OWLObjectSomeValuesFrom existential = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(property, owlConceptB);
        if (ontology.containsClassInSignature(conceptA) && ontology.containsIndividualInSignature(conceptB) &&
        		ontology.containsObjectPropertyInSignature(role)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, existential));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isScuderiaCampionatoF1SubClassOfIndividualWithTeamPrincipalCapoMeccanicoCapoIngengere(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#ScuderiaCampionatoF1");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Meccanico");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#Ingegnere");
        IRI conceptD = IRI.create(ontologyIRI.toString() + "#TeamPrincipal");
        
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haCapoMeccanico");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#haCapoIngegnere");
        IRI roleC = IRI.create(ontologyIRI.toString() + "#haTeamPrincipal");
        
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLClass owlConceptD = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptD);
        
        
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        OWLObjectPropertyExpression propertyC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleC);
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptB);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptC);
        OWLObjectSomeValuesFrom existentialC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyC, owlConceptD);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialA, existentialB, existentialC);
   
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsClassInSignature(conceptD) &&
        		ontology.containsObjectPropertyInSignature(roleA)&& ontology.containsObjectPropertyInSignature(roleB) &&
        		ontology.containsObjectPropertyInSignature(roleC)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(owlConceptA, intersection));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
}

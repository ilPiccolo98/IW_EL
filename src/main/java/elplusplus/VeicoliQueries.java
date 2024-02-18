package elplusplus;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;

public class VeicoliQueries 
{
	public static void isCiclistaSubClassOfPersona(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Ciclista");
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
	
	public static void isAdultoSubClassOfPatenteMoto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Adulto");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#PatenteMoto");
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
	
	public static void isIndividualsWithAutoAndAdultoAndWithPatenteAutoSubClassOfPersona(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Auto");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Adulto");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#Persona");
        IRI conceptD = IRI.create(ontologyIRI.toString() + "#PatenteAuto");
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haAuto");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#haPatente");
        
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLClass owlConceptD = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptD);
        
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptA);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptD);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialA, existentialB, owlConceptB);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsClassInSignature(conceptD) && 
        		ontology.containsObjectPropertyInSignature(roleA) && ontology.containsObjectPropertyInSignature(roleB)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(intersection, owlConceptC));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isIndividualsWithBiciAndBambinoSubClassOfPersona(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Bicicletta");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Bambino");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#Persona");
        IRI role = IRI.create(ontologyIRI.toString() + "#haBicicletta");
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLObjectPropertyExpression property = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(role);
        
        OWLObjectSomeValuesFrom existential = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(property, owlConceptA);
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existential, owlConceptB);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsObjectPropertyInSignature(role)) {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(intersection, owlConceptC));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isAutoBenzinaSubClassOfVeicolo(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#AutoBenzina");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Veicolo");
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
	
	public static void isIndividualsWithAutoDieselSubClassOfIndividualThatPagaBolloAutoAndAssicurazioneAuto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#AutoDiesel");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#BolloAuto");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#AssicurazioneAuto");
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haAuto");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#paga");
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptA);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptB);
        OWLObjectSomeValuesFrom existentialC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptC);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialB, existentialC);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsObjectPropertyInSignature(roleA) &&
        		ontology.containsObjectPropertyInSignature(roleB))
        {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(existentialA, intersection));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isIndividualsWithScooterSubClassOfIndividualThatPagaBolloMotoAndAssicurazioneMoto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Scooter");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#BolloMoto");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#AssicurazioneMoto");
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haMoto");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#paga");
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptA);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptB);
        OWLObjectSomeValuesFrom existentialC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptC);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialB, existentialC);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsObjectPropertyInSignature(roleA) &&
        		ontology.containsObjectPropertyInSignature(roleB))
        {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(existentialA, intersection));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isIndividualsWithAutoBenzinaSubClassOfIndividualThatPagaBolloMotoAndAssicurazioneMoto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#AutoBenzina");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#BolloMoto");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#AssicurazioneMoto");
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haAuto");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#paga");
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptA);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptB);
        OWLObjectSomeValuesFrom existentialC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptC);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialB, existentialC);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsObjectPropertyInSignature(roleA) &&
        		ontology.containsObjectPropertyInSignature(roleB))
        {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(existentialA, intersection));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isIndividualsWithMotoSportivaSubClassOfIndividualThatPagaBolloAutoAndAssicurazioneAuto(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#MotoSportiva");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#BolloAuto");
        IRI conceptC = IRI.create(ontologyIRI.toString() + "#AssicurazioneAuto");
        IRI roleA = IRI.create(ontologyIRI.toString() + "#haMoto");
        IRI roleB = IRI.create(ontologyIRI.toString() + "#paga");
        
        OWLClass owlConceptA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptA);
        OWLClass owlConceptB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptB);
        OWLClass owlConceptC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(conceptC);
        OWLObjectPropertyExpression propertyA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleA);
        OWLObjectPropertyExpression propertyB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(roleB);
        
        OWLObjectSomeValuesFrom existentialA = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyA, owlConceptA);
        OWLObjectSomeValuesFrom existentialB = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptB);
        OWLObjectSomeValuesFrom existentialC = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(propertyB, owlConceptC);
        
        OWLObjectIntersectionOf intersection = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectIntersectionOf(existentialB, existentialC);
        
        if (ontology.containsClassInSignature(conceptA) && ontology.containsClassInSignature(conceptB) &&
        		ontology.containsClassInSignature(conceptC) && ontology.containsObjectPropertyInSignature(roleA) &&
        		ontology.containsObjectPropertyInSignature(roleB))
        {
        	System.out.println("They exist");
        	System.out.println("Result subsumption: " + reasoner.subsumption(existentialA, intersection));
            reasoner.printMappingS();
            reasoner.printMappingR();
        } else {
        	System.out.println("They don't exist");
        }
	}
	
	public static void isAdultoSubClassOfAdolescente(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#Adulto");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Adolescente");
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
	
	public static void isAutoBenzinaSubClassOfIndividualsWithMotore(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#AutoBenzina");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Motore");
        IRI role = IRI.create(ontologyIRI.toString() + "#haComponente");
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
	
	public static void isAutoBenzinaSubClassOfIndividualsWithPedali(OWLOntology ontology, ELPlusPlusReasoner reasoner)
	{
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI conceptA = IRI.create(ontologyIRI.toString() + "#AutoBenzina");
        IRI conceptB = IRI.create(ontologyIRI.toString() + "#Pedali");
        IRI role = IRI.create(ontologyIRI.toString() + "#haComponente");
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
	
}

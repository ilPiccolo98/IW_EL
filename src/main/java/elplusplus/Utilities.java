package elplusplus;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utilities
{
	static Set<GCI> getGCIs(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<GCI> classesGCIs = getClassesGCIs(ontology);
		addSingletonGCIs(ontology, reasoner, classesGCIs);
		convertPropertyAssertionToGCI(ontology, reasoner, classesGCIs);
		return classesGCIs;
	}

	static Set<GCI> getClassesGCIs(OWLOntology ontology)
	{
		Set<GCI> gcis = new HashSet<GCI>();
		Set<OWLSubClassOfAxiom> axioms = ontology.getAxioms(AxiomType.SUBCLASS_OF);
		for (OWLSubClassOfAxiom axiom : axioms)
		{
            OWLClassExpression subClass = axiom.getSubClass();
            OWLClassExpression superClass = axiom.getSuperClass();
            gcis.add(new GCI(subClass, superClass));
        }
		return gcis;
	}

	private static void addSingletonGCIs(OWLOntology ontology, OWLReasoner reasoner, Set<GCI> GCIs){
//		Set<OWLClass> classes = ontology.getClassesInSignature();
//		for (OWLClass cls : classes) {
//			Set<OWLNamedIndividual> individuals = reasoner.getInstances(cls, true).getFlattened();
//			for (OWLNamedIndividual individual : individuals) {
//				OWLObjectOneOf singleton = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectOneOf(individual);
//				System.out.println("Adding singleton GCI: " + Utilities.prettyPrint(singleton) + " ⊑ " + Utilities.prettyPrint(cls));
//				GCIs.add(new GCI(singleton, cls));
//			}
//		}
		for (OWLAxiom axiom : ontology.getAxioms()) {
			// Check if the axiom is an assertion of class membership
			if (axiom instanceof OWLClassAssertionAxiom) {
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;

				// Retrieve the individual and its class
				OWLIndividual individual = classAssertionAxiom.getIndividual();
				OWLClassExpression classExpression = classAssertionAxiom.getClassExpression();

				GCIs.add(new GCI(ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectOneOf(individual), classExpression));
			}
		}
	}

	private static void convertPropertyAssertionToGCI(OWLOntology ontology, OWLReasoner reasoner, Set<GCI> GCIs){
		ontology.getIndividualsInSignature().forEach(individual -> {
			ontology.getObjectPropertyAssertionAxioms(individual).forEach(axiom -> {
//				System.out.println(axiom.getSubject() + " " + axiom.getProperty() + " " + axiom.getObject());
				/*
				   individual -> property -> object
				   {individual} ⊑ ∃property.{object}
				 */
				OWLObjectPropertyExpression property = axiom.getProperty();
				OWLObjectOneOf singletonA = ontology
						.getOWLOntologyManager()
						.getOWLDataFactory()
						.getOWLObjectOneOf(individual);
				OWLObjectOneOf singletonB = ontology
						.getOWLOntologyManager()
						.getOWLDataFactory()
						.getOWLObjectOneOf(axiom.getObject());
				OWLObjectSomeValuesFrom someValuesFrom = ontology
						.getOWLOntologyManager()
						.getOWLDataFactory()
						.getOWLObjectSomeValuesFrom(property, singletonB);
				GCIs.add(new GCI(singletonA, someValuesFrom));
				/*
				   object -> inverseProperty -> individual
				   {object} ⊑ ∃inverseProperty.{individual}
				 */
				OWLObjectPropertyExpression inverseProperty = findInverseProperty(property, reasoner);
				if (inverseProperty != null){
					OWLObjectSomeValuesFrom someValuesFromInverse = ontology
							.getOWLOntologyManager()
							.getOWLDataFactory()
							.getOWLObjectSomeValuesFrom(inverseProperty, singletonA);
					GCIs.add(new GCI(singletonB, someValuesFromInverse));
				}
			});
		});
	}

	private static OWLObjectPropertyExpression findInverseProperty(OWLObjectPropertyExpression property, OWLReasoner reasoner){
		Node<OWLObjectPropertyExpression> inverses = reasoner.getInverseObjectProperties(property);
		OWLObjectPropertyExpression inverse = (OWLObjectPropertyExpression) inverses.entities().toArray()[0];
		if (!(inverse instanceof OWLObjectInverseOf)){
			return inverse;
		}
		return null;
	}
	
	static boolean isInBC(OWLObject object)
	{
		if(object.isTopEntity())
			return true;
		else if(object.isBottomEntity())
			return false;
		else if(object.isIndividual())
			return true;
		else
		{
			OWLClassExpression expression = (OWLClassExpression)object;
			switch(expression.getClassExpressionType())
			{
				case OWL_CLASS:
					return true;
				case OBJECT_INTERSECTION_OF:
					return false;
				case OBJECT_SOME_VALUES_FROM:
					return false;
				case OBJECT_ONE_OF:
					return true;
				default:
					return false;
			}
		}
	}
	
	static boolean isInBCOrBottom(OWLClassExpression expression)
	{
		if(expression.isTopEntity())
			return true;
		else if(expression.isBottomEntity())
			return true;
		else if(expression.isIndividual())
			return true;
		switch(expression.getClassExpressionType())
		{
			case OWL_CLASS:
				return true;
			case OBJECT_INTERSECTION_OF:
				return false;
			case OBJECT_SOME_VALUES_FROM:
				return false;
			case OBJECT_ONE_OF:
				return true;
			default:
				return false;
		}
	}
	
	static Set<OWLObjectOneOf> getNominalsFromCBox(Set<GCI> cbox)
	{
		Set<OWLObjectOneOf> oneOfObjects = new HashSet<OWLObjectOneOf>();
		for(GCI gci : cbox)
		{
			gci.getSubClass().getNestedClassExpressions().forEach(expression -> {
				if(expression.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF)
					oneOfObjects.add((OWLObjectOneOf)expression);
			});
			gci.getSuperClass().getNestedClassExpressions().forEach(expression -> {
				if(expression.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF)
					oneOfObjects.add((OWLObjectOneOf)expression);
			});
		}
		return oneOfObjects;
	}

	static String prettyPrint(OWLObject object) {
		if (object.isTopEntity())
			return "⊤";
		else if (object.isBottomEntity())
			return "⊥";
		else if (object.isIndividual())
			return ((OWLNamedIndividual) object).getIRI().getShortForm();
		else {
			try {
				if (object instanceof OWLClassExpression){
					OWLClassExpression expression = (OWLClassExpression) object;
					switch (expression.getClassExpressionType()) {
						case OWL_CLASS:
							return expression.asOWLClass().getIRI().getShortForm();
						case OBJECT_ONE_OF:
							return "{"
									+ ((OWLObjectOneOf) expression).individuals().map(individual -> individual.asOWLNamedIndividual().getIRI().getShortForm())
									.reduce((s1, s2) -> s1 + ", " + s2).orElse("")
									+ "}";
						case OBJECT_INTERSECTION_OF:
							List<OWLClassExpression> operands = ((OWLObjectIntersectionOf) expression).getOperandsAsList();
							return "(" + operands.stream().map(Utilities::prettyPrint).reduce((s1, s2) -> s1 + " ⊓ " + s2).orElse("") + ")";
						case OBJECT_SOME_VALUES_FROM:
							return "∃" + ((OWLObjectSomeValuesFrom) expression).getProperty().asOWLObjectProperty().getIRI().getShortForm() + "."
									+ prettyPrint(((OWLObjectSomeValuesFrom) expression).getFiller());
						case OBJECT_HAS_VALUE:
							return "∃" + ((OWLObjectHasValue) expression).getProperty().asOWLObjectProperty().getIRI().getShortForm() + ".{"
									+ prettyPrint(((OWLObjectHasValue) expression).getFiller()) + "}";
						case OBJECT_COMPLEMENT_OF:
							return "¬" + prettyPrint(((OWLObjectComplementOf) expression).getOperand());
						default:
							throw new RuntimeException("Unknown class expression type " + expression.getClassExpressionType());
					}
				} else {
					// it's a role
					OWLObjectPropertyExpression expression = (OWLObjectPropertyExpression) object;
					return expression.getNamedProperty().getIRI().getShortForm();
				}
			}catch(ClassCastException exception) {
				throw exception;
			}
		}
	}
}

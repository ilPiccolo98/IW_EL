package elplusplus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Utilities 
{
	static Set<GCI> getGCIs(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<GCI> classesGCIs = getClassesGCIs(ontology);
		Set<List<OWLClassExpression>> equivalentClasses = getEquivalentClasses(ontology, reasoner);
		replaceEquivalentClasses(classesGCIs, equivalentClasses);
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
	
	static Set<List<OWLClassExpression>> getEquivalentClasses(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<List<OWLClassExpression>> equivalentClasses = new HashSet<List<OWLClassExpression>>();
		for(OWLAxiom axiom : ontology.getAxioms())
		{
			if (axiom.isOfType(AxiomType.EQUIVALENT_CLASSES))
			{
				OWLEquivalentClassesAxiom assertion = (OWLEquivalentClassesAxiom)axiom;
				equivalentClasses.add(assertion.getOperandsAsList());
			}
		}
		return equivalentClasses;
	}
	
	static Set<List<OWLClassExpression>> replaceEquivalentClasses(Set<GCI> gcis, Set<List<OWLClassExpression>> equivalentClasses)
	{
		for(GCI gci : gcis)
		{
			for(List<OWLClassExpression> equivalentClass : equivalentClasses)
			{
				if(equivalentClass.get(0).equals(gci.getSubClass()))
					gci.setSubClass(equivalentClass.get(1));
				if(equivalentClass.get(0).equals(gci.getSuperClass()))
					gci.setSuperClass(equivalentClass.get(1));
			}
		}
		return equivalentClasses;
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
			}catch(ClassCastException exception) {
				// it's a role
				OWLObjectPropertyExpression expression = (OWLObjectPropertyExpression) object;
				return expression.getNamedProperty().getIRI().getShortForm();
			}
		}
	}
}

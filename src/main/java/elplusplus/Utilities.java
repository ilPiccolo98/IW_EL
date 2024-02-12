package elplusplus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Utilities 
{
	static Set<GCI> getGCIs(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<GCI> classesGCIs = getClassesGCIs(ontology);
		Set<GCI> individualsGCIs = getIndividualsGCIs(ontology, reasoner);
		classesGCIs.addAll(individualsGCIs);
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
            gcis.add(new GCI(subClass, superClass, "expression"));
        }
		return gcis;
	}
	
	static Set<GCI> getIndividualsGCIs(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<GCI> gcis = new HashSet<GCI>();
		for(OWLAxiom axiom : ontology.getAxioms())
		{
			if (axiom.isOfType(AxiomType.CLASS_ASSERTION))
			{
				OWLClassAssertionAxiom assertion = (OWLClassAssertionAxiom)axiom;
				OWLIndividual individual = assertion.getIndividual();
				OWLClassExpression expression = assertion.getClassExpression();
				gcis.add(new GCI(individual, expression, "individual"));
			}
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
			default:
				return false;
		}
	}
	
	static Set<OWLIndividual> getIndividualsFromCBox(Set<GCI> cbox)
	{
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		for(GCI gci : cbox)
		{
			if(gci.getSubclassType().equals("individual"))
				individuals.add((OWLIndividual)gci.getSubClass());
		}
		return individuals;
	}

	static String prettyPrint(OWLObject object) {
		if (object.isTopEntity())
			return "⊤";
		else if (object.isBottomEntity())
			return "⊥";
		else {
			try {
				OWLClassExpression expression = (OWLClassExpression) object;
				switch (expression.getClassExpressionType()) {
					case OWL_CLASS:
						return expression.asOWLClass().getIRI().getShortForm();
					case OBJECT_INTERSECTION_OF:
						return "⊓";
					case OBJECT_SOME_VALUES_FROM:
						return "∃" + ((OWLObjectSomeValuesFrom) expression).getProperty().asOWLObjectProperty().getIRI().getShortForm() + "."
								+ prettyPrint(((OWLObjectSomeValuesFrom) expression).getFiller());
					default:
						return "Unknown";
				}
			}catch(ClassCastException exception) {
				// it's a role
				OWLObjectPropertyExpression expression = (OWLObjectPropertyExpression) object;
				return expression.getNamedProperty().getIRI().getShortForm();
			}
		}
	}
}

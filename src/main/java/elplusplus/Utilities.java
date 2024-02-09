package elplusplus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceDepth;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Utilities 
{
	static Set<GCI> getGCIs(OWLOntology ontology, OWLReasoner reasoner)
	{
		Set<GCI> classesGCIs = getClassesGCIs(ontology);
		Set<GCI> individualsGCIs = getIndividualsGCIs(ontology, reasoner);
		classesGCIs.addAll(individualsGCIs);
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
	
	static Set<OWLIndividual> getIndividualFromNormalizedCbox(Set<GCI> normalizedTBox)
	{
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		for(GCI gci : normalizedTBox)
		{
			if(gci.getSubclassType().equals("individual"))
				individuals.add((OWLIndividual)gci.getSubClass());
		}
		return individuals;
	}
}

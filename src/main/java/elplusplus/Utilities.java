package elplusplus;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class Utilities 
{
	static Set<GCI> getGCIs(OWLOntology ontology)
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
	
	static boolean isInBC(OWLClassExpression expression)
	{
		if(expression.isTopEntity())
			return true;
		else if(expression.isBottomEntity())
			return false;
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
}

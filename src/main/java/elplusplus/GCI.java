package elplusplus;

import org.semanticweb.owlapi.model.OWLClassExpression;

public class GCI 
{
	public GCI(OWLClassExpression subClass, OWLClassExpression superClass)
	{
		this.subClass = subClass;
		this.superClass = superClass;
	}
	
	public OWLClassExpression getSubClass() 
	{
		return subClass;
	}
	
	public OWLClassExpression getSuperClass() 
	{
		return superClass;
	}
	
	private OWLClassExpression subClass;
	private OWLClassExpression superClass;
}

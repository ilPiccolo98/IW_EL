package elplusplus;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;

public class GCI 
{
	public GCI(OWLObject subClass, OWLClassExpression superClass, String subclassType)
	{
		this.subClass = subClass;
		this.superClass = superClass;
		this.subclassType = subclassType;
	}
	
	public OWLObject getSubClass() 
	{
		return subClass;
	}
	
	public OWLClassExpression getSuperClass() 
	{
		return superClass;
	}
	
	public String toString()
	{
		return "Subclass: " + subClass.toString() + "; Superclass: " + superClass.toString();
	}
	
	public String getSubclassType()
	{
		return subclassType;
	}
	
	private OWLObject subClass;
	private OWLClassExpression superClass;
	private String subclassType;
}

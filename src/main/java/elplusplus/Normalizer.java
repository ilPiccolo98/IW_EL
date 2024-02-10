package elplusplus;

import java.util.Set;

public interface Normalizer 
{
	void execute();
	Set<GCI> getNormalizedExpressions();
}

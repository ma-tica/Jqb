package com.mcmatica.jqb;

public interface JqbWhereBuilder {

	JqbWhereBuilder and(JqbProperty property);
	
	JqbWhereBuilder or(JqbProperty property);
	
	String text();
	
	JqbWhereBuilder start(JqbProperty property);

	JqbWhereBuilder and(JqbWhereBuilder where);

	JqbWhereBuilder or(JqbWhereBuilder where);

	JqbWhereBuilder start(JqbWhereBuilder where);
}

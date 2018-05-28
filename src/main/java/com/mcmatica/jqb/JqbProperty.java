package com.mcmatica.jqb;

public interface JqbProperty {

	public JqbProperty eq(Object value);
	public JqbProperty lt(Object value);
	public JqbProperty gt(Object value);
	public JqbProperty ge(Object value);
	public JqbProperty le(Object value);
	public JqbProperty ne(Object value);
	public JqbProperty contains(Object value);
	public JqbProperty begins(Object value);
	public JqbProperty in(Object value);

	
	public JqbProperty eq(Object value, boolean nullIgnore);
	public JqbProperty lt(Object value, boolean nullIgnore);
	public JqbProperty gt(Object value, boolean nullIgnore);
	public JqbProperty ge(Object value, boolean nullIgnore);
	public JqbProperty le(Object value, boolean nullIgnore);	
	public JqbProperty ne(Object value, boolean nullIgnore);
	
	
	public String text();
	
}

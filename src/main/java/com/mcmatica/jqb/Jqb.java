package com.mcmatica.jqb;

public class Jqb {
	
	
	private final JqbDialect dialect;
	
	JqbWhereBuilder where;
	
	public Jqb(JqbDialect dialect)
	{		
		this.dialect = dialect;
	}

	public JqbWhereBuilder getWhere()
	{
		return this.where;
	}
	
	public JqbWhereBuilder where(JqbProperty property)
	{
		this.where = new JqbWhereBuilderImpl(this.dialect);
		this.where.start(property);
		return this.where;
	}

	public JqbWhereBuilder where(JqbWhereBuilder where)
	{
		this.where = new JqbWhereBuilderImpl(this.dialect);
		this.where.start(where);
		return this.where;
	}

	public JqbProperty property(String columnName)
	{
		return new JqbPropertyImpl(dialect, columnName);
	}
	
}



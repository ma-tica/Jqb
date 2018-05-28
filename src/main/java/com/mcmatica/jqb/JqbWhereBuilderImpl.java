package com.mcmatica.jqb;

class JqbWhereBuilderImpl implements JqbWhereBuilder {

	private String where = new String("");
	private final JqbDialect dialect;
	
    private String last;
	
	
	public JqbWhereBuilderImpl(JqbDialect dialect)
	{
		this.dialect = dialect;
		this.last = null;
	}
	

	@Override
	public JqbWhereBuilder start(JqbProperty property) {
		this.last = property.text();
		this.where += this.last == null ? "" : this.last;
		return this;
	}

	@Override
	public JqbWhereBuilder start(JqbWhereBuilder where) {
		this.last = where.text();
		this.where += this.last;
		return this;
	}

	@Override
	public JqbWhereBuilder and(JqbProperty property) {
	
		String tmp = new String("");
		switch (dialect) {
		case MONGODB:			
			if (property.text() != null && this.last != null) {
				tmp = String.format("{\"$and\": [%s, %s]}",  this.last, property.text());
				this.where = "";
			} else if (property.text() != null) {
				tmp = String.format("%s",   property.text());
				this.where = "";
			}
			break;
		case AZURE:
		case MYSQL:
			if (property.text() != null && this.last != null) {
				tmp = String.format("(%s and %s)", this.last, property.text());
				this.where = "";
			} else if (property.text() != null) {
				tmp = String.format("(%s)", property.text());
				this.where = "";
			}			
			break;
		default:
		}

		this.where += tmp;
		
		this.last = tmp.isEmpty() ? this.last : tmp;
		return this;
	}
	
	@Override
	public JqbWhereBuilder or(JqbProperty property) {
		String tmp = new String("");
		switch (dialect) {
		case MONGODB:			
			if (property.text() != null && this.last != null) {
				tmp = String.format("{\"$or\": [%s, %s]}",  this.last, property.text());
				this.where = "";
			} else if (property.text() != null) {
				tmp = String.format("%s",   property.text());
				this.where = "";
			}
			break;
		case AZURE:
		case MYSQL:			
			if (property.text() != null && this.last != null) {
				tmp = String.format("(%s or %s)", this.last, property.text());
				this.where = "";
			} else if (property.text() != null) {
				tmp = String.format("(%s)", property.text());
				this.where = "";
			}
			
			break;
		default:
		}

		where += tmp;
		
		this.last = tmp.isEmpty() ? this.last : tmp;
		return this;
	}
	
	
	@Override
	public JqbWhereBuilder and(JqbWhereBuilder where) {
		String tmp = new String("");
		switch (dialect) {
		case MONGODB:		
			if (where.text() != null) {
				tmp = String.format("{\"$and\": [%s]}",  where.text());
				this.where = "";
			}			
			break;
		case AZURE:
		case MYSQL:
			if (where.text() != null) {
				tmp = " and (" + where.text() + ")" ;
			}
			break;
		default:
		}
		this.where += tmp;
		
		this.last = tmp.isEmpty() ? this.last : tmp;
	
		return this;
	}

	@Override
	public JqbWhereBuilder or(JqbWhereBuilder where) {
		String tmp = new String("");
		switch (dialect) {
		case MONGODB:			
			if (where.text() != null) {
				tmp = String.format("{\"$or\": [%s]}",  where.text());
			}
			this.where = "";
			break;
		case AZURE:
		case MYSQL:
			if (where.text() != null) {
				tmp = " or (" + where.text() +")" ;
			}
			break;
		default:
		}
		this.where += tmp;
		
		this.last = tmp.isEmpty() ? this.last : tmp;
	
		return this;
	}
		

	@Override
	public String text() {
		return this.where;
	}
	
	
	@Override
	public String toString() {
		return "JqbWhereBuilderImpl [text()=" + text() + "]";
	}

	
	
}

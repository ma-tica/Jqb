package com.mcmatica.jqb;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

class JqbPropertyImpl implements JqbProperty {

	private final JqbDialect dialect;
	private final String columnName;
	private String text;
	
	
	public JqbPropertyImpl(JqbDialect dialect, String columnName)
	{
		this.dialect = dialect;
		this.columnName = columnName;
		this.text = null; 	
	}
	
	@Override
	public JqbProperty eq(Object value )
	{
		return 	eq(value , false);
	}
	@Override
	public JqbProperty eq(Object value , boolean nullIgnore)
	{
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$eq");
			break;
		case AZURE:
			this.build(value, "eq");
			break;
		case MYSQL:
			this.build(value, "=");
			break;

		default:
			break;
		}
		return this;
	}

	@Override
	public JqbProperty ne(Object value )
	{		
		return 	ne(value , false);
	}
	@Override
	public JqbProperty ne(Object value, boolean nullIgnore)
	{
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$ne");
			break;
		case AZURE:
			this.build(value, "ne");
			break;
		case MYSQL:
			this.build(value, "<>");
			break;

		default:
			break;
		}
		return this;
	}

	@Override
	public JqbProperty lt(Object value )
	{
		return 	lt(value , false);
	}
	@Override
	public JqbProperty lt(Object value, boolean nullIgnore) {
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$lt");
			break;
		case AZURE:
			this.build(value, "lt");
			break;
		case MYSQL:
			this.build(value, "<");
			break;

		default:
			break;
		}
		return this;
	}

	@Override
	public JqbProperty gt(Object value )
	{
		return 	gt(value , false);
	}
	@Override
	public JqbProperty gt(Object value, boolean nullIgnore) {
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$gt");
			break;
		case AZURE:
			this.build(value, "gt");
			break;
		case MYSQL:
			this.build(value, ">");
			break;

		default:
			break;
		}
		return this;
	}

	@Override
	public JqbProperty ge(Object value )
	{
		return 	ge(value , false);
	}	
	@Override
	public JqbProperty ge(Object value, boolean nullIgnore) {
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$gte");
			break;
		case AZURE:
			this.build(value, "ge");
			break;
		case MYSQL:
			this.build(value, ">=");
			break;

		default:
			break;
		}
		return this;
	}

	@Override
	public JqbProperty le(Object value )
	{
		return 	le(value , false);
	}
	@Override
	public JqbProperty le(Object value, boolean nullIgnore) {
		if (value == null && nullIgnore)
		{
			return this;
		}
		switch (dialect) {
		case MONGODB:
			this.build(value, "$lte");
			break;
		case AZURE:
			this.build(value, "le");
			break;
		case MYSQL:
			this.build(value, "<=");
			break;

		default:
			break;
		}
		return this;
	}
	
	@Override
	public JqbProperty contains(Object value) {
		if (value == null )
		{
			return this;			
		}
		switch (dialect) {
		case MONGODB:
			this.build(String.format(".*%s.*", value), "$regex");
			break;
		case AZURE:
			this.build(value, "eq");
			break;
		case MYSQL:
			this.build("%" + value + "%", "like");
			break;
		default:
			break;
		}
		return this;
		
	}
	
	@Override
	public JqbProperty begins(Object value) {
		if (value == null )
		{
			return this;			
		}
		switch (dialect) {
		case MONGODB:
			this.build(String.format("^%s", value), "$regex");
			break;
		case AZURE:
			this.build(value, "eq");
			break;
		case MYSQL:
			this.build(value + "%", "like");
			break;
		default:
			break;
		}
		return this;
		
	}


	@Override
	public JqbProperty in(Object value) {
		if (value == null )
		{
			return this;
		}
		switch (dialect) {
			case MONGODB:
				this.build(value, "$in");
				break;
			case AZURE:
				this.build(value, "in");
				break;
			case MYSQL:
				this.build(value, "in");
				break;
			default:
				break;
		}
		return this;

	}


	@Override
	public String text()
	{
		return text;
	}

	private String parseValue( Object value) throws Exception {
		String valueString = "";

		/*
		 * NULL
		 */
		if (value == null)
		{
			valueString = "''";
			if (this.dialect == JqbDialect.MYSQL)
			{
				valueString = "NULL";
			}
		}

		/*
		 * String
		 */
		else if (value instanceof String)
		{
			if (this.dialect == JqbDialect.MONGODB) {
				valueString = String.format("\"%s\"", value.toString());
			}else{
				valueString = String.format("\'%s\'", value.toString());
			}
		}

		/*
		 * Integer
		 */
		else if (value instanceof Integer)
		{
			valueString = Integer.toString((Integer)value);
		}

		/*
		 * Double
		 */
		else if (value instanceof Double)
		{
			valueString = Double.toString((Double)value);
		}

		/*
		 * Long
		 */
		else if (value instanceof Long)
		{
			valueString = Long.toString((Long)value);
		}

		/*
		 * Enum
		 */
		else if (value instanceof Enum)
		{
			if (this.dialect == JqbDialect.MONGODB) {
				valueString = String.format("\"%s\"", value.toString());
			}else{
				valueString = String.format("\'%s\'", value.toString());
			}
		}

		/*
		 * Date
		 */
		else if (value instanceof Date)
		{

			if (this.dialect == JqbDialect.MONGODB)
			{
				DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
				valueString = String.format("ISODate('%s')" ,  fmt.print (new DateTime(((Date)value)).toDateTime(DateTimeZone.UTC)));
			}else if(this.dialect == JqbDialect.AZURE)
			{
				valueString = Long.toString(new DateTime((Date) value).getMillis() / 1000);
			}else if(this.dialect == JqbDialect.MYSQL)
			{
				//fieldName = String.format("date(%s)", this.columnName);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				valueString =  "'" + sdf.format((Date) value) + "'";
			}
		}

		/*
		 * Boolean
		 */
		else if (value instanceof Boolean)
		{
			valueString = Boolean.toString((Boolean)value);

		}

		/*
		 * List of value
		 */
		else if (value instanceof List) {
			for(Object item : (List) value) {
				if (!valueString.isEmpty()) {
					valueString += ",";
				}
				valueString += parseValue(item);
			}
			if (this.dialect == JqbDialect.MONGODB) {
				valueString = "[" + valueString + "]";
			}else if(this.dialect == JqbDialect.MYSQL) {
				valueString = "(" + valueString + ")";
			}
		}

		else {
			throw new Exception("Value type " + value.getClass().getName() + " not supported");
		}

		return valueString;

	}

	private void build(Object value, String comparator) {
		String fieldName = this.columnName;

		if (value instanceof Date)
		{
			if(this.dialect == JqbDialect.MYSQL)
			{
				fieldName = String.format("date(%s)", this.columnName);
			}
		}

		String valueString = null;
		try {
			valueString = this.parseValue(value);
			switch (this.dialect) {
				case MONGODB:
					this.text = String.format("{\"%s\": {\"%s\": %s}}",  fieldName, comparator, valueString);
					break;
				case AZURE:
					this.text = String.format("%s %s %s",  fieldName, comparator, valueString);
					break;
				case MYSQL:
					this.text = String.format("%s %s %s",  fieldName, comparator, valueString);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private void build(Object value, String comparator)
//	{
//		String fieldName = this.columnName;
//		String valueString = "";
//
//		/*
//		 * NULL
//		 */
//		if (value == null)
//		{
//			valueString = "''";
//			if (this.dialect == JqbDialect.MYSQL)
//			{
//				valueString = "NULL";
//			}
//		}
//
//		/*
//		 * String
//		 */
//		else if (value instanceof String)
//		{
//			if (this.dialect == JqbDialect.MONGODB) {
//				valueString = String.format("\"%s\"", value.toString());
//			}else{
//				valueString = String.format("\'%s\'", value.toString());
//			}
//		}
//
//		/*
//		 * Integer
//		 */
//		else if (value instanceof Integer)
//		{
//			valueString = Integer.toString((Integer)value);
//		}
//
//		/*
//		 * Double
//		 */
//		else if (value instanceof Double)
//		{
//			valueString = Double.toString((Double)value);
//		}
//
//		/*
//		 * Long
//		 */
//		else if (value instanceof Long)
//		{
//			valueString = Long.toString((Long)value);
//		}
//
//		/*
//		 * Enum
//		 */
//		else if (value instanceof Enum)
//		{
//			if (this.dialect == JqbDialect.MONGODB) {
//				valueString = String.format("\"%s\"", value.toString());
//			}else{
//				valueString = String.format("\'%s\'", value.toString());
//			}
//		}
//
//		/*
//		 * Date
//		 */
//		else if (value instanceof Date)
//		{
//
//			if (this.dialect == JqbDialect.MONGODB)
//			{
//				valueString = String.format("{\"$date\": %s}" , value);
//			}else if(this.dialect == JqbDialect.AZURE)
//			{
//				valueString = Long.toString(new DateTime((Date) value).getMillis() / 1000);
//			}else if(this.dialect == JqbDialect.MYSQL)
//			{
//				fieldName = String.format("date(%s)", this.columnName);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				valueString =  "'" + sdf.format((Date) value) + "'";
//			}
//		}
//
//		/*
//		 * Boolean
//		 */
//		else if (value instanceof Boolean)
//		{
//			valueString = Boolean.toString((Boolean)value);
//
//		}
//
//		/*
//		 * List of value
//		 */
//		else if (value instanceof Collections) {
//
//	    }
//
//		else {
//			//throw new Exception("Value type " + value.getClass().getName() + " not supported");
//			System.out.println("Value type " + value.getClass().getName() + " not supported");
//		}
//
//		switch (this.dialect) {
//		case MONGODB:
//			this.text = String.format("{\"%s\": {\"%s\": %s}}",  fieldName, comparator, valueString);
//			break;
//		case AZURE:
//			this.text = String.format("%s %s %s",  fieldName, comparator, valueString);
//			break;
//		case MYSQL:
//			this.text = String.format("%s %s %s",  fieldName, comparator, valueString);
//			break;
//		default:
//			break;
//		}
//
//
//	}

	
}

package jqbtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mcmatica.jqb.Jqb;
import com.mcmatica.jqb.JqbDialect;
import com.mcmatica.jqb.JqbWhereBuilder;

public class Test {

	@org.junit.Test
	public void testWhere() {
		Jqb q = new Jqb(JqbDialect.MONGODB);
		
		boolean b = true;
		
		JqbWhereBuilder where = q.where(q.property("Token").eq("token2"))
				                .and(q.property("Token_release_date").eq(new Date()))
				                .and(q.property("Owner_uuid").eq("pippo"))
        						.and(q.property("ciao").eq(b));
		System.out.println(where.text());
	}

	@org.junit.Test
	public void testWhereMulti() {
		Jqb q = new Jqb(JqbDialect.MYSQL);
		
		JqbWhereBuilder where1 = q.where(q.property("Token").eq("token2"))
				                .and(q.property("Token_release_date").eq(new Date()))
				                .and(q.property("Owner_uuid").eq("pippo"));
		
		JqbWhereBuilder where2 = q.where(q.property("prop3").eq(2))
				.and(q.property("pro4").eq(null, false));
		
		JqbWhereBuilder where = q.where(where1).or(where2);
		
		
		System.out.println("testWhereMulti");
		System.out.println(where.text());
	}
	
	@org.junit.Test
	public void testContains() {
		Jqb q = new Jqb(JqbDialect.MYSQL);
		
		JqbWhereBuilder where = q.where(q.property("Token").contains("token2"))
				                .and(q.property("Token_release_date").eq(new Date()));


		System.out.println("testContains");
		System.out.println(where.text());
	}

	@org.junit.Test
	public void testIgnoreNull() {
		Jqb q = new Jqb(JqbDialect.MYSQL);
		

		JqbWhereBuilder where = q.where(q.property("productCode").contains(null))
				.and(q.property("pcomNumber").contains(null))
				.and(q.property("ddtNumber").contains(null))
				.and(q.property("dtDateAdded").eq(new Date(), true))
				.and(q.property("rma_number").contains(null))
				.and(q.property("installatore").contains(null));


		System.out.println("testIgnoreNull");
		System.out.println(where.text());
	}

	@org.junit.Test
	public void testWhereIn() {
		Jqb q = new Jqb(JqbDialect.MONGODB);

		List<Integer> dati = new ArrayList();
		dati.add(1);
		dati.add(12);
		dati.add(2);

		JqbWhereBuilder where = q.where(q.property("level").in(dati));

		System.out.println("testWhereIn");
		System.out.println(where.text());

		List<Date> dati2 = new ArrayList();
		dati2.add(new Date());
		dati2.add(new Date());

		JqbWhereBuilder where2 = q.where(q.property("level").in(dati2));
		System.out.println(where2.text());

	}

}

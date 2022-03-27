package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD2 {
	private Parser parser;
	
	@Before
	public void initialize() {
		parser = new Parser();
	}
	
	@Test
	public void test_example() {
		parser.addAll("option1 option2 option3 option4", "o1 o2 o3 o4", "String Integer Boolean Character");
		String[] options = {"o1", "o2", "o3", "o4","option1", "option2", "option3", "option4"};
		for (String i:options){
			assertTrue(parser.optionOrShortcutExists(i));
		}
		parser.addAll("option5", "Integer String");
		parser.parse("-o1=test -o2=33 -o3=true --option4=a --option5=12");
		assertTrue(parser.getBoolean("-o3"));
		assertEquals(parser.getString("o1"), "test");
		assertEquals(parser.getCharacter("--option4"), 'a');
		assertFalse(parser.optionExists("--option5"));
		assertEquals(parser.getInteger("o2"), 33);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_order() {
		parser.addAll("option1 option2", "String Integer", "o1, o2");
	}
	
	@Test
	public void test_no_shortcuts() {
		parser.addAll("option1 option2", "String Integer");
		assertTrue(parser.optionExists("option1"));
		assertTrue(parser.optionExists("option2"));
	}
	
	@Test
	public void test_extra_space() {
		parser.addAll("option1  option2", "String Integer");
		assertTrue(parser.optionExists("option1"));
		assertTrue(parser.optionExists("option2"));
	}
	
	@Test
	public void naming_convention() {
		parser.addAll("option1#  option2", "o1 o2", "String Integer");
		assertFalse(parser.shortcutExists("o1"));
		assertTrue(parser.optionExists("option2"));
	}
	
	@Test
	public void less_shortcuts() {
		parser.addAll("option1  option2", "o1", "String Integer");
		assertTrue(parser.optionExists("option2"));
		parser.parse("o1=Hello");
		assertEquals("Hello", parser.getString("option1"));
	}
	
	@Test
	public void more_shortcuts() {
		parser.addAll("option1  option2", "o1 o2 o3", "String Integer");
		assertFalse(parser.shortcutExists("o3"));
	}
	
	@Test
	public void less_types() {
		parser.addAll("option1  option2", "o1 o2", "Integer");
		parser.parse("-o2=34");
		assertEquals(parser.getInteger("option2"), 34);
	}
	
//	//GROUP INITIALIZATION
	@Test
	public void group_naming_convention() {
		parser.addAll("option7@9 optiona-c", "o7-9", "Integer");
		assertTrue(parser.optionExists("optiona"));
		assertFalse(parser.optionExists("o7"));
		parser.addAll("option1234-7ab t5-7", "opt1-7", "String");
		assertFalse(parser.shortcutExists("opt1"));
		assertTrue(parser.optionExists("t5"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void no_groups_type() {
		parser.addAll("option7@9", "o7-9", "Integer7-9");
	}
	
	@Test
	public void inclusive_range() {
		parser.addAll("option7-9", "o7-9", "Integer");
		assertTrue(parser.optionExists("option7"));
		assertTrue(parser.shortcutExists("o9"));
	}
	
	@Test
	public void generate_range() {
		parser.addAll("g129-11", "Integer");
		assertTrue(parser.optionExists("g1210"));
		assertFalse(parser.optionExists("g130"));
	}
	
	@Test
	public void decreasing_range() {
		parser.addAll("g3-1", "String");
		assertTrue(parser.optionExists("g2"));
	}
	
	@Test
	public void overlap() {
		parser.addAll("option1-2 option3-4", "o1-4", "Integer");
		parser.parse("-o4=3");
		assertEquals(parser.getInteger("--option4"), 3);
	}
	
	@Test
	public void invalid_group_range() {
		parser.addAll("g1234-7ab  o1-3", "s1  s2", "String Integer");
		assertTrue(parser.optionExists("o2"));
		assertFalse(parser.shortcutExists("s1"));
		assertTrue(parser.shortcutExists("s2"));
	}
	
	@Test
	public void types_match_group() {
		parser.addAll("g1-2  o1-3", "String Integer");
		String to_match = "Options Map: \n"+
		"{o1=Option[name:o1, value:, type:INTEGER],"+
				" o2=Option[name:o2, value:, type:INTEGER],"+
		" o3=Option[name:o3, value:, type:INTEGER],"+
				" g1=Option[name:g1, value:, type:STRING],"+
		" g2=Option[name:g2, value:, type:STRING]}\n"+
		"Shortcuts Map:\n"+"{}";
		assertEquals(parser.toString(), to_match);

	}
}

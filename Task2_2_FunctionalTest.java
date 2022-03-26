package st;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Task2_2_FunctionalTest {
	private Parser parser;
	@Before
	public void initialize() {
		parser = new Parser();
	}
	
	// [Bug #2 - Easy, 1PT]
	@Test
	public void test2() {
		parser.addOption(new Option("abc", Type.BOOLEAN), "a");
		parser.parse("--abc=0");
		assertFalse(parser.getBoolean("abc"));
	}
	
	// [Bug #10 - Easy, 1PT]
	@Test
	public void test3() {
		parser.addOption(new Option("a", Type.CHARACTER));
		assertEquals(parser.getCharacter("a"),('\0'));
	}
	// [Bug #4 - Medium, 2PTS]
	@Test
	public void test4() {
		parser.addOption(new Option("abc", Type.INTEGER), "aaaaaaaaaaaaaaaaaaaa");
		assertTrue(parser.shortcutExists("aaaaaaaaaaaaaaaaaaaa"));
	}
	
	// [Bug #17 - Hard, 3PTS]
	@Test
	public void test5() {
		parser.addOption(new Option("abcdefghijklmnopqrstuvwxyzabcdefghijk", Type.STRING));
		assertEquals(parser.getString("abcdefghijklmnopqrstuvwxyzabcdefghijk"),"");
	}
	// [Bug #1 - Easy, 1PT] & [Bug #7 - Hard, 3PTS]
	@Test
	public void test6() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc=5");
		assertEquals(parser.getInteger("abc"), 5);
	}
	
	// [Bug #11 - Hard, 3PTS], [Bug #1 - Easy, 1PT], [Bug #7 - Hard, 3PTS]
	@Test(expected = IllegalArgumentException.class)
	public void test7() {
		parser.addOption(new Option("abc#", Type.STRING));
	}
	
	// [Bug #1 - Easy, 1PT], [Bug #7 - Hard, 3PTS], [Bug #15 - Medium, 2PTS]
	@Test
	public void test8() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc=21474836475");
		assertEquals(parser.getInteger("abc"), 0);
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #3 - Medium, 2PTS]
	@Test
	public void test9() {
		parser.addOption(new Option("abc", Type.BOOLEAN));
		parser.parse("--abc=qwertyuiop");
		assertEquals(parser.getInteger("abc"), 1);
	}
	
	// [Bug #1 - Easy, 1PT], [Bug #14 -  Hard, 3PTS]
	@Test
	public void test10() {
		parser.addOption(new Option("abc", Type.BOOLEAN));
		parser.parse("--abc=\\n");
		assertEquals(parser.getString("abc"), "\\n");
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #18 - Easy, 1PT]
	@Test
	public void test11() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc= Old");
		parser.replace("abc       ", "Old", "New");
		assertEquals(parser.getString("abc"), "New");
	}
	
	// [Bug #12 - Hard, 3PTS]
	@Test
	public void test12() {
		parser.addOption(new Option("opt", Type.STRING), "opt");
		parser.parse("--opt= Old");
		parser.replace("-opt ", "Old", "New");
		assertEquals(parser.getString("opt"), "New");
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #9 - Easy, 1PT]
	@Test
	public void test13() {
		parser.addOption(new Option("w", Type.INTEGER));
		assertEquals(parser.parse(" "), 0);
	}
	
	// [Bug #13 - Medium, 2PTS], [Bug #20 - Hard, 3PTS]
	@Test
	public void test14() {
		parser.addOption(new Option("z", Type.STRING));
		parser.parse("--z == '='");
		assertEquals(parser.getString("z"), "==");
	}
	
	
	// [Bug #19 - Medium, 2PTS]
	@Test
	public void test16() {
		parser.addOption(new Option("b", Type.STRING),"b");
		parser.parse("b=\" -\"");
		assertEquals(parser.getString("b"), " -");
	}
	
	// [Bug #8 - Medium, 2PTS]
	@Test
	public void test17() {
		parser.addOption(new Option("d", Type.BOOLEAN), "d");
		parser.addOption(new Option("d", Type.INTEGER), "e");
		assertTrue(parser.shortcutExists("e"));
	}
	
	// [Bug #6 - Easy, 1PT]
	@Test
	public void test18() {
		Option opt1 = new Option("f", Type.BOOLEAN);
		Option opt2 = new Option("h", Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	
	// [Bug #5 - Medium, 2PTS]
	@Test
	public void test19() {
		parser.addOption(new Option("neg", Type.INTEGER));
		parser.parse("--neg=-1");
		assertEquals(parser.getInteger("neg"), -1);
	}
	
	// [Bug #16 - Medium, 2PTS]
	@Test(expected = NullPointerException.class)
	public void test20() {
		parser.getString(null);
	}	
	
	//----------------------------------------------- BRANCH COVERAGE -------------------------------------------------------------------
	@Test
	public void test21() {
		parser.addOption(new Option("isthere", Type.INTEGER));
		assertFalse(parser.shortcutExists("no"));
		assertTrue(parser.optionOrShortcutExists("isthere"));
		parser.setShortcut("isthere", "yes");
		assertTrue(parser.shortcutExists("yes"));
		assertTrue(parser.optionExists("isthere"));
		assertTrue(parser.optionOrShortcutExists("yes"));
		parser.setShortcut("isthere", "isthere");
		assertTrue(parser.optionOrShortcutExists("isthere"));
		assertFalse(parser.optionOrShortcutExists("no"));
	}
	
	@Test
	public void test22() {
		parser.addOption(new Option("cmd", Type.INTEGER));
		assertEquals(parser.parse(""), -2);
		assertEquals(parser.parse(null), -1);
	}
	
	@Test
	public void test23() {
		parser.addOption(new Option("cmd", Type.INTEGER));
		assertEquals(parser.parse(""), -2);
		assertEquals(parser.parse(null), -1);
	}
	
	@Test
	public void test24() {
		parser.addOption(new Option("replace", Type.STRING),"short");
		parser.parse("--replace= Old");
		parser.replace("--replace ", "Old", "New");
		parser.replace("replace", "New", "Old");
		parser.replace("short", "Old", "OldToo");
		assertEquals(parser.getString("replace"), "OldToo");
		parser.replace("--replace ", "OldToo", "New");
		assertEquals(parser.getString("replace"), "New");
	}
	
	@Test
	public void test25() {
		String st = parser.toString();
		HashMap<String, Option> empty = new HashMap<String, Option>();
		assertEquals(st, ("Options Map: \n" + empty + "\nShortcuts Map:\n" + empty));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test26() {
		parser.addOption(new Option(null, Type.INTEGER));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test27() {
		parser.addOption(new Option("", Type.INTEGER));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test28() {
		parser.addOption(new Option("validName", Type.INTEGER), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test29() {
		parser.addOption(new Option("validName", Type.INTEGER), "\\");
	}
	
	@Test
	public void test30() {
		Option test = new Option("opt", Type.BOOLEAN);
		test.setName("myBool");
		assertEquals(test.toString(), "Option[name:" + "myBool" + ", value:" + "" + ", type:" + "BOOLEAN"+ "]");
	}
	
	// BRANCH COVERAGE FOR Option.java
	@Test
	public void test31() {
		Option opt1 = new Option("opt1", Type.INTEGER);
		Option opt2 = new Option("opt2", Type.BOOLEAN);
		assertFalse(opt1.equals(null));
		assertFalse(opt1.equals(opt2));
		assertTrue(opt1.equals(opt1));
		Option opt3 = new Option("opt1", Type.BOOLEAN);
		assertFalse(opt2.equals(opt3));
		Option opt4 = new Option(null, Type.BOOLEAN);
		assertFalse(opt4.equals(opt2));
	}
	
	@Test (expected = NullPointerException.class)
	public void test32() {
		Option opt4 = new Option(null, Type.BOOLEAN);
		assertTrue(opt4.equals(opt4));
	}
	
	// BRANCH COVERAGE FOR OptionMap.java
	@Test (expected = RuntimeException.class)
	public void test33() {
		parser.getString("--nope");
	}
	
	@Test (expected = RuntimeException.class)
	public void test34() {
		parser.getString("-nope");
	}
	
	@Test (expected = RuntimeException.class)
	public void test35() {
		parser.getString("nope");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test36() {
		parser.addOption(new Option("hey", Type.NOTYPE));
	}
	
	@Test
	public void test37() {
		parser.addOption(new Option("hey", Type.STRING), "r");
		parser.addOption(new Option("hey", Type.STRING), "");
		assertEquals(parser.getString("hey"), "");
	}
	
	// BRANCH COVERAGE FOR Parser: Cover all switch
	@Test
	public void test38() {
		parser.addOption(new Option("o", Type.CHARACTER));
		parser.parse("--o=a");
		assertEquals(parser.getInteger("o"), 'a');
		parser.addOption(new Option("f", Type.BOOLEAN));
		assertEquals(parser.getInteger("f"), 0);
	}
	
	// Break Big Integer
	@Test
	public void test39() {
		parser.addOption(new Option("breakit", Type.INTEGER));
		parser.parse("breakit=9223372036854775808");
		assertEquals(parser.getInteger("breakit"), 0);
	}
	
	// Bools
	@Test
	public void test40() {
		parser.addOption(new Option("bool", Type.STRING));
		parser.parse("bool=\'r");
		parser.parse("bool=r\'");
		parser.parse("bool=r\"");
		parser.parse("bool=      ");
	}

}

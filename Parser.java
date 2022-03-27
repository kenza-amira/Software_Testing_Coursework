package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private OptionMap optionMap;
	
	public Parser() {
		optionMap = new OptionMap();
	}
	
	private ArrayList<String> remove_n_elements(ArrayList<String> splitted, int no){
		while (no != 0) {
			splitted.remove(splitted.size() - 1);
			no -= 1;
		}
		return splitted;
	}
	
	private Type what_type(String type_string){
		if(type_string.equals("Boolean")){
			return Type.BOOLEAN;
		}
		else if (type_string.equals("Character")) {
			return Type.CHARACTER;
		}
		else if (type_string.equals("Integer")) {
			return Type.INTEGER;
		}
		else if (type_string.equals("String")) {
			return Type.STRING;
		}
		return Type.NOTYPE;
	}
	
	private ArrayList<String> generate_sequence(String option_name, int s, int e, boolean isNum, boolean isReverse) {
		ArrayList<String> new_options = new ArrayList<String>();
		if (isNum) {
			if (isReverse) {
				for (int k = s; k>=e; k--) {
					String name = option_name + Integer.toString(k);
					new_options.add(name);
				}
			} else {
				for (int k = s; k<=e; k++) {
					String name = option_name + Integer.toString(k);
					new_options.add(name);
				}
			}
		} else {
			if (isReverse) {
				for (int k = e; k>=e; k--) {
					char c = (char)k;
					String name = option_name + Character.toString(c);
					new_options.add(name);
				}
			} else {
				for (int k = s; k<=e; k++) {
					char c = (char)k;
					String name = option_name + Character.toString(c);
					new_options.add(name);
				}
			}
		}
		return new_options;
	}
	
	private List<ArrayList<String>> group_initialize(String[] input, String[] types, boolean getTypes){
		int current_len = input.length;
		int j = 0;
		Pattern pattern = Pattern.compile("([A-Za-z0-9]){1}-([A-Za-z0-9])+");
		ArrayList<String> new_out = new ArrayList<String>();
		ArrayList<String> new_types = new ArrayList<String>();
		// GROUP INITIALIZATION
		while (j != current_len) {
			String entry = input[j];
			if (entry.matches("(([A-Za-z0-9_])+(([A-Z]-[A-Z]+)|[a-z]-[a-z]+|[0-9]-[0-9]+))")) {
				Matcher matcher = pattern.matcher(entry);
				matcher.find();
				String[] option_name = entry.split("([A-Za-z0-9]){1}-([A-Za-z0-9])+");
				String[] groups = entry.substring(matcher.start(), matcher.end()).split("-");
				if(groups[0].matches(("([0-9])+"))){
					
					int s = Integer.parseInt(groups[0]);
					int e = Integer.parseInt(groups[1]);
					if (getTypes) {
						int diff = Math.abs(s-e)+1;
						try{
							String type = types[j];
							new_types.addAll(Collections.nCopies(diff, type));
						}catch(IndexOutOfBoundsException e2){
							;
						}
					}
					if(s<e) {
						new_out.addAll(this.generate_sequence(option_name[0], s, e, true, false));
					} else {
						new_out.addAll(this.generate_sequence(option_name[0], s, e, true, true));
					}
				} else {
					int s = groups[0].charAt(0);
					int e = groups[1].charAt(0);
					if (getTypes) {
						int diff = Math.abs(s-e)+1;
						try{
							String type = types[j];
							new_types.addAll(Collections.nCopies(diff, type));
						}catch(IndexOutOfBoundsException e2){
							;
						}
					}
					if(s<e) {
						new_out.addAll(this.generate_sequence(option_name[0], s, e, false, false));
					} else {
						new_out.addAll(this.generate_sequence(option_name[0], s, e, false, true));
					}
				}
			} else {
				new_out.add(entry);
			}
			j += 1;
		}
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		out.add(new_out);
		out.add(new_types);
		return out;
	}
	public void addAll(String options, String shortcuts, String types) {
		if(!types.matches("(Integer|String|Character|Boolean){1}(\s)*((Integer|String|Character|Boolean){1}(\s)*)*")) {
			throw new IllegalArgumentException("Invalid type inputted");
		}
		String[] splitted_options = options.split("\\s+");
		String[] splitted_shortcuts = shortcuts.split("\\s+");
		String[] splitted_types = types.split("\\s+");
		
		List<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		ArrayList<String> spl_types = new ArrayList<String>();
		
		out = this.group_initialize(splitted_options, splitted_types, true);
		ArrayList<String> spl_options = out.get(0);
		if (out.get(1).size() > 0) {
			spl_types = out.get(1);
		} else {
			spl_types.addAll(Arrays.asList(splitted_types));
		}
		
		out = this.group_initialize(splitted_shortcuts, splitted_types, false);
		ArrayList<String> spl_shortcuts = out.get(0);
		
		if(spl_options.size() > spl_shortcuts.size()) {
			int diff = spl_options.size() - spl_shortcuts.size();
			spl_shortcuts.addAll(Collections.nCopies(diff, ""));
		}
		if(spl_options.size() < spl_shortcuts.size()) {
			int diff = spl_shortcuts.size() - spl_options.size();
			spl_shortcuts = this.remove_n_elements(spl_shortcuts, diff);
		}
		if(spl_types.size() > spl_options.size()) {
			int diff = spl_types.size() - spl_options.size();
			spl_types = this.remove_n_elements(spl_types, diff);
		}
		if(spl_types.size() < spl_options.size()) {
			int diff = spl_options.size() - spl_types.size();
			String toCopy = spl_types.get(spl_types.size()-1);
			spl_types.addAll(Collections.nCopies(diff, toCopy));
		}
		
		
		for(int i=0; i<spl_options.size();i++) {
			Option opt = new Option(spl_options.get(i),
					this.what_type(spl_types.get(i)));
			try {
				this.addOption(opt, spl_shortcuts.get(i));
			} catch(IllegalArgumentException e) {
				continue;
			}
			
		}
	}
	
	public void addAll(String options, String types) {
		this.addAll(options, "", types);
	}
		
	public void addOption(Option option, String shortcut) {
		optionMap.store(option, shortcut);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}

	
	private Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}
}

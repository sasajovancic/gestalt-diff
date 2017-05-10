package org.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordUtil {
	public static final char SIMPLE_DOUBLE_QUOTATION = '"';
    public static final char SIMPLE_SINGLE_QUOTATION = '\'';
    public static final char LEFT_ACCENT = '`';
    public static final char RIGHT_ACCENT = '´';
    public static final char LEFT_CURLY_SINGLE_QUTATION = '‘';
    public static final char RIGHT_CURLY_SINGLE_QUTATION = '’';
    public static final char LEFT_CURLY_DOUBLE_QUTATION = '“';
    public static final char RIGHT_CURLY_DOUBLE_QUTATION = '”';
    
    public static final char SIMPLE_SPACE = ' ';
    public static final char SPACE_SEPARATOR = '\u00A0';
    public static final char LINE_SEPARATOR = '\u2007';
    public static final char PARAGRAPH_SEPARATOR = '\u202F';
    
    
	public static String toSimpleQuotes(String str) {

        while (str.indexOf(LEFT_CURLY_SINGLE_QUTATION) > -1 || str.indexOf(RIGHT_CURLY_SINGLE_QUTATION) > -1 || str.indexOf(LEFT_ACCENT) > -1
                || str.indexOf(RIGHT_ACCENT) > -1 || str.indexOf(LEFT_CURLY_DOUBLE_QUTATION) > -1 || str.indexOf(RIGHT_CURLY_DOUBLE_QUTATION) > -1
                || str.indexOf(SPACE_SEPARATOR) > -1 || str.indexOf(LINE_SEPARATOR) > -1 || str.indexOf(PARAGRAPH_SEPARATOR) > -1) {
            if (str.indexOf(LEFT_CURLY_SINGLE_QUTATION) > -1) {
                str = str.replace(LEFT_CURLY_SINGLE_QUTATION, SIMPLE_SINGLE_QUOTATION);
            } else if (str.indexOf(RIGHT_CURLY_SINGLE_QUTATION) > -1) {
                str = str.replace(RIGHT_CURLY_SINGLE_QUTATION, SIMPLE_SINGLE_QUOTATION);
            } else if (str.indexOf(LEFT_ACCENT) > -1) {
                str = str.replace(LEFT_ACCENT, SIMPLE_SINGLE_QUOTATION);
            } else if (str.indexOf(RIGHT_ACCENT) > -1) {
                str = str.replace(RIGHT_ACCENT, SIMPLE_SINGLE_QUOTATION);
            } else if (str.indexOf(LEFT_CURLY_DOUBLE_QUTATION) > -1) {
                str = str.replace(LEFT_CURLY_DOUBLE_QUTATION, SIMPLE_DOUBLE_QUOTATION);
            } else if (str.indexOf(RIGHT_CURLY_DOUBLE_QUTATION) > -1) {
                str = str.replace(RIGHT_CURLY_DOUBLE_QUTATION, SIMPLE_DOUBLE_QUOTATION);
            } else if (str.indexOf(SPACE_SEPARATOR) > -1) {
                str = str.replace(SPACE_SEPARATOR, SIMPLE_SPACE);
            } else if (str.indexOf(LINE_SEPARATOR) > -1) {
                str = str.replace(LINE_SEPARATOR, SIMPLE_SPACE);
            } else if (str.indexOf(PARAGRAPH_SEPARATOR) > -1) {
                str = str.replace(PARAGRAPH_SEPARATOR, SIMPLE_SPACE);
            }
        }

        return str;
    }
	
	protected static List<String> splitString(String str) {
		char[] spliting = new char[] {' ', ',', '.', '<', '>', '=', '/', '\\', ':', ';', '-', '>', ':', '<'};
		Arrays.sort(spliting);
		
		StringBuilder sb = new StringBuilder();
		char[] strChars = str.toCharArray();
		
		List<String> res = new ArrayList<String>();
		for (char c : strChars) {
			if (Arrays.binarySearch(spliting, c) > -1) {
				if (sb.length() > 0) {
					res.add(sb.toString());
					sb = new StringBuilder();
				}
				sb.append(c);
				res.add(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(c);
			}
		}
		
		if (sb.length() > 0) {
			res.add(sb.toString());
		}
		
		return res;
	}
	
	public static Object[] mapWordsToCharacters(final String text1, final String text2) {
		// must be simple solution to store this
		Map<Character, Object> mapCharsToWords = new HashMap<Character, Object>();
		Map<String, Character> mapWordsToChars = new HashMap<String, Character>();
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
//		String[] parts1 = text1.split(" ");
//		String[] parts2 = text2.split(" ");
		List<String> parts1 = splitString(text1);
		List<String> parts2 = splitString(text2);
		
		char start = '0';
		char letter = start;
		char[] exclude = new char[] {'<', 'b', 'i', '>', '/'};
		Arrays.sort(exclude);
		for (String part : parts1) {
			part = toSimpleQuotes(part); 
			if (mapWordsToChars.containsKey(part)) {
				sb1.append(mapWordsToChars.get(part));
			} else {
				mapWordsToChars.put(part, letter);
				mapCharsToWords.put(letter, part);
				sb1.append(letter);
				letter++;
				while (Arrays.binarySearch(exclude, letter) > -1) {
					letter++;
				}
			}
			/*if (mapWordsToChars.containsKey(" ")) {
				sb1.append(mapWordsToChars.get(" "));
			} else {
				mapWordsToChars.put(" ", letter);
				mapCharsToWords.put(letter, " ");
				sb1.append(letter);
				letter++;
			}*/
		}
		
		for (String part : parts2) {
			part = toSimpleQuotes(part); 
			if (mapWordsToChars.containsKey(part)) {
				sb2.append(mapWordsToChars.get(part));
			} else {
				mapWordsToChars.put(part, letter);
				mapCharsToWords.put(letter, part);
				sb2.append(letter);
				letter++;
			}
			/*if (mapWordsToChars.containsKey(" ")) {
				sb2.append(mapWordsToChars.get(" "));
			} else {
				mapWordsToChars.put(" ", letter);
				mapCharsToWords.put(letter, " ");
				sb2.append(letter);
				letter++;
			}*/
		}
		
		
		return new Object[] { mapCharsToWords, sb1.toString(), sb2.toString()};
	}
	
	public static String[] listToStringResults(List<Diff> listOfChanges, Context context) {
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		for (Diff diff : listOfChanges) {
			
			if (diff.getOperation() == Operation.EQUAL) {
				sb1.append(context.getStartEqual());
				sb1.append(diff.getText());
				sb1.append(context.getEndEqual());
				
				sb2.append(context.getStartEqual());
				sb2.append(diff.getText());
				sb2.append(context.getEndEqual());
			} else if (diff.getOperation() == Operation.DELETE) {
				sb1.append(context.getStartDelete()).append(diff.getText()).append(context.getEndDelete());
			} else if (diff.getOperation() == Operation.INSERT) {
				sb2.append(context.getStartInsert()).append(diff.getText()).append(context.getEndInsert());
			} 
		}
		
		return new String[] {sb1.toString(), sb2.toString()};
	}
	
	public static String[] mapCharactersToWords(Map<Character, String> mapCharsToWords, List<Diff> listOfChanges, Context context) {
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		String decode;
		for (Diff diff : listOfChanges) {
			
			decode = decodeText(mapCharsToWords, diff.getTextChars());
			
			if (diff.getOperation() == Operation.EQUAL) {
				sb1.append(context.getStartEqual());
				sb1.append(decode);
				sb1.append(context.getEndEqual());
				
				sb2.append(context.getStartEqual());
				sb2.append(decode);
				sb2.append(context.getEndEqual());
			} else if (diff.getOperation() == Operation.DELETE) {
				sb1.append(context.getStartDelete()).append(decode).append(context.getEndDelete());
			} else if (diff.getOperation() == Operation.INSERT) {
				sb2.append(context.getStartInsert()).append(decode).append(context.getEndInsert());
			} 
		}
		
		return new String[] {sb1.toString(), sb2.toString()};
	}
	
	public static List<Diff> mapCharactersToWords(Map<Character, String> map,
			List<Diff> diffRes) {
		
		List<Diff> res = new ArrayList<Diff>();
		
		for (Diff diff : diffRes) {
			
			res.add(Diff.createNewSimpleDiff(decodeText(map, diff.getTextChars()).toCharArray(), diff.getOperation()));
			 
		}
		
		return res;
	}
	
	public static String decodeText(Map<Character, String> mapCharsToWords, char[] chrs) {
		StringBuilder sb = new StringBuilder();
		for (char c : chrs) {
			sb.append(mapCharsToWords.get(c));
		}
		return sb.toString();
	}

	public static int getRealLength(Map<Character, String> mapCharsToWords, char c) {
		if (mapCharsToWords == null) return 1;
		return mapCharsToWords.get(c).length();
	}
	
	public static int getRealLength(Map<Character, String> mapCharsToWords, char[] chars) {
		if (chars == null) {
			return 0;
		}
		
		if (mapCharsToWords == null) {
			return chars.length; 
		}
		
		int len = 0;
		
		for (char c : chars) {
			len += getRealLength(mapCharsToWords, c);
		}
		
		return len;
	}
}

package org.diff.gestalt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.diff.Context;
import org.diff.Diff;
import org.diff.DiffTypeOfDetails;
import org.diff.Operation;
import org.diff.TextPart;
import org.diff.WordUtil;


public class GestaltDiffImpl {
	
	public static String[] diffStrings(String text1, String text2, Context context) {
		List<Diff> res = null;
		if (context.getType() == DiffTypeOfDetails.CHAR) {
			res = diffListChars(text1, text2);
			return WordUtil.listToStringResults(res, context);
		} else if (context.getType() == DiffTypeOfDetails.WORD) {
			
			Object[] wordsToCharacters = WordUtil.mapWordsToCharacters(text1, text2);
			
			String wordText1 = (String) wordsToCharacters[1];
			String wordText2 = (String) wordsToCharacters[2];
			
			res = diffListChars(wordText1, wordText2);
			return WordUtil.mapCharactersToWords((Map<Character, String>) wordsToCharacters[0], res, context);
		}
		
		return null;
	}
	
	public static List<Diff> diffListWords(String text1, String text2) {
		Object[] wordsToCharacters = WordUtil.mapWordsToCharacters(text1, text2);
		
		String wordText1 = (String) wordsToCharacters[1];
		String wordText2 = (String) wordsToCharacters[2];
		char[] chars1 = wordText1.toCharArray();
		char[] chars2 = wordText2.toCharArray();
		
		List<Diff> diffRes = diffList((Map<Character, String>) wordsToCharacters[0], chars1, chars2);
		 
		return WordUtil.mapCharactersToWords((Map<Character, String>) wordsToCharacters[0], diffRes);
	}
	
	public static List<Diff> diffListChars(String text1, String text2) {
		return diffList(null, text1.toCharArray(), text2.toCharArray());
	}
	
	// Creates Diff for two texts, per char or per word 
	public static List<Diff> diffList(Map<Character, String> mapCharsToWords, char[] chars1, char[] chars2) {
		
		List<Diff> res = new ArrayList<Diff>();
		
		// if inputs are empty return empty list
		if (chars1 == null && chars2 == null || chars1.length == 0 && chars2.length == 0) {
			return res;
		}
		
		TextPart part = findLongesEqualStringWithImaginaryMatrix(mapCharsToWords, chars1, chars2);
		
		if (part == null) {
			// end of reqursion
			if (chars1 != null && chars1.length > 0) {
				res.add(Diff.createNewSimpleDiff(chars1, Operation.DELETE));
			}
			if (chars2 != null && chars2.length > 0) {
				res.add(Diff.createNewSimpleDiff(chars2, Operation.INSERT));
			}
		} else {
			// split text to pre and after EQUAL
			char[] orgPre = Arrays.copyOfRange(chars1, 0, part.getStart1());
			char[] orgAfter = Arrays.copyOfRange(chars1, part.getEnd1(), chars1.length);
			char[] chgPre = Arrays.copyOfRange(chars2, 0, part.getStart2());
			char[] chgAfter = Arrays.copyOfRange(chars2, part.getEnd2(), chars2.length);
			
			// do same for pre
			List<Diff> diffPre = diffList(mapCharsToWords, orgPre, chgPre);
			// do same for after
			List<Diff> diffAfter = diffList(mapCharsToWords, orgAfter, chgAfter);
			
			res.addAll(diffPre);
			res.add(part);
			res.addAll(diffAfter);
		}
		
		return res;
	}
	
	protected static TextPart findLongesEqualStringWithImaginaryMatrix(Map<Character, String> mapCharsToWords, char[] chars1, char[] chars2) {
		
		int max = chars1.length;
		if(chars2.length > max) {
			max = chars2.length;
		}
		
		TextPart found = null;
		
		int move = 0;
		
		int char1RealLength = WordUtil.getRealLength(mapCharsToWords, chars1);
		int char2RealLength = WordUtil.getRealLength(mapCharsToWords, chars2);
		
		while (move < max) {
			
			char[] moveChar1 = findRemainingChars(chars1, move);
			
			if (found == null || (mapCharsToWords == null && found.getLength() < Math.min(chars1.length - move, chars2.length)) 
					|| (mapCharsToWords != null && found.getRealLength() < Math.min(WordUtil.getRealLength(mapCharsToWords, moveChar1), char2RealLength))) {
				
				found = scanOneDiagonal(mapCharsToWords, chars1, chars2, move, 0, found);
			}
			
			if (move == 0) {
				move++;
				continue;
			}
			
			char[] moveChar2 = findRemainingChars(chars2, move);
			
			if (found == null || (mapCharsToWords == null && found.getLength() < Math.min(chars1.length, chars2.length - move)) 
					|| (mapCharsToWords != null && found.getRealLength() < Math.min(char1RealLength, WordUtil.getRealLength(mapCharsToWords, moveChar2)))) {
				
				found = scanOneDiagonal(mapCharsToWords, chars1, chars2, 0, move, found);
			}
			
			move++;
		}
		
		return found;
	}
	
	private static char[] findRemainingChars(char[] chars, int move) {
		char[] moveChar = null;
		if (move < chars.length) {
			moveChar = Arrays.copyOfRange(chars, move, chars.length);
		}
		
		return moveChar;
	} 
	
	private static TextPart scanOneDiagonal(Map<Character, String> mapCharsToWords, char[] chars1, char[] chars2, int startI, int startJ, TextPart oldFound) {
		
		int count = 0;
		int start1 = -1;
		int start2 = -1;
		
		// make found lenght temporary field in textpart
		TextPart found = oldFound;
		// int foundLength = 0;
		
		TextPart newFound = null;
		int newFoundLength = 0;
		char[] newFoundChars;
		
		int i = startI;
		int j = startJ;
		
		while(i < chars1.length && j < chars2.length) {
			//System.out.println(chars1[i] + "-" + chars2[j]);
			if (chars1[i] == chars2[j]) {
				if (count == 0) {
					start1 = i;
					start2 = j;
				}
				count++;
			} else {
				if (count > 0) {
					newFoundChars = Arrays.copyOfRange(chars1, start1, start1 + count);
					newFoundLength = WordUtil.getRealLength(mapCharsToWords, newFoundChars);
					newFound = TextPart.createNewEqualTextPart(start1, start2, newFoundChars, newFoundLength);
					
					if (found == null || newFound.isLongerThan(found)) {
						found = newFound;
					}
					count = 0;
				}
			}
			i++;
			j++;
		}
		if (count > 0) {
			newFoundChars = Arrays.copyOfRange(chars1, start1, start1 + count);
			newFoundLength = WordUtil.getRealLength(mapCharsToWords, newFoundChars);
			newFound = TextPart.createNewEqualTextPart(start1, start2, newFoundChars, newFoundLength);
			
			if (found == null || newFound.isLongerThan(found)) {
				found = newFound;
			}
			count = 0;
		}
		
		return found;
	}
}

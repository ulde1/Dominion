package de.tesd.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import de.tesd.collection.HashMap;
import de.tesd.collection.TreeSet;


/** Hlifsroutinen mit Strings
 * @author Roland M. Eppelt
 */
/**
 * @author Roland M. Eppelt
 *
 */
public class Strings {
	
	
	/** Liefert Anfang von s. 
	 * @param s String
	 * @param length Anzahl der gewünschten Zeichen bzw. falls negativ: Anzahl der hinten abzuschneidenden Zeichen. length wird automatisch auf [-s.length(), s.length()] reduziert.
	 * @return Anfang von s mit der Länge min(abs(length), s.length)
	 */
	public static String left(String s, int length) {
		int l = s.length();
		return length>=0 ? s.substring(0, Math.min(length, l)) : s.substring(0, Math.max(l+length, 0));  
	}
	
	
	/** Liefert Ende von s. 
	 * @param s String
	 * @param length Anzahl der gewünschten Zeichen bzw. falls negativ: Anzahl der vorne abzuschneidenden Zeichen. length wird automatisch auf [-s.length(), s.length()] reduziert.
	 * @return Ende von s mit der Länge min(abs(length), s.length)
	 */
	public static String right(String s, int length) {
		int l = s.length();
		return length>=0 ? s.substring(Math.max(l-length, 0), l) : s.substring(Math.min(-length, l), l);  
	}


	public static @Nullable String limit(@Nullable String s, int maxLength, String dots) {
		return s!=null && s.length()>maxLength ? s.substring(0,  maxLength-dots.length())+dots : s;
	}


	/**
	 * @param s {@link String}, in dem das Zeichen gesucht werden soll
	 * @param c {@link Character}, das gesucht wird
	 * @param additionalPositions Zusätzlich zurückzugebende Positionen
	 * @return Positionen, an denen {@link Character c} in {@link String s} vorkommt plus die additionalPositions. Jede Position höchstens einmal. Aufsteigend sortiert.*/
	public static int[] indexesOf(String s, char c, int... additionalPositions) {
		TreeSet<Integer> result = new TreeSet<>();
		for (int i = 0; i<s.length(); i++) {
			if (s.charAt(i)==c) {
				result.add(i);
			}
		}
		for (int i : additionalPositions) {
			result.add(i);
		}
		int[] array = result.stream().mapToInt(i -> i).toArray();
		Arrays.sort(array);
		return array;
	}
	
	
	/** Vergleicht, wie s linksbündig mit key übereinstimmt (ignore Case). 
	 * @see java.lang.String#compareToIgnoreCase(String)*/ 
	public static int compareLeft(String s, String key) {
		int result = s.substring(0, Math.min(s.length(), key.length())).compareToIgnoreCase(key); 
		return result;
	}
	
	
		/** Erste Position, an der sich a von b unterscheidet. Bei Gleichheit a.length(). */
	public static int firstDifference(String a, String b) {
		int i = 0;
		while (i<a.length() && i<b.length() && a.charAt(i)==b.charAt(i)) i++;
		return i;
	}
	
	
	/** Verbindet mehrere Strings durch delimiter. */
	static public String sentenceArray(String delimiter, boolean omitNulls, Object[] words) {
		assert words!=null : "words==null";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Object word : words) {
			if (!omitNulls || (word!=null && (!(word instanceof String) || !((String)word).isEmpty()))) {
				if (first) first = false; else sb.append(delimiter); 
				sb.append(word);
			} 
		}
		return sb.toString();
	}
	
	
	/** Verbindet mehrere Strings durch delimiter. */
	static public String sentence(String delimiter, boolean omitNulls, Object... words) {
		return sentenceArray(delimiter, omitNulls, words);
	}	
	
	
		/** Verbindet mehrere Strings durch delimiter. */
	static public String sentence(String delimiter, boolean omitNulls, String... words) {
		return sentence(delimiter, omitNulls, (Object[]) words);
	}

		
	public static String sentenceDelimiter = "⇥";
	
	
	/** Verbindet mehrere Strings durch "⇥". */
	static public String sentence(String... words) {
		return sentenceArray(sentenceDelimiter, false, words);
	}
	
	
	/** Analysiert Zahlenreihen. Trenner sind Leerzeichen, Komma, Strichpunkt. Mit "-" kann eine ganze Folge angegeben werden. 
	 * @param s Zahlenreihe, z.B. "1, 2 4  6-9 ; 14"
	 * @return Zahlen als long[], z.B. [1, 2, 4, 6, 7, 8, 9, 14];
	 * @throws IOException */
	static public long[] parseLongs(String s) throws IOException {
		String[] sl = s.replaceAll("\\s-\\s", "-").split("[\\s,;\\+]+");
			// Zählen
		int count = sl.length;
		for (int i = 0; i<sl.length; i++) // "a-b"
			if (sl[i].contains("-")) {
				String[] ssl = sl[i].split("-", 2);
				count += Long.parseLong(ssl[1])-Long.parseLong(ssl[0]);
			}
			// Parsen
		long[] result = new long[count];
		int p = 0;
		for (int i = 0; i<sl.length; i++) // "a-b"
			if (sl[i].contains("-")) {
				String[] ssl = sl[i].split("-", 2);
				long end = Long.parseLong(ssl[1]);
				for (long j=Long.parseLong(ssl[0]); j<=end; j++)
					result[p++] = j;
			} else {
				result[p++] = Long.parseLong(sl[i]);
			}
		return result;
	}
	
	
	/** Fügt s an, falls first==false
	 * @return false */
	public static boolean appendNotFirst(StringBuffer sb, String s, boolean first) {
		if (!first) sb.append(s);
		return false;
	}
	
	
	/** Fügt ein Komma an, falls first==false
	 * @return false
	 * @see {@link #appendNotFirst(StringBuffer, String, boolean)} */
	public static boolean appendKomma(StringBuffer sb, boolean first) { return appendNotFirst(sb, ", ", first); }
	

	/** Fügt {@link String} an {@link StringBuffer} an und falls beide nicht leer sind, dazwischen auch den Trenner.
	 * @param sb {@link StringBuffer}, an den angefügt wird
	 * @param delimiter Trenner, der dazwischengefügt wird, falls weder {@link StringBuffer} noch {@link String} leer sind.
	 * @param s {@link String}., der angefügt wird. Kann auch null oder leer sein.
	 */
	public static void appendWithDelimiter(StringBuffer sb, String delimiter, @Nullable String s) {
		if (s!=null && !s.isEmpty()) {
			if (sb.length()>0) {
				sb.append(delimiter);
			}
			sb.append(s);
		}
	}
	
	
	/** Fügt {@link String prefix} und {@link String string} an {@link StringBuffer sb} an und falls beide nicht leer sind, dazwischen auch den {@link String delimiter}.
	 * @param sb {@link StringBuffer}, an den angefügt wird
	 * @param delimiter {@link String} Trenner, der dazwischengefügt wird, falls weder {@link StringBuffer} noch {@link String} leer/null sind.
	 * @param prefix {@link String}, der angefügt wird, falls {@link String string} nicht leer/null ist.
	 * @param string {@link String}., der angefügt wird. Kann auch null oder leer sein.
	 */
	public static void appendWithDelimiterAndPrefix(StringBuffer sb, String delimiter, String prefix, @Nullable String string) {
		if (string!=null && !string.isEmpty()) {
			if (sb.length()>0) {
				sb.append(delimiter);
			}
			sb.append(prefix);
			sb.append(string);
		}
	}
	
	
	/**@param c Zeichen
	 * @param n Anzahl
	 * @return n mal das Zeichen c */
	public static String mul(char c, int n) {
		StringBuffer sb = new StringBuffer();
		append(sb, c, n);
		return sb.toString();
	}
	
	
	/**@param s String
	 * @param n Anzahl
	 * @return n mal der String s */
	public static String mul(String s, int n) {
		StringBuffer sb = new StringBuffer();
		append(sb, s, n);
		return sb.toString();
	}
	
	
	public static void append(StringBuffer sb, char c, int n) { for (int i = 0; i<n; i++) sb.append(c); }
	public static void append(StringBuffer sb, String s, int n) { for (int i = 0; i<n; i++) sb.append(s); }
	
	
	/** Sucht die kleinste gültige Nr und hängt diese an root an. */ 
	public static String appendNr(String root, Predicate<String> isOk) {
		int n = 1;
		while(true) {
			String result = root+n;
			if (isOk.test(result))
				return result;
			n++;
		}
	}


	/** Ersetzt Nicht-ASCII-Zeichen und die XML-Sonderzeichen &lt;, &gt;, &quot;, &amp;, &apos;
	 * durch die entsprechenden Escape-Sequenzen. */
	public static String xml(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			int c = s.codePointAt(i);
			switch (c) {
				case '<': sb.append("&lt;"); break;
				case '>': sb.append("&gt;"); break;
				case '\"': sb.append("&quot;"); break;
				case '&': sb.append("&amp;"); break;
				case '\'': sb.append("&apos;"); break;
				default:
					if (Character.getType(c)==Character.CONTROL)
						sb.append("&#"+c+";");
					else
						sb.appendCodePoint(c);
			}
		}
		return sb.toString();
	}


	/** Ersetzt die HTML-Sonderzeichen &lt;, &gt;, &quot;, &amp;, &apos;
	 * durch die entsprechenden Escape-Sequenzen. */
	public static String html(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			int c = s.codePointAt(i);
			switch (c) {
				case '<': sb.append("&lt;"); break;
				case '>': sb.append("&gt;"); break;
				case '\"': sb.append("&quot;"); break;
				case '&': sb.append("&amp;"); break;
				case '\'': sb.append("&apos;"); break;
				default:
					sb.appendCodePoint(c);
			}
		}
		return sb.toString();
	}


	/** Dekodiert Zeichenfolgen der Form "+" und "%xy". 
	 * @see java.net.URLDecoder */
	@SuppressWarnings("null")
	public static @NonNull String urlDecode(@NonNull String value) {
		try {
			return java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return e.getClass().getName()+": "+e.getMessage();
		}
	}
	
	
	/**  Enkodiert alles außer A-Za-z0-9 -_.* als %xy (und ' ' als '+').
	 * @see java.net.URLEncoder */
	@SuppressWarnings("null")
	public static @NonNull String urlEncode(@NonNull String value) {
		try {
			return java.net.URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return e.getClass().getName()+": "+e.getMessage();
		}
	}
	
	
	public static @Nullable String utf8Encode(@Nullable String s) {
		if (s==null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<s.length(); i++) {
			int c = s.charAt(i);
			if (c<0x20)
				sb.append(String.format("%%%02x", c));
			else if (c<0x7F)
				sb.appendCodePoint(c);
			else if (c<=0x7FF) {
				sb.append('%');
				sb.append(Integer.toHexString(c>>6 & 0x1F | 0xC0));
				sb.append('%');
				sb.append(Integer.toHexString(c & 0x3F | 0x80));
			} else if (c<=0xFFFF) {
				sb.append('%');
				sb.append(Integer.toHexString(c>>12 & 0x0F | 0xE0));
				sb.append('%');
				sb.append(Integer.toHexString(c>>6 & 0x3F | 0x80));
				sb.append('%');
				sb.append(Integer.toHexString(c & 0x3F | 0x80));
			} else { // if (c<=0x10FFFF)
				sb.append('%');
				sb.append(Integer.toHexString(c>>18 & 0x07 | 0xF0));
				sb.append('%');
				sb.append(Integer.toHexString(c>>12 & 0x3F | 0x80));
				sb.append('%');
				sb.append(Integer.toHexString(c>>6 & 0x3F | 0x80));
				sb.append('%');
				sb.append(Integer.toHexString(c & 0x3F | 0x80));
			}
		}
		return sb.toString();
	}


	/** Ersetzt "\", CR, LF und TAB durch "\\", "\r", "\n" und "\t". */
	public static @Nullable String escapeCrLfTab(@Nullable String s) {
		if (s==null) return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\\': sb.append("\\\\"); break;
				case '\r': sb.append("\\r"); break;
				case '\n': sb.append("\\n"); break;
				case '\t': sb.append("\\t"); break;
				default: sb.append(c);
			}
		}
		return sb.toString();
	}
	
	
	/** Ersetzt alle Regex-Steuerzeichen x durch \x */ 
	public static @Nullable String escapeRegex(@Nullable String s) {
		return s==null ? null : s.replaceAll("[-\\[\\]{}()*+?.,\\\\\\\\^$|#\\\\s]", "\\\\$0");
	}
	
	
	/** Ersetzt "\\", "\r", "\n" und "\t" durch "\", CR, LF und TAB. */
	public static String unescape(String s) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i<s.length()) {
			char c = s.charAt(i);
			if (c=='\\') {
				i++;
				if (i<s.length()) {
					c = s.charAt(i);
					switch (c) {
						case '\\': sb.append("\\"); break;
						case 'r': sb.append("\r"); break;
						case 'n': sb.append("\n"); break;
						case 't': sb.append("\t"); break;
						default:  sb.append('\\'); sb.append(c); // Unbekannte Escape-Sequenzen sollten eigentlich nicht vorkommen...
					}
				} else { 
					sb.append('\\'); // '\\' am String-Ende? Seltsam...
				}
			} else {
				sb.append(c);
			}
			i++;
		}
		return sb.toString();
	}
	
	
	/** Zerlegt eine URI in einzelne Parameter */
	static public Map<String, @Nullable String> mapRawQueryParams(@Nullable String rawQuery) throws UnsupportedEncodingException {
		HashMap<String, @Nullable String> result = new HashMap<String, @Nullable String>();
		if (rawQuery!=null) {
			String[] params = rawQuery.split("&");
			for (String param : params) {
					// siehe: http://stackoverflow.com/questions/2632175/java-decoding-uri-query-string
				String[] pair = URLDecoder.decode(param.replace("+", "%2B"), "UTF-8").replace("%2B", "+").split("=", 2);
				result.put(pair[0], pair.length>1 ? pair[1] : null);
			}
		}
		return result;
	}
	
	
	/** Liest InputStream in String ein (mittels {@link Scanner})
	 * @see Streams#readToString(InputStream, String) */
	public static String readFrom(InputStream is, String charSet) {
		Scanner scanner = new Scanner(is, charSet); 
		String s = scanner.useDelimiter("\\A").next();
		scanner.close();
		return s;
	}


	/** Liest UTF-8 kodierten InputStream in String ein (mittels {@link Scanner})
	 * @see Streams#readToString(InputStream)  */
	public static String readFrom(InputStream is) {
		return readFrom(is, "UTF-8"); 
	}

	
	public static ArrayList<String> loadStrings(InputStream inputStream) throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
			for (String s = in.readLine(); s!=null; s = in.readLine())
				result.add(s);
		}
		return result;
	}
	
	
	public static ArrayList<String> loadStrings(File file) throws IOException {
		ArrayList<String> result = new ArrayList<String>(); 
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (String s = in.readLine(); s!=null; s = in.readLine())
				result.add(s);
		}
		return result;
	}
	
	
	public static void saveStrings(String[] strings, File file) throws IOException {
		try (Writer out = new FileWriter(file)) {
			for (String s : strings) {
				out.write(s);
				out.write('\r');
			}
		}
	}
	
	
	public static int getLevenshteinDistance (String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}		 
		int n = s.length(); // length of s
		int m = t.length(); // length of t	
		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}
		int p[] = new int[n+1]; //'previous' cost array, horizontally
		int d[] = new int[n+1]; // cost array, horizontally
		int _d[]; //placeholder to assist in swapping p and d
			// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t
		char t_j; // jth character of t
		int cost; // cost
		for (i = 0; i<=n; i++) {
			 p[i] = i;
		}			
		for (j = 1; j<=m; j++) {
			 t_j = t.charAt(j-1);
			 d[0] = j;
			 for (i=1; i<=n; i++) {
					cost = s.charAt(i-1)==t_j ? 0 : 1;
					// minimum of cell to the left+1, to the top+1, diagonally left and up +cost				
					d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),	p[i-1]+cost);	
			 }
			 	// copy current distance counts to 'previous row' distance counts
			 _d = p;
			 p = d;
			 d = _d;
		} 
			// our last action in the above loop was to switch d and p, so p now 
			// actually has the most recent cost counts
		return p[n];
	}

	
	/** @return JSON-Value escaped */
	public static String toJSON(@Nullable String value, boolean quoted) {
		if (value==null || value.isEmpty())
			return quoted ? "\"\"" : "";
		int len = value.length();
		StringBuilder sb = new StringBuilder(len+4);
		if (quoted) {
			sb.append('"');
		}
		for (int i = 0; i<len; i += 1) {
			char c = value.charAt(i);
			switch (c) {
				case '\\':
				case '"':
				case '/':
					sb.append('\\');
					sb.append(c);
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\r':
					sb.append("\\r");
					break;
				default:
					if (c<' ') {
						String t = "000"+Integer.toHexString(c);
						sb.append("\\u"+t.substring(t.length()-4));
					} else {
						sb.append(c);
					}
			}
		}
		if (quoted) {
			sb.append('"');
		}
		return sb.toString();
	}
	
	
	/** @return JSON-Value in Quotes und escaped */
	public static String toJSON(@Nullable String value) {
		return toJSON(value, true);

	}
	
	
	/** @return s==null || s.toString().isEmpty() */
	public static boolean isNullOrEmpty(@Nullable Object value) {
		return value==null || value.toString().isEmpty();
	}
	
	
	/** @return value, falls weder null noch leer, sonst elseValue */
	public static String or(@Nullable String value, String elseValue) {
		return value!=null && !value.isEmpty() ? value : elseValue; 
	}
	
	
	public static int maxLength(String @Nullable [] values) {
		return values!=null ? 0 : Arrays.stream(values).mapToInt(s  -> s!=null ? s.length() : 0).max().orElse(0);
	}
	
	
	/**
	 * @param sb {@link StringBuffer}
	 * @return {@link String} oder null, falls {@link String#isEmpty() leer}
	 */
	public static @Nullable String toStringOrNull(StringBuffer sb) {
		return sb.length()==0 ? null : sb.toString();
	}


	/** @return value als Zahl im chars-System */
	public static String encode(String chars, int value) {
		StringBuffer sb = new StringBuffer();
		final int n = chars.length();
		do {
			sb.insert(0, chars.charAt(value%n));
			value /= n;
		} while (value!=0);
		return sb.toString();
	}


	/** Fängt s mit einem der prefixes an?
	 * @param s Zu prüfender {@link String}
	 * @param prefixes {@link String}s, mögliche Anfänge von s
	 * @return Fängt s mit einem der Prefixes an?
	 */
	public static boolean startsWith(String s, String... prefixes) {
		for (String prefix : prefixes) {
			if (s.startsWith(prefix))
				return true;
		}
		return false;
	}


	public static String cutPrefix(String string, String prefix) {
		return string.startsWith(prefix) ? string.substring(prefix.length()) : string;
	}
	

}

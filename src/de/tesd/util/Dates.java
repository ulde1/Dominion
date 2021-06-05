package de.tesd.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;


public class Dates {
	
	
	/** {@link SimpleDateFormat} für "yyyy-MM-dd" */
	public final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
	public final static SimpleDateFormat DDMMYYYY = new SimpleDateFormat("dd.MM.yyyy");
	public final static SimpleDateFormat DMMMMYYYY = new SimpleDateFormat("d. MMMM yyyy");
	public final static SimpleDateFormat DDMMYY = new SimpleDateFormat("dd.MM.yy");
	public final static SimpleDateFormat YYMMDD = new SimpleDateFormat("yyMMdd");
	public final static double DAY_MILLIS_DOUBLE = 86400000.0;
	public final static long DAY_MILLIS_LONG = 86400000;
	public final static int CENTURY = 100*(Calendar.getInstance().get(Calendar.YEAR)/100);
	public final static Date MIN = new Date(Long.MIN_VALUE);
	public final static Date MAX = new Date(Long.MAX_VALUE);
	
	
	public static Calendar beginOfDay(@Nullable Date date) {
		Calendar result = Calendar.getInstance();
		if (date!=null) {
			result.setTime(date);
		}
		setMidnight(result);
		return result;
	}
	
	
	public static Calendar beginOfNextDay(Date date) {
		Calendar result = beginOfDay(date);
		result.add(Calendar.DAY_OF_MONTH, 1);
		return result;
	}
	
	
	/** Setzt die Uhrzeit */
	public static void setTime(Calendar calendar, int hour, int minute, int second, int millis) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millis);
	}
	
	
	/** Setzt die Uhrzeit auf 00:00 */
	public static void setMidnight(Calendar calendar) {
		setTime(calendar, 0, 0, 0, 0);
	}
	
	
	/** @return Montag 00:00 der Kalenderwoche des angegeben Tages*/
	public static Calendar beginOfWeek(Date date) {
		Calendar result = Calendar.getInstance();
		result.setTime(date);
		result.set(Calendar.DAY_OF_WEEK, result.getFirstDayOfWeek());
		setMidnight(result);
		return result;
	}
	
	
	/** @return Montag 00:00 der folgenden Kalenderwoche */
	public static Calendar endOfWeek(Date date) {
		Calendar result = beginOfWeek(date);
		result.add(Calendar.DAY_OF_YEAR, 7);
		return result;
	}
	
	
	/** @return Montag 00:00 der angegeben Kalenderwoche */
	public static Calendar beginOfWeek(int year, int week) {
		Calendar result = Calendar.getInstance();
		result.set(Calendar.YEAR, year);
		result.set(Calendar.WEEK_OF_YEAR, week);
		result.set(Calendar.DAY_OF_WEEK, result.getFirstDayOfWeek());
		setMidnight(result);
		return result;
	}
	
	
	/** @return Montag 00:00 der folgenden Kalenderwoche */
	public static Calendar endOfWeek(int year, int week) {
		Calendar result = beginOfWeek(year, week);
		result.add(Calendar.DAY_OF_YEAR, 7);
		return result;
	}
	
	
	/** Liefert den nächsten „{@link DayOfWeek Wochentag}“, z.B. das Datum des nächsten Dienstags.
	 * @param after Anfangsdatum oder heute (falls null)
	 * @param wochentag der zu liefernde Wochentag
	 * @param orToday darf auch „heute” geliefert werden? Sonst: erst ab morgen
	 * @return der nächsten „{@link DayOfWeek Wochentag}“.
	 */
	public static Calendar next(@Nullable Date after, DayOfWeek wochentag, boolean orToday) {
		Calendar result = Calendar.getInstance();
		if (after!=null) {
			result.setTime(after);
		}
		setMidnight(result);
		int plusDays = (wochentag.getValue()+8-result.get(Calendar.DAY_OF_WEEK))%7;
		if (!orToday && plusDays==0) {
			plusDays = 7;
		}
		result.add(Calendar.DAY_OF_YEAR, plusDays);
		return result;
	}


	public static Calendar currentCentury(Date date) {
		Calendar result = Calendar.getInstance();
		result.setTime(date);
		result.set(Calendar.YEAR, CENTURY+result.get(Calendar.YEAR)%100);
		return result;
	}


	/** Parst Datum der Form 2011-12-03, 03.12.11, 03.12.2011, Dec. 3 11, 12/3/11, 20111203 */
	public static LocalDate parseLocalDate(String value) throws DateTimeParseException {
		try {
			return LocalDate.from(DateTimeFormatter.ISO_DATE.parse(value));
		} catch (DateTimeParseException e1) { try {
			return LocalDate.from(DateTimeFormatter.ofPattern("d.M.yy").parse(value)); // FormatStyle.SHORT mit Locale.GERMANY verlangt zweistellige Tage und Monate…
		} catch (DateTimeParseException e2) { try {
			return LocalDate.from(DateTimeFormatter.ofPattern("d.M.yyyy").parse(value)); // FormatStyle.MEDIUM mit Locale.GERMANY verlangt zweistellige Tage und Monate…
		} catch (DateTimeParseException e3) { try {
			return LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.US).parse(value)); // Das kann komischerweise mit einstelligen Tagen und Monaten umgehen…
		} catch (DateTimeParseException e4) { try {
			return LocalDate.from(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).parse(value)); // Das kann komischerweise mit einstelligen Tagen und Monaten umgehen…
		} catch (DateTimeParseException e5) {
			return LocalDate.from(DateTimeFormatter.BASIC_ISO_DATE.parse(value));
		}}}}}
	}
	
	
	public static LocalTime parseLocalTime(String value) throws DateTimeParseException {
		try {
			return LocalTime.from(DateTimeFormatter.ISO_TIME.parse(value));
		} catch (DateTimeParseException e1) {
			return LocalTime.from(DateTimeFormatter.ofPattern("H:m:s").parse(value));
		}
	}
	
	
	public static LocalDateTime parseLocalDateTime(String value) throws DateTimeParseException {
		try {
			return LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(value));
		} catch (DateTimeParseException e1) { try {
			return LocalDateTime.from(DateTimeFormatter.ofPattern("d.M.yy H:m:s").parse(value)); // FormatStyle.SHORT mit Locale.GERMANY verlangt zweistellige Tage und Monate…
		} catch (DateTimeParseException e2) { try {
			return LocalDateTime.from(DateTimeFormatter.ofPattern("d.M.yyyy H:m:s").parse(value)); // FormatStyle.MEDIUM mit Locale.GERMANY verlangt zweistellige Tage und Monate…
		} catch (DateTimeParseException e3) { try {
			return LocalDateTime.from(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US).parse(value)); // Das kann komischerweise mit einstelligen Tagen und Monaten umgehen…
		} catch (DateTimeParseException e4) { try {
			return LocalDateTime.from(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).parse(value)); // Das kann komischerweise mit einstelligen Tagen und Monaten umgehen…
		} catch (DateTimeParseException e5) {
			return LocalDateTime.from(DateTimeFormatter.BASIC_ISO_DATE.parse(value));
		}}}}}
	}
	
	
	/** @return z.B. 27. Juli – 3. August 1992 */ 
	public static String dmy(LocalDate von, LocalDate bis, TextStyle style, Locale locale) {
		StringBuffer sb = new StringBuffer();
		sb.append(von.getDayOfMonth());
		sb.append(".");
		Month monthA = von.getMonth();
		int yearA = von.getYear();
		Month monthB = bis.getMonth();
		int yearB = bis.getYear();
		if (monthA!=monthB || yearA!=yearB) {
			sb.append(" ");
			sb.append(monthA.getDisplayName(style, locale));
		}
		if (yearA!=yearB) {
			sb.append(" ");
			sb.append(yearA);
		}
		sb.append(" – ");
		sb.append(bis.getDayOfMonth());
		sb.append(". ");
		sb.append(monthB.getDisplayName(style, locale));
		sb.append(" ");
		sb.append(yearB);		
		return sb.toString();
	}
	
	
	public static String dmy(Date von, Date bis, TextStyle style, Locale locale) {
		return dmy(toLocalDate(von), toLocalDate(bis), style, locale);
	}	
	
	
	public static String weekDescription(Calendar calendar) {
		StringBuffer sb = new StringBuffer("KW ");
		sb.append(calendar.get(Calendar.WEEK_OF_YEAR));
		sb.append(" (");
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(".");
		int monthA = calendar.get(Calendar.MONTH)+1;
		int yearA = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.DAY_OF_MONTH, 6);
		try {
			int monthB = calendar.get(Calendar.MONTH)+1;
			int yearB = calendar.get(Calendar.YEAR);
			if (monthA!=monthB) {
				sb.append(monthA);
				sb.append(".");
			}
			if (yearA!=yearB)
				sb.append(yearA);
			sb.append("-");
			sb.append(calendar.get(Calendar.DAY_OF_MONTH));
			sb.append(".");
			sb.append(monthB);
			sb.append(".");
			sb.append(yearB);
			sb.append(")");
		} finally {
			calendar.add(Calendar.DAY_OF_MONTH, -6);
		}
		return sb.toString();
	}
	
	
	public static String weekDescription(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return weekDescription(calendar);
	}
	
	
	public static String weekDescription(int year, int week) {
		return weekDescription(beginOfWeek(year, week));
	}


	public static Calendar beginOfQuarter(int year, int quarter) {
		Calendar result = Calendar.getInstance();
		result.set(Calendar.YEAR, year);
		result.set(Calendar.MONTH, quarter*3);
		result.set(Calendar.DAY_OF_MONTH, 1);
		setMidnight(result);
		return result;
	}
	
	
	public static Calendar endOfQuarter(int year, int quarter) {
		return quarter==3 ? beginOfQuarter(year+1, 0) : beginOfQuarter(year, quarter+1);
	}
	
	
	public static Calendar of(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, day);
		return calendar;
	}
	
	
	public static Calendar endOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, 15);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar;
	}
	
	
	public static Calendar nextBeginOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		return calendar;
	}


	public static Integer age(LocalDate birth) {
		return Period.between(birth, LocalDate.now()).getYears();
	}
	
	
	public static @Nullable Integer ageOrNull(@Nullable LocalDate birth) {
		return O.nn(birth, d -> age(d));
	}
	
	
	public static Integer age(Date birth) {
		return age(toLocalDate(birth));
	}
	
	
	public static @Nullable Integer ageOrNull(@Nullable Date birth) {
		return O.nn(birth, d -> age(d));
	}
	
	
	public static double daysBetween (Date start, Date end) {
		return (end.getTime()-start.getTime())/DAY_MILLIS_DOUBLE;
	}
	
	
	public static @Nullable Double daysBetweenOrNull(@Nullable Date start, @Nullable Date end) {
		if (start==null || end==null)
			return null;
		return daysBetween(start, end);		
	}
	
	
	public static long daysBetween(LocalDate start, LocalDate end) {
		return ChronoUnit.DAYS.between(start, end);		
	}
	
	
	public static @Nullable Long daysBetweenOrNull(@Nullable LocalDate start, @Nullable LocalDate end) {
		if (start==null || end==null)
			return null;
		return daysBetween(start, end);		
	}
	
	
	/** @return {@link Date} gestern 0:00 Uhr */
	public static Date yesterday() {
		return Dates.from(LocalDate.now().plusDays(-1L));
	}


	/** Fügt genau 24 Stunden*days hinzu. Das kann bei Zeitumstellungen zu Problemen führen. */
	public static Date addDays(Date date, double days) {
		return new Date(date.getTime()+(long) (days*DAY_MILLIS_DOUBLE));
	}
	
	
	/** Fügt genau 24 Stunden*days hinzu. Das kann bei Zeitumstellungen zu Problemen führen. */
	
	public static @Nullable Date addDaysOrNull(@Nullable Date date, @Nullable Double days) {
		if (date==null || days==null)
			return date;
		return addDays(date, days);
	}
	
	
	public static Date addDays(Date date, int days) {
		return from(toLocalDateTime(date).plusDays(days));
	}
	
	
	public static @Nullable Date addDaysOrNull(@Nullable Date date, @Nullable Integer days) {
		if (date==null || days==null)
			return date;
		return addDays(date, days);
	}
	
	
	/** Ermittelt den nächsten {@link Date} mit dieser Uhrzeit. 
	 * @param time {@link LocalTime} die gewünschte Uhrzeit
	 * @param safetySeconds Sekunden Sicherheitsabstand, die das ermittelte {@link Date}mindestens in der Zukunft liegen muss
	 * @return der nächste {@link Date} mit dieser Uhrzeit
	 */
	public static Date nextDateOf(LocalTime time, long safetySeconds) {
		LocalTime now = LocalTime.now().plusSeconds(2L);
		LocalDate today = LocalDate.now();
		return Dates.from(time.atDate(time.isBefore(now) ? today.plusDays(1L) : today));
	}

	
	public static Period period(Date startDateInclusive, Date endDateExclusive) {
		return Period.between(toLocalDate(startDateInclusive), toLocalDate(endDateExclusive));
	}
	
	
	public static @Nullable Period periodOrNull(@Nullable Date startDateInclusive, @Nullable Date endDateExclusive) {
		if (startDateInclusive==null || endDateExclusive==null)
			return null;
		else
			return period(startDateInclusive, endDateExclusive);
	}
	
	
	public static Period period(LocalDateTime startDateInclusive, LocalDateTime endDateExclusive) {
		return Period.between(startDateInclusive.toLocalDate(), endDateExclusive.toLocalDate());
	}
	
	
	public static @Nullable Period periodOrNull(@Nullable LocalDateTime startDateInclusive, @Nullable LocalDateTime endDateExclusive) {
		if (startDateInclusive==null || endDateExclusive==null)
			return null;
		else
			return period(startDateInclusive, endDateExclusive);
	}
	
	
	public static Date from(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	
	public static @Nullable Date fromOrNull(@Nullable LocalDateTime date) {
		return O.nn(date, d -> from(d));
	}
	
	
	public static Date from(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	
	public static @Nullable Date fromOrNull(@Nullable LocalDate date) {
		return O.nn(date, d -> from(d));
	}
	
	
	public static LocalDate toLocalDate(Date date) {
		if (date instanceof java.sql.Date)
			return ((java.sql.Date) date).toLocalDate();
		else
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	
	public static @Nullable LocalDate toLocalDateOrNull(@Nullable Date date) {
		return O.nn(date, d -> toLocalDate(d));
	}
	
	
	public static @Nullable LocalDate toLocalDateOrNull(@Nullable LocalDateTime date) {
		return O.nn(date, d -> d.toLocalDate());
	}
	
	
	public static LocalDateTime toLocalDateTime(Date date) {
		if (date instanceof java.sql.Date)
			return LocalDateTime.ofInstant(new Date(date.getTime()).toInstant(), ZoneId.systemDefault());
		else
			return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	
	public static @Nullable LocalDateTime toLocalDateTimeOrNull(@Nullable Date date) {
		return O.nn(date, d -> toLocalDateTime(d));
	}
	
	
	protected static void appendInt(StringBuilder sb, int n, String unit, String units) {
		if (n>0 && sb.length()<10) {
			if (sb.length()>0)
				sb.append(" ");
			sb.append(n);
			sb.append(" ");
			sb.append(n==1 ? unit : units);
		}
	}


	/** Übergibt das {@link Date} 10x an {@link String#format(String, Object...)}. 
	 * @param formatString siehe {@link Formatter}
	 * @param date Date, das im FormatString bis zu 10x benutzt werden kann
	 * @return {@link String#format(String, Object...)} */
	public static String format(String formatString, Date date) {
		Object[] dates = new Object[10];
		Arrays.fill(dates, date);
		return String.format(formatString, dates);
	}
	
	
	public static String format(Period period) {
		StringBuilder result = new StringBuilder();
		appendInt(result, period.getYears(), "Jahr", "Jahre");
		appendInt(result, period.getMonths(), "Monat", "Monate");
		appendInt(result, period.getDays(), "Tag", "Tage");
		return result.toString();
	}
	
	
	public static String formatDuration(long millis) {
		StringBuilder result = new StringBuilder();
		Calendar duration = Calendar.getInstance();
		duration.setTimeInMillis(millis);
		appendInt(result, duration.get(Calendar.YEAR)-1970, "Jahr", "Jahre");
		appendInt(result, duration.get(Calendar.MONTH), "Monat", "Monate");
		appendInt(result, duration.get(Calendar.DAY_OF_MONTH)-1, "Tag", "Tage");
		appendInt(result, duration.get(Calendar.HOUR_OF_DAY)-1, "Stunde", "Stunden");
		appendInt(result, duration.get(Calendar.MINUTE), "Minute", "Minuten");
		appendInt(result, duration.get(Calendar.SECOND), "Sekunde", "Sekunden");
		appendInt(result, duration.get(Calendar.MILLISECOND), "ms", "ms");
		return result.toString();
	}


	public static String dmy(LocalDate date) {
		return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
	}


	public static String dmy(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
	}


	/** @return "02.10.2019" */
	public static String dmy(Date date) {
		return DDMMYYYY.format(date);
	}


	/** @return "2. Oktober 2019" */
	public static String dMy(Date date) {
		return DMMMMYYYY.format(date);
	}


	public static String ymd(LocalDate date) {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}


	public static String ymd(LocalDateTime date) {
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}


	/** @return {@link Date} im Format {@link #YYYYMMDD} */
	public static String ymd(Date date) {
		return YYYYMMDD.format(date);
	}


	public static String ymdhms(Date date) {
		return YYYYMMDDHHMMSS.format(date);
	}


	public static String yymmdd(Date date) {
		return YYMMDD.format(date);
	}
	
	
	/** @return {@link Date} zu ddmmyyyy oder null, falls null oder blank */
	public static @Nullable Date fromDDMMYYYY(@Nullable String ddmmyyyy) throws ParseException {
		return Strings.isNullOrEmpty(ddmmyyyy) ? null : DDMMYYYY.parse(ddmmyyyy);
	}


	/** @return den jüngsten/spätesten/letzten Zeitpunkt oder {@link LocalDateTime#MIN} */
	public static LocalDateTime max(@Nullable LocalDateTime... values) {
		LocalDateTime result = LocalDateTime.MIN;
		for (LocalDateTime value : values) {
			if (value!=null && value.isAfter(result))
				result = value;
		}
		return result;
	}


	/** @return den jüngsten/spätesten/letzten Zeitpunkt oder null */
	public static @Nullable Date max(@Nullable Date... values) {
		Date min = new Date(0L);
		Date result = min;
		for (Date value : values) {
			if (value!=null && value.after(result))
				result = value;
		}
		return result==min ? null : result;
	}


	/** @return den ältesten/frühesten/ersten Zeitpunkt oder {@link LocalDateTime#MIN} */
	public static LocalDateTime min(@Nullable LocalDateTime... values) {
		LocalDateTime result = LocalDateTime.MAX;
		for (LocalDateTime value : values) {
			if (value!=null && value.isBefore(result))
				result = value;
		}
		return result;
	}


	/** @return den ältesten/frühesten/ersten Zeitpunkt oder null */
	public static @Nullable Date min(@Nullable Date... values) {
		Date max = new Date(Long.MAX_VALUE);
		Date result = max;
		for (Date value : values) {
			if (value!=null && value.before(result))
				result = value;
		}
		return result==max ? null : result;
	}


	/** Gibt {@link Date} zwischen min und max zurück. 
	 * @param value {@link Date} gewünschter Zeitpunkt oder jetzt, falls null
	 * @param min {@link Date}, das nicht unterschritten werden darf (optional)
	 * @param max {@link Date}, das nicht überschritten werden darf (optional, nicht vor min)
	 * @return {@link Date} zwischen min und max
	 */
	public static Date toRange(@Nullable Date value, @Nullable Date min, @Nullable Date max) {
		Date result = O.or(value, Date::new);
		if (min!=null && result.before(min)) {
			result = min;
		}
		if (max!=null && result.after(max)) {
			result = max;
		}
		return result;
	}


	/** Gibt {@link LocalDateTime} zwischen min und max zurück. 
	 * @param value {@link LocalDateTime} gewünschter Zeitpunkt oder jetzt, falls null
	 * @param min {@link LocalDateTime}, das nicht unterschritten werden darf (optional)
	 * @param max {@link LocalDateTime}, das nicht überschritten werden darf (optional, nicht vor min)
	 * @return {@link LocalDateTime} zwischen min und max
	 */
	public static LocalDateTime toRange(@Nullable LocalDateTime value, @Nullable LocalDateTime min, @Nullable LocalDateTime max) {
		LocalDateTime result = O.or(value, LocalDateTime::now);
		if (min!=null && result.isBefore(min)) {
			result = min;
		}
		if (max!=null && result.isAfter(max)) {
			result = max;
		}
		return result;
	}


}

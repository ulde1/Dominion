package de.tesd.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;


public class Streams {
	
	
		/* Java 8 Streams */
	
	
	/** Verbindet mehrere Streams miteinander. Für zwei {@link Stream} kann direkt {@link Stream#concat(Stream, Stream)} genutzt werden.
	 * @param streams
	 * @return Diese {@link Stream}s nacheinander
	 * @see Stream#concat(Stream, Stream)
	 */
	@SafeVarargs
	public static <T> Stream<T> concat(Stream<? extends T>... streams) {
//    	Stream<? extends T> result = streams[0];
//    	for (int i=1; i<streams.length; i++)
//    		result = Stream.concat(result, streams[i]);
//        return (@NonNull Stream<T>) result;
// eleganter:		
		return Arrays.stream(streams).flatMap(s -> s);
    }
	

	/** Filtert Objekte einer {@link Class} und mappt den {@link Stream} aus diese Class. */ 
	public static <A, B> Stream<B> cast(Class<B> bClass, Stream<A> stream) {
		return stream.filter(o -> bClass.isInstance(o)).map(o -> bClass.cast(o));
	}

	
	public static class StreamIterator<T> implements Iterable<T> {
		protected Stream<T> stream;
		public StreamIterator(Stream<T> stream) { this.stream = stream; }
		@Override public Iterator<T> iterator() {
			return stream.iterator();
		}
	}
	
	
	
	public static final NullOutputStream nullOutputStream = new NullOutputStream();
	public static final int DEFAULT_BUFFER_SIZE = 4096;
	
	
		/* SaveToFile */

	
	public static long saveToFile(InputStream is, String fileName) throws IOException {
		return saveToFile(is, -1, new File(fileName), false);
	}

	public static long saveToFile(InputStream is, long fileSize, String fileName) throws IOException {
		return saveToFile(is, fileSize, new File(fileName), false);
	}
	
	public static long saveToFile(InputStream is, File file) throws IOException {
		return saveToFile(is, -1, file, false);
	}

	public static long saveToFile(InputStream is, long fileSize, File file, boolean append) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, append);
		long count = copy(is, fileSize, fos);
		fos.close();
		if (!append && file.length() != count)
			throw new IOException("File-Transmission of " + file.getName() + " failed. Copied: "+count+" Size: " + file.length());
		if (fileSize>0 && count != fileSize)
			throw new IOException("File-Transmission of " + file.getName() + " failed. Copied: " + count + " Expected: " + fileSize);
		return count;
	}
	
	
		/* Copy */

	
	public static long copy(InputStream is, OutputStream os) throws IOException {
		return copy(is, -1, os, DEFAULT_BUFFER_SIZE);
	}

	public static long copy(InputStream is, long size, OutputStream os) throws IOException {
		return copy(is, size, os, DEFAULT_BUFFER_SIZE);
	}

	public static long copy(InputStream is, OutputStream os, int bufferSize) throws IOException {
		return copy(is, -1, os, bufferSize);
	}

	public static long copy(InputStream is, long size, OutputStream os, int bufferSize) throws IOException {
		byte[] bytes = new byte[bufferSize];
		long countTotal = 0;
		int countStep = is.read(bytes, 0, size < 0 ? bufferSize : (int) Math.min(bufferSize, size - countTotal));
		while (countStep > 0 && (size < 0 || countTotal < size)) {
			countTotal += countStep;
			os.write(bytes, 0, countStep);
			if (size < 0 || countTotal < size) {
				countStep = is.read(bytes, 0, size < 0 ? bufferSize : (int) Math.min(bufferSize, size - countTotal));
			} else {
				break;
			}
		}
		return countTotal;
	}

	
	public static long copy(Reader r, Writer w) throws IOException {
		return copy(r, -1, w, DEFAULT_BUFFER_SIZE);
	}

	public static long copy(Reader r, long size, Writer w) throws IOException {
		return copy(r, size, w, DEFAULT_BUFFER_SIZE);
	}

	public static long copy(Reader r, Writer w, int bufferSize) throws IOException {
		return copy(r, -1, w, bufferSize);
	}

	public static long copy(Reader r, long size, Writer w, int bufferSize) throws IOException {
		char[] chars = new char[bufferSize];
		long countTotal = 0;
		int countStep = r.read(chars, 0, size < 0 ? bufferSize : (int) Math.min(bufferSize, size - countTotal));
		while (countStep > 0 && (size < 0 || countTotal < size)) {
			countTotal += countStep;
			w.write(chars, 0, countStep);
			if (size < 0 || countTotal < size) {
				countStep = r.read(chars, 0, size < 0 ? bufferSize : (int) Math.min(bufferSize, size - countTotal));
			} else {
				break;
			}
		}
		return countTotal;
	}
	
	
	public static long copy(InputStream is, Writer w) throws IOException {
		return copy(new InputStreamReader(is, "UTF-8"), w);
	}
	
	
	public static long copy(String srcFile, String destFile) throws IOException {
		try (FileInputStream is = new FileInputStream(srcFile)) {
			try (FileOutputStream os = new FileOutputStream(destFile)) {
				return copy(is, os);
			}
		}
	}
	
	
	/** Liest {@link InputStream} mittels {@link StringWriter} in {@link StringBuffer} ein. Der {@link InputStream} wird nicht geschlossen. */
	public static StringBuffer readToString(InputStream inputStream) throws IOException {
		StringWriter stringWriter = new StringWriter();
		copy(inputStream, stringWriter);
		return stringWriter.getBuffer();
	}
	
	
	/** Liest {@link InputStream} mittels {@link StringWriter} in {@link StringBuffer} ein. Der {@link InputStream} wird nicht geschlossen.*/
	@SuppressWarnings("resource")
	public static StringBuffer readToString(InputStream inputStream, @Nullable String charsetName) throws IOException {
		InputStreamReader inputStreamReader = charsetName!=null ? new InputStreamReader(inputStream, charsetName) : new InputStreamReader(inputStream);
		StringWriter stringWriter = new StringWriter();
		copy(inputStreamReader, stringWriter);
		return stringWriter.getBuffer();
	}
	
	
	/** Stellt einen {@link String} als {@link InputStream} bereit. */
	public static InputStream streamFromString(String s) {
		try {
			return new ByteArrayInputStream(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not known", e);
		}
	}
	
	
	public static byte[] toBytes(InputStream inputStream) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			copy(inputStream, outputStream);
			return outputStream.toByteArray();
		}
	}


	
		/* equals */
	
	
	public static boolean equals(InputStream is1, boolean close1, InputStream is2, boolean close2, int bufferSize) throws IOException {
		byte[] buffer1 = new byte[bufferSize], buffer2 = new byte[bufferSize];
		int count1 = 0, count2 = 0, p1 = 0, p2 = 0;
		do {
			if (p1==count1) { count1 = is1.read(buffer1); p1 = 0; }
			if (p2==count2) { count2 = is2.read(buffer2); p2 = 0; }
			for (int i = 0; i<Math.min(count1-p1, count2-p2); i++)
				if (buffer1[p1++]!=buffer2[p2++]) return false;
			
		} while (count1>=0 && count2>=0);
		if (close1) is1.close();
		if (close2) is2.close();
		return count1<0 && count2<0 && p1==0 && p2==0;
	}

	public static boolean equals(InputStream is1, boolean close1, InputStream is2, boolean close2) throws IOException {
		return equals(is1, close1, is2, close2, DEFAULT_BUFFER_SIZE);
	}
	public static boolean equals(InputStream is1, InputStream is2) throws IOException {
		return equals(is1, false, is2, false, DEFAULT_BUFFER_SIZE);
	}
	
	
		/* NullOutputStream */
	
	
	static public class NullOutputStream extends OutputStream {
		@Override public void write(int b) {}
		@Override public void write(byte @Nullable [] b) {}
		@Override public void write(byte @Nullable [] b, int off, int len) {}
	}


}
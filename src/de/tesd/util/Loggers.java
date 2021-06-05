package de.tesd.util;


import java.util.function.Supplier;
import java.util.logging.Level;

import org.eclipse.jdt.annotation.Nullable;


public interface Loggers {
	
	
	static void logp(java.util.logging.Logger logger, Level level, @Nullable Throwable t, Supplier<String> msgSupplier) {
		if (logger.isLoggable(level)) {
			StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
			logger.logp(level, stackTraceElement.getClassName(), stackTraceElement.getMethodName(), t, msgSupplier);
		}
	}
	
	
	static void logp(java.util.logging.Logger logger, Level level, @Nullable String msg, @Nullable Throwable t) {
		if (logger.isLoggable(level)) {
			if (msg==null && t!=null)
				msg = t.getClass().getName()+": "+t.getMessage();
			StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
			logger.logp(level, stackTraceElement.getClassName(), stackTraceElement.getMethodName(), msg, t);
		}
	}
	
	
	static void finest(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.FINEST, null, msgSupplier);
	}

	
	static void finer(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.FINER, null, msgSupplier);
	}

	
	static void fine(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.FINE, null, msgSupplier);
	}

	
	static void config(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.CONFIG, null, msgSupplier);
	}

	
	static void info(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.INFO, null, msgSupplier);
	}

	
	static void warning(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.WARNING, null, msgSupplier);
	}

	
	static void severe(java.util.logging.Logger logger, Supplier<String> msgSupplier) {
		logp(logger, Level.SEVERE, null, msgSupplier);
	}

	
	static void info(java.util.logging.Logger logger, @Nullable String message, Throwable e) {
		logp(logger, Level.INFO, message, e);
	}


	static void warning(java.util.logging.Logger logger, @Nullable String message, Throwable e) {
		logp(logger, Level.WARNING, message, e);
	}
	
	
	static void severe(java.util.logging.Logger logger, @Nullable String message, Throwable e) {
		logp(logger, Level.SEVERE, message, e);
	}
	
	
	static void info(java.util.logging.Logger logger, Throwable e) {
		logp(logger, Level.INFO, null, e);
	}


	static void warning(java.util.logging.Logger logger, Throwable e) {
		logp(logger, Level.WARNING, null, e);
	}
	
	
	static void severe(java.util.logging.Logger logger, Throwable e) {
		logp(logger, Level.SEVERE, null, e);
	}
	
	
		// ========== QLogger ==========


	java.util.logging.Logger logger();


	default void finest(Supplier<String> msgSupplier) {
		logp(logger(), Level.FINEST, null, msgSupplier);
	}

	
	default void finest(Throwable e) {
		logp(logger(), Level.FINEST, null, e);
	}


	default void finest(String msg, Throwable e) {
		logp(logger(), Level.FINEST, msg, e);
	}
	
	
	default void finer(Supplier<String> msgSupplier) {
		logp(logger(), Level.FINER, null, msgSupplier);
	}

	
	default void finer(Throwable e) {
		logp(logger(), Level.FINER, null, e);
	}


	default void finer(String msg, Throwable e) {
		logp(logger(), Level.FINER, msg, e);
	}
	
	
	default void fine(Supplier<String> msgSupplier) {
		logp(logger(), Level.FINE, null, msgSupplier);
	}

	
	default void fine(Throwable e) {
		logp(logger(), Level.FINE, null, e);
	}


	default void fine(String msg, Throwable e) {
		logp(logger(), Level.FINE, msg, e);
	}
	
	
	default void config(Supplier<String> msgSupplier) {
		logp(logger(), Level.CONFIG, null, msgSupplier);
	}

	
	default void config(Throwable e) {
		logp(logger(), Level.CONFIG, null, e);
	}


	default void config(String msg, Throwable e) {
		logp(logger(), Level.CONFIG, msg, e);
	}
	
	
	default void info(Supplier<String> msgSupplier) {
		logp(logger(), Level.INFO, null, msgSupplier);
	}

	
	default void info(Throwable e) {
		logp(logger(), Level.INFO, null, e);
	}


	default void info(String msg, Throwable e) {
		logp(logger(), Level.INFO, msg, e);
	}
	
	
	default void warning(Supplier<String> msgSupplier) {
		logp(logger(), Level.WARNING, null, msgSupplier);
	}

	
	default void warning(String msg) {
		logp(logger(), Level.WARNING, msg, null);
	}

	
	default void warning(Throwable e) {
		logp(logger(), Level.WARNING, null, e);
	}
	
	
	default void warning(String msg, Throwable e) {
		logp(logger(), Level.WARNING, msg, e);
	}
	
	
	default void severe(String msg) {
		logp(logger(), Level.SEVERE, msg, null);
	}

	
	default void severe(Supplier<String> msgSupplier) {
		logp(logger(), Level.SEVERE, null, msgSupplier);
	}

	
	default void severe(Throwable e) {
		logp(logger(), Level.SEVERE, null, e);
	}
	
	
	default void severe(String msg, Throwable e) {
		logp(logger(), Level.SEVERE, msg, e);
	}
	
	
}

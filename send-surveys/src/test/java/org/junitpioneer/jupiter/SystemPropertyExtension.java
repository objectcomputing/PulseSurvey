/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junitpioneer.jupiter;

import static java.util.stream.Collectors.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

public class SystemPropertyExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback {

	private static final Namespace NAMESPACE = Namespace.create(SystemPropertyExtension.class);
	private static final String BACKUP = "Backup";

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		handleSystemProperties(context);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		if (annotationsPresentOnTestMethod(context)) {
			handleSystemProperties(context);
		}
	}

	private boolean annotationsPresentOnTestMethod(ExtensionContext context) {
		//@formatter:off
		return context.getTestMethod()
				.map(testMethod -> AnnotationSupport.isAnnotated(testMethod, ClearSystemProperty.class)
						|| AnnotationSupport.isAnnotated(testMethod, ClearSystemProperties.class)
						|| AnnotationSupport.isAnnotated(testMethod, SetSystemProperty.class)
						|| AnnotationSupport.isAnnotated(testMethod, SetSystemProperties.class))
				.orElse(false);
		//@formatter:on
	}

	private void handleSystemProperties(ExtensionContext context) {
		Set<String> propertiesToClear;
		Map<String, String> propertiesToSet;
		try {
			propertiesToClear = findRepeatableAnnotations(context, ClearSystemProperty.class).stream()
					// collect to map because the collector throws an IllegalStateException on
					// duplicate keys, which is desired behavior;
					// see comment SystemPropertyExtensionTests.ConfigurationFailureTests.shouldFailWhenClearSameSystemPropertyTwice
					.collect(toMap(ClearSystemProperty::key, __ -> "")).keySet();
			propertiesToSet = findRepeatableAnnotations(context, SetSystemProperty.class).stream().collect(
				toMap(SetSystemProperty::key, SetSystemProperty::value));
			preventMultiplePropertyMutations(propertiesToClear, propertiesToSet.keySet());
		}
		catch (IllegalStateException ex) {
			throw new ExtensionConfigurationException("Don't clear/set the same property more than once.", ex);
		}
		storeOriginalSystemProperties(context, propertiesToClear, propertiesToSet.keySet());
		clearSystemProperties(propertiesToClear);
		setSystemProperties(propertiesToSet);
	}

	private void preventMultiplePropertyMutations(Collection<String> propertiesToClear,
			Collection<String> propertiesToSet) {
		// @formatter:off
		propertiesToClear.stream()
				.filter(propertiesToSet::contains)
				.reduce((k0, k1) -> k0 + ", " + k1)
				.ifPresent(duplicateKeys -> {
					throw new IllegalStateException(
							"Cannot clear and set the following system properties at the same time: " + duplicateKeys);
				});
		// @formatter:on
	}

	private <A extends Annotation> List<A> findRepeatableAnnotations(ExtensionContext context,
			Class<A> annotationType) {
		// @formatter:off
		return context.getElement()
				.map(element -> AnnotationSupport.findRepeatableAnnotations(element, annotationType))
				.orElseGet(Collections::emptyList);
		// @formatter:on
	}

	private void storeOriginalSystemProperties(ExtensionContext context, Collection<String> clearProperties,
			Collection<String> setProperties) {
		context.getStore(NAMESPACE).put(BACKUP, new SystemPropertyBackup(clearProperties, setProperties));
	}

	private void clearSystemProperties(Collection<String> clearProperties) {
		clearProperties.forEach(System::clearProperty);
	}

	private void setSystemProperties(Map<String, String> setProperties) {
		setProperties.entrySet().forEach(
			propertyWithValue -> System.setProperty(propertyWithValue.getKey(), propertyWithValue.getValue()));
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if (annotationsPresentOnTestMethod(context)) {
			restoreOriginalSystemProperties(context);
		}
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		restoreOriginalSystemProperties(context);
	}

	private void restoreOriginalSystemProperties(ExtensionContext context) {
		context.getStore(NAMESPACE).get(BACKUP, SystemPropertyBackup.class).restoreProperties();
	}

	/**
	 * Stores which system properties need to be cleared or set to their old values after the test.
	 */
	private static class SystemPropertyBackup {

		private final Map<String, String> propertiesToSet;
		private final Collection<String> propertiesToUnset;

		public SystemPropertyBackup(Collection<String> clearProperties, Collection<String> setProperties) {
			propertiesToSet = new HashMap<>();
			propertiesToUnset = new HashSet<>();
			// @formatter:off
			Stream.concat(clearProperties.stream(), setProperties.stream())
					.forEach(property -> {
						String backup = System.getProperty(property);
						if (backup == null)
							propertiesToUnset.add(property);
						else
							propertiesToSet.put(property, backup);
					});
			// @formatter:on
		}

		public void restoreProperties() {
			// @formatter:off
			propertiesToSet
					.entrySet()
					.forEach(propertyWithValue -> System.setProperty(
							propertyWithValue.getKey(),
							propertyWithValue.getValue()));
			propertiesToUnset
					.forEach(System::clearProperty);
			// @formatter:on
		}

	}

}

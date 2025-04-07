package com.test.automation.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG listener to modify test annotations at runtime
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /**
     * This method is called for every test during the test run
     * 
     * @param annotation The annotation being transformed
     * @param testClass The test class
     * @param testConstructor The test constructor
     * @param testMethod The test method
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Set retryAnalyzer for all test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
} 
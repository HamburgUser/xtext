package org.eclipse.xtend.core.tests.validation

import org.eclipse.xtend.core.tests.AbstractXtendTestCase
import com.google.inject.Inject
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import static org.eclipse.xtend.core.xtend.XtendPackage$Literals.*
import static org.eclipse.xtend.core.validation.IssueCodes.*

class ModifierValidationTest extends AbstractXtendTestCase {
	
	@Inject extension ValidationTestHelper
	
	@Test def void testClassAllowedModifiers() {
		clazz('''private class Foo{}''').assertError(XTEND_CLASS, INVALID_MODIFIER)
		clazz('''package class Foo{}''').assertError(XTEND_CLASS, INVALID_MODIFIER)
		clazz('''protected class Foo{}''').assertError(XTEND_CLASS, INVALID_MODIFIER)
		clazz('''public class Foo{}''').assertNoErrors
		clazz('''static class Foo{}''').assertNoErrors		
		clazz('''abstract class Foo{}''').assertNoErrors
		clazz('''dispatch class Foo{}''').assertError(XTEND_CLASS, INVALID_MODIFIER)		
		clazz('''final class Foo{}''').assertNoErrors		
	}
	
	@Test def void testAnnotationTypeAllowedModifiers() {
		annotationType('''private annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)
		annotationType('''package annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)
		annotationType('''protected annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)
		annotationType('''public annotation Foo{}''').assertNoErrors
		annotationType('''static annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)
		annotationType('''abstract annotation Foo{}''').assertNoErrors		
		annotationType('''dispatch annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)		
		annotationType('''final annotation Foo{}''').assertError(XTEND_ANNOTATION_TYPE, INVALID_MODIFIER)
	}
	
	@Test def void testMethodAllowedModifiers() {
		function('''private def foo() {}''').assertNoErrors
		function('''package def foo() {}''').assertNoErrors
		function('''protected def foo() {}''').assertNoErrors
		function('''public def foo() {}''').assertNoErrors
		function('''static def foo() {}''').assertNoErrors		
		function('''abstract def foo()''').assertNoError(INVALID_MODIFIER)	
		function('''dispatch def foo (int i){}''').assertNoErrors		
		function('''final def foo() {}''').assertNoErrors		
	}

	@Test def void testConstructorAllowedModifiers() {
		constructor('''private new() {}''').assertNoErrors
		constructor('''package new() {}''').assertNoErrors
		constructor('''protected new() {}''').assertNoErrors
		constructor('''public new() {}''').assertNoErrors
		constructor('''static new() {}''').assertError(XTEND_CONSTRUCTOR, INVALID_MODIFIER)		
		constructor('''abstract new() {}''').assertError(XTEND_CONSTRUCTOR, INVALID_MODIFIER)
		constructor('''dispatch new (){}''').assertError(XTEND_CONSTRUCTOR, INVALID_MODIFIER)
		constructor('''final new() {}''').assertError(XTEND_CONSTRUCTOR, INVALID_MODIFIER)
	}
	
	@Test def void testFieldAllowedModifiers() {
		field('''private int foo''').assertNoErrors
		field('''package int foo''').assertNoErrors
		field('''protected int foo''').assertNoErrors
		field('''public int foo''').assertNoErrors
		field('''static int foo''').assertNoErrors		
		field('''abstract int foo''').assertError(XTEND_FIELD, INVALID_MODIFIER)
		field('''dispatch int foo''').assertError(XTEND_FIELD, INVALID_MODIFIER)
		field('''final int foo = 42''').assertNoErrors
		field('''extension int foo''').assertNoErrors
	}
	
	@Test def void testDuplicateModifier() {
		function('''private private def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''package package def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''protected protected def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''public public def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''static static def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)		
		function('''abstract abstract def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)		
		function('''dispatch dispatch def foo (int i){}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)		
		function('''final final def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)		
	}

	@Test def void testDuplicateVisibility() {
		function('''private package def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''package protected def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''protected public def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''public private def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		
	}

	@Test def void testStaticVsAbstract() {
		field('''abstract static int foo=42''').assertError(XTEND_FIELD, INVALID_MODIFIER)
		field('''static abstract int foo=42''').assertError(XTEND_FIELD, INVALID_MODIFIER)
	}

	@Test def void testFinalVsAbstract() {
		function('''abstract final def foo() ''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''final abstract def foo() ''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
	}
	
	@Test def void testAbstractVsNoBody() {
		function('''abstract def foo() {}''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''abstract def foo()''').assertNoError(INVALID_MODIFIER)
	}
	
	@Test def void testFinalVsNoBody() {
		function('''final def foo()''').assertError(XTEND_FUNCTION, INVALID_MODIFIER)
		function('''final def foo() {}''').assertNoErrors
	}
	
	@Test def void testFinalValVsVar() {
		field('''final var int i=42''').assertError(XTEND_FIELD, INVALID_MODIFIER)
		field('''var final int i=42''').assertError(XTEND_FIELD, INVALID_MODIFIER)
		field('''final val int i=42''').assertNoErrors
		field('''val final int i=42''').assertNoErrors
	}
}
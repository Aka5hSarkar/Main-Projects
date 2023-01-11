package com.cms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class CMS_Test {
	@TestFactory
	public Collection<DynamicTest> validateMarksTest(){
		return Arrays.asList(dynamicTest("validateMarks Positive Test",()->assertTrue(CMS_Loader.validateMarks(75, 75))));
	}
	@Test
	public void validateMarksNegativeTest() {
		assertFalse(CMS_Loader.validateMarks(10,20));
	}
}

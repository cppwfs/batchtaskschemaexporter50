package io.spring.batchschema;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
@Suite
@SelectPackages({
	"io.spring.batchschema.simplefailedtask",
	"io.spring.batchschema.simplesuccesstask",
	"io.spring.batchschema.singlejobexitcode",
	"io.spring.batchschema.singlejobmultistep",
	"io.spring.batchschema.singlejobmultistepfailedsecondstep",
	"io.spring.batchschema.singlejobmultistepwithjobparam",
	"io.spring.batchschema.singlejobsinglestep"
})
@SelectClasses(Batchschema30ApplicationTests.class)
public class AllTests {
}

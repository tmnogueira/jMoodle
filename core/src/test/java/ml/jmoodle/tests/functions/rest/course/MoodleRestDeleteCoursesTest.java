package ml.jmoodle.tests.functions.rest.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import ml.jmoodle.commons.MoodleCourse;
import ml.jmoodle.configs.MoodleConfig;
import ml.jmoodle.functions.MoodleWSFunctionFactory;
import ml.jmoodle.functions.MoodleWSFunctions;
import ml.jmoodle.functions.exceptions.MoodleWSFucntionException;
import ml.jmoodle.functions.rest.core.course.MoodleRestDeleteCourses;

/**
 * Delete Course(s) Function
 *
 *
 * @author Carlos Alexandre S. da Fonseca
 * @copyrigth © 2018 Carlos Alexandre S. da Fonseca
 * @license https://opensource.org/licenses/MIT - MIT License
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MoodleRestDeleteCoursesTest  {
	
	private static final String MOODLE_TEST_URL = "http://teste.com";
	@Mock
	MoodleConfig mc;


	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("ml.jmoodle.tests");
	}
	
	@Before
	public void beforeEach() throws MalformedURLException {
		URL testUrl = new URL(MOODLE_TEST_URL);
		when(mc.getMoodleURL()).thenReturn(testUrl);
		when(mc.getVersion()).thenReturn(new String ("2.3.0"));
	}

	@Test
	public void get_instance_without_errors_test() {
		try {
			MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_COURSE_DELETE_COURSES, mc
			);
		} catch (MoodleWSFucntionException e) {
			fail("An exception was throwed"+e.getMessage());
		}
	}

	@Test
	public void exception_if_moodle_version_is_lower_than_2_3_0_test () {
		when(mc.getVersion()).thenReturn(new String ("2.2.9"));
		try {
			MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_COURSE_DELETE_COURSES, mc
			);
		} catch (MoodleWSFucntionException e) {
			assertNotNull(e);
			return;
		}
		fail("No exception was throwed");
	}

	
	
	@Test
	public void verify_since_version_test() throws MoodleWSFucntionException {
		MoodleRestDeleteCourses cc = (MoodleRestDeleteCourses) MoodleWSFunctionFactory.getFunction(
			MoodleWSFunctions.CORE_COURSE_DELETE_COURSES, mc
		);
		assertEquals("2.3.0", cc.getSinceVersion());
	}

	@Test
	public void exception_if_no_course_is_set_test() {
		try {
			MoodleRestDeleteCourses cc = (MoodleRestDeleteCourses) MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_COURSE_DELETE_COURSES, mc
			);
			cc.getFunctionData();
		} catch (MoodleWSFucntionException e) {
			assertNotNull(e);
			return;
		}
		fail("No exception was throwed");
	}

	
	@Test
	public void verify_function_data_test() throws UnsupportedEncodingException, MoodleWSFucntionException {
	
		MoodleRestDeleteCourses cc = (MoodleRestDeleteCourses) MoodleWSFunctionFactory.getFunction(
			MoodleWSFunctions.CORE_COURSE_DELETE_COURSES, mc
		);
		Set<MoodleCourse> mcs = new HashSet<>(Fixture.from(MoodleCourse.class).gimme(2, "valid"));
		cc.setCourses(mcs);
		String dataString = URLDecoder.decode(cc.getFunctionData(), MoodleConfig.DEFAULT_ENCODING);
		
		assertTrue(dataString.contains("wsfunction="+cc.getFunctionName()));
		assertTrue(dataString.contains("courseids[0]"));
		assertTrue(dataString.contains("courseids[1]"));
		assertTrue(dataString.contains("="));
		assertTrue(dataString.contains("&"));

		mcs.stream().forEach(mc -> {
			assertTrue(dataString.contains(String.valueOf(mc.getId())));
		});
		
		
	}
	
}



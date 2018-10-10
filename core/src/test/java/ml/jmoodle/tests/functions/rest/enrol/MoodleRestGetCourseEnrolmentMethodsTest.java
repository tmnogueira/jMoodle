package ml.jmoodle.tests.functions.rest.enrol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import ml.jmoodle.commons.MoodleCourse;
import ml.jmoodle.commons.MoodleEnrolmentMethod;
import ml.jmoodle.configs.MoodleConfig;
import ml.jmoodle.configs.expections.MoodleConfigException;
import ml.jmoodle.functions.MoodleWSFunctionFactory;
import ml.jmoodle.functions.MoodleWSFunctions;
import ml.jmoodle.functions.exceptions.MoodleWSFucntionException;
import ml.jmoodle.functions.rest.core.enrol.MoodleRestGetCourseEnrolmentMethods;
import ml.jmoodle.functions.rest.core.enrol.exceptions.MoodleRestGetCourseEnrolmentMethodsException;

/**
 * Get list of course ids that a user is enrolled in (if allowed to)
 *
 *
 * @author Carlos Alexandre S. da Fonseca
 * @copyrigth © 2018 Carlos Alexandre S. da Fonseca
 * @license https://opensource.org/licenses/MIT - MIT License
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MoodleRestGetCourseEnrolmentMethodsTest  {
	
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
		when(mc.getVersion()).thenReturn(new String ("3.0.0"));
	}
	
	@Test
	public void verify_since_version_test() throws MoodleWSFucntionException {
		MoodleRestGetCourseEnrolmentMethods cc = (MoodleRestGetCourseEnrolmentMethods) MoodleWSFunctionFactory.getFunction(
			MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
		);
		assertThat(cc.getSinceVersion()).isEqualTo("3.0.0");
	}

	@Test
	public void get_instance_without_errors_test() {
		try {
			assertThat(MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
			)).isInstanceOf(MoodleRestGetCourseEnrolmentMethods.class);
		} catch (MoodleWSFucntionException e) {
			fail("An exception was throwed"+e.getMessage());
		}
	}

	@Test
	public void exception_if_moodle_version_is_lower_than_3_0_0_test () {
		when(mc.getVersion()).thenReturn(new String ("2.9.9"));
		try {
			MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
			);
		} catch (MoodleWSFucntionException e) {
			assertNotNull(e);
			return;
		}
		fail("No exception was throwed");
	}

	
	@Test
	public void verify_function_name_test () throws MoodleWSFucntionException {
		
		MoodleRestGetCourseEnrolmentMethods cc = (MoodleRestGetCourseEnrolmentMethods) MoodleWSFunctionFactory.getFunction(
			MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
		);
		assertThat(cc.getFunctionName()).isEqualTo("core_enrol_get_course_enrolment_methods");
	}

	@Test
	public void exception_if_course_is_not_set() {
		try {
			MoodleRestGetCourseEnrolmentMethods func = (MoodleRestGetCourseEnrolmentMethods) MoodleWSFunctionFactory.getFunction(
				MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
			);
			func.getFunctionData();

		} catch (MoodleWSFucntionException e) {
			assertNotNull(e);
			return;
		}
		fail("No exception was throwed");
		
	}

	@Test
	public void verify_function_data_test() throws UnsupportedEncodingException, MoodleWSFucntionException {
	
		MoodleRestGetCourseEnrolmentMethods cc = (MoodleRestGetCourseEnrolmentMethods) MoodleWSFunctionFactory.getFunction(
			MoodleWSFunctions.CORE_ENROL_GET_COURSE_ENROLMENT_METHODS, mc
		);
		MoodleCourse entity = Fixture.from(MoodleCourse.class).gimme("valid");
		cc.setCourse(entity);
		
		String dataString = URLDecoder.decode(cc.getFunctionData(), MoodleConfig.DEFAULT_ENCODING);
		
		assertTrue(dataString.contains("wsfunction="+cc.getFunctionName()));
		assertTrue(dataString.contains("[courseid]="+entity.getId()));
	}

	@Test
	public void verify_process_respose_test() throws MoodleWSFucntionException, MoodleConfigException, SAXException, IOException, ParserConfigurationException {
	
		TestMoodleFunctionWarpClass cc = new TestMoodleFunctionWarpClass(mc);
		Set<MoodleEnrolmentMethod> mcs = new HashSet<>(Fixture.from(MoodleEnrolmentMethod.class).gimme(15, "getEnrolledMethods"));
		
		Document response = EnrolFunctionsFixtureTemplate.getCourseEnrolmentMethodsResponse(mcs);
		Set<MoodleEnrolmentMethod> resp = cc.processResponse(response);
		doProcessResponseAssertions(mcs, resp);
	}
	
	
	private void doProcessResponseAssertions(Set<MoodleEnrolmentMethod> expected, Set<MoodleEnrolmentMethod> actual) {
		assertThat(actual).hasSize(expected.size());
		for (MoodleEnrolmentMethod entiy : expected) {
			assertThat(actual).contains(entiy);
		}
		
	}


	class TestMoodleFunctionWarpClass extends MoodleRestGetCourseEnrolmentMethods {
		
		public TestMoodleFunctionWarpClass(MoodleConfig moodleConfig) throws MoodleWSFucntionException, MoodleConfigException {
				super(moodleConfig);
			}
		
		public Set<MoodleEnrolmentMethod> processResponse(Document response) throws MoodleRestGetCourseEnrolmentMethodsException   {
			return super.processResponse(response);
		}
		
			
	}
			
}
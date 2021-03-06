package ml.jmoodle.tests.functions.rest.user.tools;

import static org.assertj.core.api.Assertions.assertThat;
// import static com.google.common.truth.Truth8.assertThat;
// import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import ml.jmoodle.commons.DescriptionFormat;
import ml.jmoodle.commons.MoodleCourse;
import ml.jmoodle.commons.MoodleGroup;
import ml.jmoodle.commons.MoodleRole;
import ml.jmoodle.commons.MoodleUser;
import ml.jmoodle.commons.UserCustomField;
import ml.jmoodle.commons.UserCustomField.CustomFieldType;
import ml.jmoodle.commons.UserPreference;
import ml.jmoodle.configs.MoodleConfig;
import ml.jmoodle.functions.rest.core.course.exceptions.MoodleRestCreateCoursesException;
import ml.jmoodle.functions.rest.core.user.tools.MoodleUserTools;
import net.beaconhillcott.moodlerest.commons.UserEnrolledCourse;

public class MoodleUserToolsTest {
	MoodleUser entity;
	Set<MoodleUser> entities;
	MoodleUserTools tool;

	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("ml.jmoodle.tests");
	}
	@Before
	public void beforeEach() {
        entity = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		entities = new LinkedHashSet<>(Fixture.from(MoodleUser.class).gimme(15, "MoodleRestCreateUserFunctionUser"));
		tool =  new MoodleUserTools();
	}
	@Test
	public void test_serialize() throws MoodleRestCreateCoursesException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, UnsupportedEncodingException {
		
		doSerializeAssertions(URLDecoder.decode(tool.serialize(entities), MoodleConfig.DEFAULT_ENCODING),
			entities);
	}

	@Test
	public void test_to_entity() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Map<String, Object> m = new HashMap<>();
		m.put("id", "1");
		m.put("username", "username");
		m.put("fullname", "test user");
		m.put("auth", "1");
		m.put("email", "e-mail");

		Set<Map<String, Object>> upSet = getPreferences();
		Set<Map<String, Object>> ucfSet = getCustomfields();
		Set<Map<String, Object>> uecSet = getEnrolledCourses();
		Set<Map<String, Object>> grpsSet = getGroups();
		Set<Map<String, Object>> rlsSet = getRoles();

		m.put("preferences", upSet);
		m.put("customfields", ucfSet);
		m.put("enrolledcourses", uecSet);
		m.put("roles", rlsSet);
		m.put("groups", grpsSet);

		MoodleUser muTest = tool.toEntity(m);
		assertThat(m.get("id")).isEqualTo(muTest.getId().toString());
		
		assertThat(m.get("username")).isEqualTo(muTest.getUsername());
		assertThat(m.get("fullname")).isEqualTo(muTest.getFullname());
		assertThat(m.get("auth")).isEqualTo(muTest.getAuth());
		assertThat(m.get("email")).isEqualTo(muTest.getEmail());
		
		List<UserPreference> preferenceLst = Arrays.asList(muTest.getPreferences());
		
		assertThat(preferenceLst.size()).isEqualTo(2);
		doPreferencesAssertion(upSet, preferenceLst);
		

		List<UserCustomField> customLst = Arrays.asList(muTest.getCustomfields());
		assertThat(customLst.size()).isEqualTo(2);
		doCustomFieldsAssertion(ucfSet, customLst);

		List<MoodleCourse> enrollLst = Arrays.asList(muTest.getEnrolledCourses());
		assertThat(enrollLst.size()).isEqualTo(2);
		doEnrolledCourseAssertion(uecSet, enrollLst);

		List<MoodleGroup> grpLst = Arrays.asList(muTest.getGroups());
		assertThat(grpLst.size()).isEqualTo(2);
		doGroupAssertion(grpsSet, grpLst);

		List<MoodleRole> roleLst = Arrays.asList(muTest.getRoles());
		assertThat(roleLst.size()).isEqualTo(2);
		doRoleAssertion(rlsSet, roleLst);
		
	}

	private Set<Map<String, Object>> getGroups() {
		MoodleGroup mg1 = new MoodleGroup(23l, 12l, "asjkne", "asjwkl qd kd kqj", "");
		
		MoodleGroup mg2 = new MoodleGroup(45l, 142l, "asgvgjkne", "afevsjwkl fffqd kd kqj", "");
		
		
		Map<String, Object> mg1map = new HashMap<>();
		mg1map.put("id", mg1.getId().toString());
		mg1map.put("courseid", mg1.getCourseId().toString());
		mg1map.put("name", mg1.getName());
		mg1map.put("description", mg1.getDescription());
		mg1map.put("descriptionformat", DescriptionFormat.MOODLE.getValue().toString());
		
		Map<String, Object> mg2map = new HashMap<>();
		mg2map.put("id", mg2.getId().toString());
		mg2map.put("courseid", mg2.getCourseId().toString());
		mg2map.put("name", mg2.getName());
		mg2map.put("description", mg2.getDescription());
		mg2map.put("descriptionformat", DescriptionFormat.MARKDOWN.getValue().toString());
		
		Set<Map<String, Object>> set = new HashSet<>();
		set.add(mg1map);
		set.add(mg2map);
		
		return set;
	}
	
	private Set<Map<String, Object>> getRoles() {
		MoodleRole mg1 = new MoodleRole(12l, "asjkne", "asjwkl qd kd kqj", 3);
		MoodleRole mg2 = new MoodleRole(142l, "asgvgjkne", "afevsjwkl fffqd kd kqj", 8);
		
		Map<String, Object> mg1map = new HashMap<>();
		mg1map.put("roleid", mg1.getRoleId().toString());
		mg1map.put("name", mg1.getName());
		mg1map.put("shortname", mg1.getShortName());
		mg1map.put("sortorder", mg1.getSortOrder().toString());
		
		Map<String, Object> mg2map = new HashMap<>();
		mg2map.put("roleid", mg2.getRoleId().toString());
		mg2map.put("name", mg2.getName());
		mg2map.put("shortname", mg2.getShortName());
		mg2map.put("sortorder", mg2.getSortOrder().toString());
		
		Set<Map<String, Object>> set = new HashSet<>();
		set.add(mg1map);
		set.add(mg2map);
		
		return set;
	}
	
	private Set<Map<String, Object>> getEnrolledCourses() {
		UserEnrolledCourse uec1 = new UserEnrolledCourse(1l,"teste 1", "t1");
		UserEnrolledCourse uec2 = new UserEnrolledCourse(31l,"teste 2", "t2");
		
		Map<String, Object> uec1map = new HashMap<>();
		uec1map.put("id", uec1.getId().toString());
		uec1map.put("fullname", uec1.getFullName());
		uec1map.put("shortname", uec1.getShortName());
		
		Map<String, Object> uec2map = new HashMap<>();
		uec2map.put("id", uec2.getId().toString());
		uec2map.put("fullname", uec2.getFullName());
		uec2map.put("shortname", uec2.getShortName());
		
		Set<Map<String, Object>> set = new HashSet<>();
		set.add(uec1map);
		set.add(uec2map);
		
		return set;
	}
	private Set<Map<String, Object>> getCustomfields() {
		UserCustomField ucf1 = new UserCustomField("checkbox", "assas1", "asasas2", "asasas3");
		UserCustomField ucf2 = new UserCustomField(CustomFieldType.TEXT, "assas4", "asasas5", "asasas6");
		Map<String, Object> ucf1map = new HashMap<>();
		ucf1map.put("type", ucf1.getType().getValue());
		ucf1map.put("value", ucf1.getValue());
		
		Map<String, Object> ucf2map = new HashMap<>();
		ucf2map.put("type", ucf2.getType().getValue());
		ucf2map.put("value", ucf2.getValue());
		
		
		Set<Map<String, Object>> set = new HashSet<>();
		set.add(ucf1map);
		set.add(ucf2map);
		
		return set;
	}
	
	private Set<Map<String, Object>> getPreferences() {
		UserPreference up1 = new UserPreference("name1", "value1");
		UserPreference up2 = new UserPreference("name2", "value2");
		Map<String, Object> up1map = new HashMap<>();
		up1map.put("type", up1.getType());
		up1map.put("value", up1.getValue());
		
		Map<String, Object> up2map = new HashMap<>();
		up2map.put("type", up2.getType());
		up2map.put("value", up2.getValue());
		
		
		Set<Map<String, Object>> set = new HashSet<>();
		set.add(up1map);
		set.add(up2map);
		
		return set;
	}
	
	private void doRoleAssertion(Set<Map<String, Object>> rlsSet, List<MoodleRole> roleLst) {
		for (Map<String, Object> entityMap : rlsSet) {
			MoodleRole entity = new MoodleRole();
			entity.setRoleId(Long.parseLong((String) entityMap.get("roleid")));
			entity.setShortName((String) entityMap.get("shortname"));
			entity.setName((String) entityMap.get("name"));
			entity.setSortOrder(Integer.parseInt((String) entityMap.get("sortorder")));
			assertThat(roleLst).contains(entity);
		}
	}

	private void doGroupAssertion(Set<Map<String, Object>> grpsSet, List<MoodleGroup> grpLst) {
		for (Map<String, Object> entityMap : grpsSet) {
			MoodleGroup entity = new MoodleGroup();
			entity.setId(Long.parseLong((String) entityMap.get("id")));
			entity.setCourseId(Long.parseLong((String) entityMap.get("courseid")));
			entity.setName((String) entityMap.get("name"));
			entity.setDescription((String) entityMap.get("description"));
			entity.setDescriptionFormat(Integer.parseInt((String) entityMap.get("descriptionformat")));
			assertThat(grpLst).contains(entity);
		}
	}
	private void doEnrolledCourseAssertion(Set<Map<String, Object>> ucfSet, List<MoodleCourse> enrollLst) {
		for (Map<String, Object> entityMap : ucfSet) {
			MoodleCourse entity = new MoodleCourse();
			entity.setId(Long.parseLong((String) entityMap.get("id")));
			entity.setFullname((String) entityMap.get("fullname"));
			entity.setShortname((String) entityMap.get("shortname"));
			assertThat(enrollLst).contains(entity);
		}
	}
	private void doCustomFieldsAssertion(Set<Map<String, Object>> ucfSet, List<UserCustomField> customLst) {
		for (Map<String, Object> ucfMap : ucfSet) {
			UserCustomField ucf = new UserCustomField();
			ucf.setType((String) ucfMap.get("type"));
			ucf.setValue((String) ucfMap.get("value"));
			assertThat(customLst).contains(ucf);
			
		}
	}

	private void doPreferencesAssertion(Set<Map<String, Object>> upSet, List<UserPreference> preferenceLst) {
		for (Map<String, Object> upMap : upSet) {
			UserPreference up = new UserPreference(
				(String) upMap.get("type"), 
				(String) upMap.get("value")
			);
			assertThat(preferenceLst).contains(up);
			
		}
	}

	private void doSerializeAssertions(String dataString, Set<MoodleUser> entities) {
		int i = 0;
		
		for (MoodleUser e : entities) {
			
			assertTrue("Not contains username", dataString.contains("users["+ i +"][username]"));
			assertTrue("Not contains "+e.getUsername(), dataString.contains(e.getUsername()));
			assertTrue("Not contains password", dataString.contains("users["+ i +"][password]"));
			assertTrue("Not contains "+e.getPassword(), dataString.contains(e.getPassword()));
			assertTrue("Not contains firstname", dataString.contains("users["+ i +"][firstname]"));
			assertTrue("Not contains "+e.getFirstname(), dataString.contains(e.getFirstname()));
			assertTrue("Not contains email", dataString.contains("users["+ i +"][email]"));
			assertTrue("Not contains "+e.getLastname(), dataString.contains(e.getLastname()));
			assertTrue("Not contains email", dataString.contains("users["+ i +"][email]"));
			assertTrue("Not contains "+e.getEmail(), dataString.contains(e.getEmail()));
			assertTrue("Not contains auth", dataString.contains("users["+ i +"][auth]"));
			assertTrue("Not contains "+e.getAuth(), dataString.contains(e.getAuth()));
			assertTrue("Not contains idnumber", dataString.contains("users["+ i +"][idnumber]"));
			assertTrue("Not contains "+e.getIdnumber(), dataString.contains(e.getIdnumber()));
			assertTrue("Not contains lang", dataString.contains("users["+ i +"][lang]"));
			assertTrue("Not contains "+e.getLang(), dataString.contains(e.getLang()));
			assertTrue("Not contains theme", dataString.contains("users["+ i +"][theme]"));
			assertTrue("Not contains "+e.getTheme(), dataString.contains(e.getTheme()));
			assertTrue("Not contains timezone", dataString.contains("users["+ i +"][timezone]"));
			assertTrue("Not contains "+e.getTimezone(), dataString.contains(e.getTimezone()));
			assertTrue("Not contains description", dataString.contains("users["+ i +"][description]"));
			assertTrue("Not contains "+e.getDescription(), dataString.contains(e.getDescription()));
			assertTrue("Not contains city", dataString.contains("users["+ i +"][city]"));
			assertTrue("Not contains "+e.getCity(), dataString.contains(e.getCity()));
			assertTrue("Not contains country", dataString.contains("users["+ i +"][country]"));
			assertTrue("Not contains "+e.getCountry(), dataString.contains(e.getCountry()));
			assertTrue("Not contains firstnamephonetic", dataString.contains("users["+ i +"][firstnamephonetic]"));
			assertTrue("Not contains "+e.getFirstnamephonetic(), dataString.contains(e.getFirstnamephonetic()));
			assertTrue("Not contains lastnamephonetic", dataString.contains("users["+ i +"][lastnamephonetic]"));
			assertTrue("Not contains "+e.getLastnamephonetic(), dataString.contains(e.getLastnamephonetic()));
			assertTrue("Not contains middlename", dataString.contains("users["+ i +"][middlename]"));
			assertTrue("Not contains "+e.getMiddlename(), dataString.contains(e.getMiddlename()));
			assertTrue("Not contains alternatename", dataString.contains("users["+ i +"][alternatename]"));
			assertTrue("Not contains "+e.getAlternatename(), dataString.contains(e.getAlternatename()));

			int opidx = 0;
			for (UserPreference op : e.getPreferences()) {
				assertTrue("Not contains type", dataString.contains("users["+ i +"][preferences]["+opidx+"][type]"));
				assertTrue("Not contains "+op.getType(), dataString.contains(op.getType()));
				assertTrue("Not contains value", dataString.contains("users["+ i +"][preferences]["+opidx+"][value]"));
				assertTrue("Not contains "+op.getValue(), dataString.contains(op.getValue()));
				opidx++;
			}

			int cfidx = 0;
			for (UserCustomField ucf : e.getCustomfields()) {
				assertTrue("Not contains type", dataString.contains("users["+ i +"][customfields]["+cfidx+"][type]"));
				assertTrue("Not contains "+ucf.getType(), dataString.contains(ucf.getType().getValue()));
				assertTrue("Not contains value", dataString.contains("users["+ i +"][customfields]["+cfidx+"][value]"));
				assertTrue("Not contains "+ucf.getValue(), dataString.contains(ucf.getValue()));
			}
			i++;	
		}
		assertTrue(dataString.contains("="));
		assertTrue(dataString.contains("&"));
	}
}

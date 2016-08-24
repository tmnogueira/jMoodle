package ml.jmoodle.functions.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import ml.jmoodle.commons.MoodleUser;
import ml.jmoodle.configs.MoodleConfig;
import ml.jmoodle.configs.expections.MoodleConfigException;
import ml.jmoodle.functions.MoodleWSFunction;
import ml.jmoodle.functions.MoodleWSFunctionCall;
import ml.jmoodle.functions.MoodleWSFunctionFactory;
import ml.jmoodle.functions.MoodleWSFunctions;
import ml.jmoodle.functions.exceptions.MoodleRestCreateUsersException;
import ml.jmoodle.functions.exceptions.MoodleWSFucntionException;
import ml.jmoodle.functions.rest.fixtures.UsersFixture;
import ml.jmoodle.functions.rest.tools.MoodleRestUserFunctionsTools;
import ml.jmoodle.tools.MoodleTools;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MoodleTools.class, MoodleRestCreateUsers.class, MoodleWSFunctionCall.class })
public class MoodleRestCreateUserTest implements MoodleRestFunctionsCommonsTest {
	URL mdlUrl;

	MoodleConfig configMck;
	String serializedUser = "users%5B0%5D%5Busername%5D=Rona-Auer&users%5B0%5D%5Bpassword%5D=awe2&users%5B0%5D%5Bfirstname%5D=Rona&users%5B0%5D%5Blastname%5D=Auer&users%5B0%5D%5Bemail%5D=Rona-Auer%40email.test&users%5B0%5D%5Bauth%5D=manual&users%5B0%5D%5Bidnumber%5D=024886573360022&users%5B0%5D%5Blang%5D=en_us&users%5B0%5D%5Bcalendartype%5D=gregorian&users%5B0%5D%5Btheme%5D=aasas&users%5B0%5D%5Btimezone%5D=Sao_Paulo&users%5B0%5D%5Bmailformat%5D=0&users%5B0%5D%5Bdescription%5D=foo+bar&users%5B0%5D%5Bcity%5D=Neil+Koch&users%5B0%5D%5Bcountry%5D=%24lang&users%5B0%5D%5Bfirstnamephonetic%5D=%2Fqwwsss%2F&users%5B0%5D%5Blastnamephonetic%5D=%2Fqwwsss%2F&users%5B0%5D%5Bmiddlename%5D=Yost&users%5B0%5D%5Balternatename%5D=Alva+Bins&users%5B0%5D%5Bcustomfields%5D%5B0%5D%5Btype%5D=brithday&users%5B0%5D%5Bcustomfields%5D%5B0%5D%5Bvalue%5D=533122112&users%5B0%5D%5Bcustomfields%5D%5B1%5D%5Btype%5D=anivers%C3%A1rio&users%5B0%5D%5Bcustomfields%5D%5B1%5D%5Bvalue%5D=100100010&users%5B0%5D%5Bpreferences%5D%5B0%5D%5Btype%5D=maildigest&users%5B0%5D%5Bpreferences%5D%5B0%5D%5Bvalue%5D=1&users%5B0%5D%5Bpreferences%5D%5B1%5D%5Btype%5D=editorformat&users%5B0%5D%5Bpreferences%5D%5B1%5D%5Bvalue%5D=4";

	@BeforeClass
	public static void setUpClass() {
		FixtureFactoryLoader.loadTemplates("ml.jmoodle.functions.rest.fixtures");
	}

	@Before()
	public final void setUp() throws Exception {
		this.mdlUrl = new URL("http://test.mdl");
		// PowerMockito.whenNew(MoodleConfig.class).withAnyArguments();
		configMck = mock(MoodleConfig.class);
		when(configMck.getMoodleURL()).thenReturn(mdlUrl);
		when(configMck.getVersion()).thenReturn("2.2.0");

		PowerMockito.mockStatic(MoodleTools.class);
		PowerMockito.when(MoodleTools.compareVersion(anyString(), anyString())).thenReturn(0);
	}

	@Test
	public final void testIfGetTheRightClassUsingFactory() throws Exception {
		MoodleWSFunction function1 = MoodleWSFunctionFactory.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS,
				configMck);
		MoodleWSFunction function2 = MoodleWSFunctionFactory.getFunction(MoodleWSFunctions.MOODLE_USER_CREATE_USERS,
				configMck);
		assertThat(function1, instanceOf(MoodleRestCreateUsers.class));
		assertThat(function2, instanceOf(MoodleRestCreateUsers.class));
	}

	@Test
	public final void testIfReturnTheRightAddedVersion() throws Exception {
		// This function id added in 2.0.0
		MoodleRestCreateUsers createUser = new MoodleRestCreateUsers(configMck);
		assertEquals("2.0.0", createUser.getSinceVersion());
	}

	@Test(expected = MoodleWSFucntionException.class)
	public final void testIfThrowExceptionIfWrongMoodleVersionInConstructor()
			throws MoodleConfigException, MoodleWSFucntionException {
		PowerMockito.when(MoodleTools.compareVersion(anyString(), anyString())).thenReturn(-1);
		when(configMck.getVersion()).thenReturn("1.4.0");
		new MoodleRestCreateUsers(configMck);

	}

	@Test(expected = MoodleRestCreateUsersException.class)
	public final void testIfGetFunctionStrThrowExceptionWhenNoUsersIsSet()
			throws MoodleWSFucntionException, MoodleConfigException {

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		function1.getFunctionData();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void testIfGetFunctionStrCallMoodleRestUserFunctioTooleSerializeUsers() throws Exception {

		Set<MoodleUser> mdlUsers = new LinkedHashSet(
				Fixture.from(MoodleUser.class).gimme(3, "MoodleRestCreateUserFunctionUser"));
		MoodleRestUserFunctionsTools userFunctionsTools = mock(MoodleRestUserFunctionsTools.class);

		MoodleRestCreateUsers function1 = PowerMockito.spy((MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck));

		PowerMockito.doReturn(userFunctionsTools).when(function1, "getUserFuntionsTools");

		function1.setUsers(mdlUsers);
		function1.getFunctionData();

		verify(userFunctionsTools).serliazeUsers(mdlUsers);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testIfGetTheRightFunctionStrByMoodleVersion() throws Exception {
		Set<MoodleUser> mdlUsers = new LinkedHashSet(
				Fixture.from(MoodleUser.class).gimme(3, "MoodleRestCreateUserFunctionUser"));
		MoodleRestUserFunctionsTools userFunctionsTools = mock(MoodleRestUserFunctionsTools.class);
		when(userFunctionsTools.serliazeUsers(anySet())).thenReturn(serializedUser);

		MoodleRestCreateUsers function1 = PowerMockito.spy((MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck));
		// PowerMockito.doReturn(mdlUsers).when(function1, "getUsers");
		PowerMockito.doReturn(userFunctionsTools).when(function1, "getUserFuntionsTools");

		StringBuilder expectedStr = new StringBuilder();
		expectedStr.append(MoodleTools.encode("wsfunction")).append("=")
				.append(MoodleTools.encode(MoodleWSFunctions.CORE_USER_CREATE_USERS.getValue())).append("&")
				.append(serializedUser);
		function1.setUsers(mdlUsers);
		assertThat(function1.getFunctionData(), equalTo(expectedStr.toString()));

		expectedStr.delete(0, expectedStr.length());

		when(configMck.getVersion()).thenReturn("2.1.3");
		MoodleRestCreateUsers function2 = PowerMockito.spy((MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck));
		// PowerMockito.doReturn(mdlUsers).when(function2, "getUsers");
		PowerMockito.doReturn(userFunctionsTools).when(function2, "getUserFuntionsTools");

		expectedStr.append(MoodleTools.encode("wsfunction")).append("=")
				.append(MoodleTools.encode(MoodleWSFunctions.MOODLE_USER_CREATE_USERS.getValue())).append("&")
				.append(serializedUser);
		function2.setUsers(mdlUsers);
		assertThat(function2.getFunctionData(), equalTo(expectedStr.toString()));
	}

	@Test(expected = MoodleRestCreateUsersException.class)
	public final void testIfAddDuplicateUserThrowsExecpiton() throws MoodleWSFucntionException, MoodleConfigException {
		MoodleUser user1 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		MoodleUser user2 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		function1.addUser(user2);
		function1.addUser(user1);
		function1.addUser(user1);
	}

	@Test
	public final void testIfAddUserWithoutUsernameThrowsExecpiton()
			throws MoodleWSFucntionException, MoodleConfigException {
		MoodleUser user1 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		MoodleUser user2 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");

		user1.setUsername(null);
		user2.setUsername("");

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		try {
			function1.addUser(user1);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}

		try {
			function1.addUser(user2);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}
	}

	@Test
	public final void testIfAddUserWithoutFirstnameThrowsExecpiton()
			throws MoodleWSFucntionException, MoodleConfigException {
		MoodleUser user1 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		MoodleUser user2 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");

		user1.setFirstname(null);
		user2.setFirstname("");

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		try {
			function1.addUser(user1);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}

		try {
			function1.addUser(user2);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}
	}

	@Test
	public final void testIfAddUserWithoutLastNameThrowsExecpiton()
			throws MoodleWSFucntionException, MoodleConfigException {
		MoodleUser user1 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		MoodleUser user2 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");

		user1.setLastname(null);
		user2.setLastname("");

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		try {
			function1.addUser(user1);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}

		try {
			function1.addUser(user2);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}
	}

	@Test
	public final void testIfAddUserWithoutEmailThrowsExecpiton()
			throws MoodleWSFucntionException, MoodleConfigException {
		MoodleUser user1 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");
		MoodleUser user2 = Fixture.from(MoodleUser.class).gimme("MoodleRestCreateUserFunctionUser");

		user1.setEmail(null);
		user2.setEmail("");

		MoodleRestCreateUsers function1 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		try {
			function1.addUser(user1);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}

		try {
			function1.addUser(user2);
		} catch (MoodleRestCreateUsersException e) {
			assertThat(e, instanceOf(MoodleRestCreateUsersException.class));
		}
	}

	@Test
	public final void testIfGetTheRightFunctionNameByMoodleVersion() throws Exception {
		// "core_user_create_users", "moodle_user_create_users"
		// This function chages name in moodle 2.2

		MoodleRestCreateUsers mdlfnc22 = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);
		String fnc22Name = mdlfnc22.getFunctionName();

		when(configMck.getVersion()).thenReturn("2.1.0");
		MoodleRestCreateUsers mdlfnc20 = new MoodleRestCreateUsers(configMck);
		PowerMockito.when(MoodleTools.compareVersion(anyString(), anyString())).thenReturn(-1);
		String fnc20Name = mdlfnc20.getFunctionName();

		assertEquals("core_user_create_users", fnc22Name);
		assertEquals("moodle_user_create_users", fnc20Name);

	}

	@Test
	public final void testIfDoCallMethodCallsMoodleWSFunctionCallCallMethod() throws Exception {

		UsersFixture usersFixture=Fixture.from(UsersFixture.class).gimme("MoodleRestCreateUserResponse");
		Document userResponse = usersFixture.getAddUsersRespone();
		MoodleWSFunctionCall wsFunctionCallMck = mock(MoodleWSFunctionCall.class);
		when(wsFunctionCallMck.call(any(MoodleWSFunction.class))).thenReturn(userResponse);
		PowerMockito.mockStatic(MoodleWSFunctionCall.class);
		PowerMockito.when(MoodleWSFunctionCall.getInstance(any(MoodleConfig.class))).thenReturn(wsFunctionCallMck);

		MoodleRestCreateUsers mdlfnc = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		mdlfnc.setUsers(usersFixture.getMdlUsers());
		try {
			mdlfnc.doCall();
		} catch (NullPointerException e) {
			// Null poiter is excpeted since wsFunctionCall is a mock
		}

		verify(wsFunctionCallMck).call(mdlfnc);
	}
	
	

	@Test
	public final void testIfDoCallRetunrsMoodleUserColletion() throws Exception {

		UsersFixture usersFixture = Fixture.from(UsersFixture.class).gimme("MoodleRestCreateUserResponse");
		Document userResponse = usersFixture.getAddUsersRespone();
		MoodleWSFunctionCall wsFunctionCallMck = mock(MoodleWSFunctionCall.class);
		when(wsFunctionCallMck.call(any(MoodleWSFunction.class))).thenReturn(userResponse);
		PowerMockito.mockStatic(MoodleWSFunctionCall.class);
		PowerMockito.when(MoodleWSFunctionCall.getInstance(any(MoodleConfig.class))).thenReturn(wsFunctionCallMck);

		MoodleRestCreateUsers mdlfnc = (MoodleRestCreateUsers) MoodleWSFunctionFactory
				.getFunction(MoodleWSFunctions.CORE_USER_CREATE_USERS, configMck);

		Set<MoodleUser> usersToAdd = usersFixture.getMdlUsers();
		for (MoodleUser moodleUser : usersToAdd) {
			moodleUser.setId(null);
		}
		assertThat(usersToAdd.size(), equalTo(3));
		for (MoodleUser moodleUser : usersToAdd) {
			assertThat(moodleUser.getId(), nullValue());
		}

		mdlfnc.setUsers(usersFixture.getMdlUsers());
		Set<MoodleUser> response = mdlfnc.doCall();

		assertThat(response.size(), equalTo(3));
		for (MoodleUser moodleUser : response) {
			assertThat(moodleUser, instanceOf(MoodleUser.class));
			assertThat(moodleUser.getId(), notNullValue());
			assertThat(moodleUser.getId(), instanceOf(Long.class));
		}

	}

}

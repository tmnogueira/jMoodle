package ml.jmoodle.functions.rest.tools;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import ml.jmoodle.commons.MoodleUser;
import ml.jmoodle.tools.MoodleTools;

public class MoodleRestUserFunctionsToolsTest {
	private MoodleRestUserFunctionsTools uft = new MoodleRestUserFunctionsTools();
	
	@BeforeClass
	public static void setUp() {
	    FixtureFactoryLoader.loadTemplates("ml.jmoodle.functions.rest.fixtures");
	}

	@Test
	public final void testSerializeUsers() {
		Assert.fail("not implemented");
	}

	@Test
	public final void testSerializeUser() throws Exception {
		MoodleUser moodleUser =  Fixture.from(MoodleUser.class).gimme("MoodleRestUserFunctionsToolsTestUser1");
		
		String userStr = uft.serializeUser(moodleUser);
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][id]")+"="+MoodleTools.encode(String.valueOf(moodleUser.getId()))));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][username]")+"="+MoodleTools.encode(moodleUser.getUsername())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][firstname]")+"="+MoodleTools.encode(moodleUser.getFirstname())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][lastname]")+"="+MoodleTools.encode(moodleUser.getLastname())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][password]")+"="+MoodleTools.encode(moodleUser.getPassword())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][email]")+"="+MoodleTools.encode(moodleUser.getEmail())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][auth]")+"="+MoodleTools.encode(moodleUser.getAuth())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][idnumber]")+"="+MoodleTools.encode(moodleUser.getIdnumber())));
		
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][lang]")+"="+MoodleTools.encode(moodleUser.getLang())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][calendartype]")+"="+MoodleTools.encode(moodleUser.getCalendartype())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][theme]")+"="+MoodleTools.encode(moodleUser.getTheme())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][timezone]")+"="+MoodleTools.encode(moodleUser.getTimezone())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][mailformat]")+"="+MoodleTools.encode(String.valueOf(moodleUser.getMailformat()))));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][description]")+"="+MoodleTools.encode(moodleUser.getDescription())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][city]")+"="+MoodleTools.encode(moodleUser.getCity())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][country]")+"="+MoodleTools.encode(moodleUser.getCountry())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][firstnamephonetic]")+"="+MoodleTools.encode(moodleUser.getFirstnamephonetic())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][lastnamephonetic]")+"="+MoodleTools.encode(moodleUser.getLastnamephonetic())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][middlename]")+"="+MoodleTools.encode(moodleUser.getMiddlename())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][alternatename]")+"="+MoodleTools.encode(moodleUser.getAlternatename())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][preferences][0][type]")+"="+MoodleTools.encode(moodleUser.getPreferences().iterator().next().getName())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][preferences][0][value]")+"="+MoodleTools.encode(moodleUser.getPreferences().iterator().next().getValue())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][customfields][0][type]")+"="+MoodleTools.encode(moodleUser.getCustomfields().iterator().next().getName())));
		Assert.assertThat(userStr, containsString(MoodleTools.encode("users[0][customfields][0][value]")+"="+MoodleTools.encode(moodleUser.getCustomfields().iterator().next().getValue())));
		// users[0][id] = int
		// users[0][username]= string
		// users[0][password]= string
		// users[0][firstname]= string
		// users[0][lastname]= string
		// users[0][email]= string
		// users[0][auth]= string
		// users[0][idnumber]= string
		// users[0][lang]= string
		// users[0][calendartype]= string
		// users[0][theme]= string
		// users[0][timezone]= string
		// users[0][mailformat]= int
		// users[0][description]= string
		// users[0][city]= string
		// users[0][country]= string
		// users[0][firstnamephonetic]= string
		// users[0][lastnamephonetic]= string
		// users[0][middlename]= string
		// users[0][alternatename]= string
		// users[0][preferences][0][type]= string
		// users[0][preferences][0][value]= string
		// users[0][customfields][0][type]= string
		// users[0][customfields][0][value]= string
		
	}

	@Test
	public final void testUnSerializeUsers() {
		Assert.fail("not implemented");
	}

	@Test
	public final void testUnSerializeUser() {
		Assert.fail("not implemented");
	}

}

package co.kr.cafego.core.config;

public class SystemEnviroment {

	public final static String PROFILE_LOCAL = "local";

	public final static String PROFILE_DEV = "dev";

	public final static String PROFILE_TEST = "test";

	public final static String PROFILE_STG = "stg";
	
	public final static String PROFILE_PROD = "prod";

	private static String activeProfile;

	public final static String[] CHIEF_PROFILE_CANDIDATES = new String[] { PROFILE_PROD, PROFILE_TEST, PROFILE_DEV,
			PROFILE_LOCAL, PROFILE_STG };

	public static String getActiveProfile() {
        return activeProfile;
    }
	
	public static String setActiveProfile(String profile) {
        return activeProfile = profile;
    }
}

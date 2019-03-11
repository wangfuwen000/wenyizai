package cases;

import org.junit.Test;

import utils.SpoonUtil;


public class demo extends Base {


	@Test
	public void testClickMenu() throws Exception {

		//Unlock the lock screen
		solo.unlockScreen();
		//Click on action menu item add
		solo.sleep(3000);
//		solo.clickOnView(solo.getView(R.id.nav_view));
		solo.clickOnText("Banana");
		solo.sleep(3000);
		solo.clickOnActionBarHomeButton();
		solo.sleep(3000);
		//b = true;
	}
}

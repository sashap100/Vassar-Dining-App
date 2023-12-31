package edu.vassar.cmpu203.app;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.*;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;

import java.time.LocalDate;
import edu.vassar.cmpu203.app.controller.ControllerActivity;

/*
* This class is instrumented testing for all functionalities solely in the ViewDayFragment.
* Both tests reset the memory of the app for the purposes of testing favorites and caching.
* Note that this will entirely reset the memory for the app, so any saved information from
* running it on a particular device in the past will no longer be available.
*/

public class ViewDayTest {
    //wait time to fetch menu
    final int waitTime = 15000;
    @org.junit.Rule
    public ActivityScenarioRule<ControllerActivity> activityRule = new ActivityScenarioRule<>(ControllerActivity.class);

    /*
     * Instrumented test, which will execute on an Android device.
     * Testing for correct menu for multiple dates, proper overwriting of menusDisplay, caching of
     * menu previously searched for
     */
    @org.junit.Test
    public void searchDatesTest(){
        //Reset memory to remove any previously set restrictions or favorites
        this.activityRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ControllerActivity>(){
                    @Override
                    public void perform(ControllerActivity activity) {activity.memoryReset();}
                } );
        SystemClock.sleep(waitTime); //Wait for memory reset to complete

        //Check default input
        ViewInteraction dateInput = Espresso.onView(ViewMatchers.withId(R.id.dateInput));
        dateInput.check(ViewAssertions.matches(ViewMatchers.withText(LocalDate.now().toString())));

        //Enter desired date
        dateInput.perform(ViewActions.clearText());
        dateInput.perform(ViewActions.typeText("2022-04-27"));
        Espresso.closeSoftKeyboard();

        //Click search button
        ViewInteraction dateButton = Espresso.onView(ViewMatchers.withId(R.id.dateInputButton));
        dateButton.perform(ViewActions.click());
        SystemClock.sleep(waitTime); //Wait to fetch from website

        //Check that certain meals are in the menu
        ViewMatchers.withSubstring("@The Farmer");
        ViewMatchers.withSubstring("Vegan White Bean And Chickpea Soup");
        ViewMatchers.withSubstring("Pesto Shrimp Pizza");
        ViewMatchers.withSubstring("Lemon-garlic Pasta Primavera");
        ViewMatchers.withSubstring("Corn And Black Bean Cheese Quesadillas");

        //Enter new date
        dateInput.perform(ViewActions.clearText());

        dateInput.perform(ViewActions.typeText("2023-11-11"));
        Espresso.closeSoftKeyboard();

        //Click search button
        dateButton.perform(ViewActions.click());
        SystemClock.sleep(waitTime); //Wait to fetch from website

        //Check that certain meals are in the menu
        ViewMatchers.withSubstring("Stuffed French Toast With Berries And Cream Cheese");
        ViewMatchers.withSubstring("Hawaiian Style Pizza");
        ViewMatchers.withSubstring("Spicy Thai Coconut Chicken Soup With Noodles");
        ViewMatchers.withSubstring("Mozzarella Stix With Marinara");

        //Check that old menu items are no longer displayed
        Espresso.onView(ViewMatchers.withSubstring("Pesto Shrimp Pizza")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Vegan White Bean And Chickpea Soup")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Lemon-garlic Pasta Primavera")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Corn And Black Bean Cheese Quesadillas")).check(ViewAssertions.doesNotExist());


        //Try previous date again to check that it is not scraped again
        dateInput.perform(ViewActions.clearText());
        dateInput.perform(ViewActions.typeText("2022-04-27"));
        Espresso.closeSoftKeyboard();
        dateButton.perform(ViewActions.click());

        //Check that certain meals are in the menu
        ViewMatchers.withSubstring("Vegan White Bean And Chickpea Soup");
        ViewMatchers.withSubstring("Pesto Shrimp Pizza");
        ViewMatchers.withSubstring("Lemon-garlic Pasta Primavera");
        ViewMatchers.withSubstring("Corn And Black Bean Cheese Quesadillas");

        //Check that past items are no longer there
        Espresso.onView(ViewMatchers.withSubstring("Stuffed French Toast With Berries And Cream Cheese")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Hawaiian Style Pizza")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Spicy Thai Coconut Chicken Soup With Noodles")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withSubstring("Mozzarella Stix With Marinara")).check(ViewAssertions.doesNotExist());

    }

    /*
     * Instrumented test, which will execute on an Android device.
     * Testing for ability to add favorites, show only favorites and all favorites
     * when favorites-only button is selected, and remove favorites in the ViewDayFragment.
     */
    @org.junit.Test
    public void favoritesTest(){
        //Reset memory to remove any previously set restrictions or favorites
        this.activityRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<ControllerActivity>(){
                    @Override
                    public void perform(ControllerActivity activity) {activity.memoryReset();}
                } );
        SystemClock.sleep(waitTime); //Wait for memory reset to complete

        ViewInteraction dateInput = Espresso.onView(ViewMatchers.withId(R.id.dateInput));

        //Enter desired date
        dateInput.perform(ViewActions.clearText());
        dateInput.perform(ViewActions.typeText("2021-12-12"));
        Espresso.closeSoftKeyboard();

        //Click search button
        ViewInteraction dateButton = Espresso.onView(ViewMatchers.withId(R.id.dateInputButton));
        dateButton.perform(ViewActions.click());
        SystemClock.sleep(waitTime); //Wait to fetch from website

        //Check that certain items are in the menu
        ViewInteraction dish1 = Espresso.onView(ViewMatchers.withSubstring("Granola"));
        ViewInteraction dish2 = Espresso.onView(ViewMatchers.withSubstring("French Dressing"));
        ViewInteraction dish3 = Espresso.onView(ViewMatchers.withSubstring("Parmesan Cheese"));
        ViewInteraction dish4 = Espresso.onView(ViewMatchers.withSubstring("Bell Peppers"));
        ViewInteraction dish5 = Espresso.onView(ViewMatchers.withSubstring("Tofu"));

        //Select favorites
        ViewInteraction favorite1 = Espresso.onView(ViewMatchers.withTagValue(Matchers.is("granola")));
        favorite1.perform(ViewActions.click());
        ViewInteraction favorite2 = Espresso.onView(ViewMatchers.withTagValue(Matchers.is("french dressing")));
        favorite2.perform(ViewActions.click());
        ViewInteraction favorite5 = Espresso.onView(ViewMatchers.withTagValue(Matchers.is("tofu")));
        favorite5.perform(ViewActions.click());

        //Select view only favorites
        ViewInteraction favoritesButton = Espresso.onView(ViewMatchers.withId(R.id.favoritesFilterCheckbox));
        favoritesButton.perform(ViewActions.click());
        //Check that all favorites and only favorites are displayed
        dish1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish5.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish4.check(ViewAssertions.doesNotExist());
        dish3.check(ViewAssertions.doesNotExist());

        //Enter new date
        dateInput.perform(ViewActions.clearText());
        dateInput.perform(ViewActions.typeText("2022-10-03"));
        Espresso.closeSoftKeyboard();
        //Click search button
        dateButton.perform(ViewActions.click());
        SystemClock.sleep(waitTime); //Wait to fetch from website
        //Check that old favorites are displayed
        dish1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish5.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish4.check(ViewAssertions.doesNotExist());
        dish3.check(ViewAssertions.doesNotExist());
        //Unclick view only favorites
        favoritesButton.perform(ViewActions.click());
        //Change favorites: Remove some, add others
        favorite1.perform(ViewActions.click());
        favorite2.perform(ViewActions.click());
        ViewInteraction favorite4 = Espresso.onView(ViewMatchers.withTagValue(Matchers.is("bell peppers")));
        favorite4.perform(ViewActions.click());
        //Check updated favorites
        favoritesButton.perform(ViewActions.click());
        dish1.check(ViewAssertions.doesNotExist());
        dish2.check(ViewAssertions.doesNotExist());
        dish5.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        dish3.check(ViewAssertions.doesNotExist());
        //Unclick all favorites so future tests with potentially saved info have a clean slate
        favorite5.perform(ViewActions.click());
        favorite4.perform(ViewActions.click());
    }



}

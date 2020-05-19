package br.com.angelorobson.usermvi.userdetails

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.angelorobson.usermvi.AndroidTestApplication
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.component
import br.com.angelorobson.usermvi.di.TestComponent
import br.com.angelorobson.usermvi.users.UserListFragment
import br.com.angelorobson.usermvi.utils.FileUtils
import br.com.angelorobson.usermvi.utils.TestIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

class UserDetailsFragmentTest {


    private val mockWebServer = MockWebServer()

    @Test
    fun views() {
        val id = 1L
        val fragmentArgs = Bundle().apply {
            putLong("id", id)
        }

        val mockResponse = MockResponse()
            .setBody(FileUtils.getJson("json/users/user.json"))
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario = launchFragmentInContainer<UserDetailFragment>(
            themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar,
            fragmentArgs = fragmentArgs
        )

        var idlingResource: TestIdlingResource? = null

        scenario.onFragment { fragment ->
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()
        }

        onView(withId(R.id.tvName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvUserName)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
        mockWebServer.close()
    }

    @Test
    fun success_show_user() {
        val id = 1L
        val fragmentArgs = Bundle().apply {
            putLong("id", id)
        }

        val mockResponse = MockResponse()
            .setBody(FileUtils.getJson("json/users/user.json"))
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario = launchFragmentInContainer<UserDetailFragment>(
            themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar,
            fragmentArgs = fragmentArgs
        )

        var idlingResource: TestIdlingResource? = null

        scenario.onFragment { fragment ->
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()
        }

        onView(withId(R.id.tvName)).check(matches(withText("Leanne Graham")))
        onView(withId(R.id.tvUserName)).check(matches(withText("Bret")))

        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
        mockWebServer.close()
    }

    @Test
    fun errorEvent_textView_showErrorMessage() {
        val id = 1L
        val fragmentArgs = Bundle().apply {
            putLong("id", id)
        }

        val mockResponse = MockResponse()
            .setBody(FileUtils.getJson("json/users/user.json"))
        mockResponse.setResponseCode(400)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario =
            launchFragmentInContainer<UserDetailFragment>(
                themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar,
                fragmentArgs = fragmentArgs
            )

        var resources: Resources? = null

        scenario.onFragment { fragment ->
            resources = fragment.resources
        }

        onView(withId(R.id.tvError)).check(matches(withText(resources?.getString(R.string.an_error_occurred))))

        mockWebServer.close()
    }

}
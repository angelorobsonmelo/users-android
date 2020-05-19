package br.com.angelorobson.usermvi.users

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.component
import br.com.angelorobson.usermvi.di.TestComponent
import br.com.angelorobson.usermvi.utils.TestIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class UsersListFragmentTest {


    private val mockWebServer = MockWebServer()

    private val json = "[{\n" +
            "\t\t\"id\": 1,\n" +
            "\t\t\"name\": \"Leanne Graham\",\n" +
            "\t\t\"username\": \"Bret\",\n" +
            "\t\t\"email\": \"Sincere@april.biz\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Kulas Light\",\n" +
            "\t\t\t\"suite\": \"Apt. 556\",\n" +
            "\t\t\t\"city\": \"Gwenborough\",\n" +
            "\t\t\t\"zipcode\": \"92998-3874\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-37.3159\",\n" +
            "\t\t\t\t\"lng\": \"81.1496\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"1-770-736-8031 x56442\",\n" +
            "\t\t\"website\": \"hildegard.org\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Romaguera-Crona\",\n" +
            "\t\t\t\"catchPhrase\": \"Multi-layered client-server neural-net\",\n" +
            "\t\t\t\"bs\": \"harness real-time e-markets\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 2,\n" +
            "\t\t\"name\": \"Ervin Howell\",\n" +
            "\t\t\"username\": \"Antonette\",\n" +
            "\t\t\"email\": \"Shanna@melissa.tv\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Victor Plains\",\n" +
            "\t\t\t\"suite\": \"Suite 879\",\n" +
            "\t\t\t\"city\": \"Wisokyburgh\",\n" +
            "\t\t\t\"zipcode\": \"90566-7771\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-43.9509\",\n" +
            "\t\t\t\t\"lng\": \"-34.4618\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"010-692-6593 x09125\",\n" +
            "\t\t\"website\": \"anastasia.net\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Deckow-Crist\",\n" +
            "\t\t\t\"catchPhrase\": \"Proactive didactic contingency\",\n" +
            "\t\t\t\"bs\": \"synergize scalable supply-chains\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 3,\n" +
            "\t\t\"name\": \"Clementine Bauch\",\n" +
            "\t\t\"username\": \"Samantha\",\n" +
            "\t\t\"email\": \"Nathan@yesenia.net\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Douglas Extension\",\n" +
            "\t\t\t\"suite\": \"Suite 847\",\n" +
            "\t\t\t\"city\": \"McKenziehaven\",\n" +
            "\t\t\t\"zipcode\": \"59590-4157\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-68.6102\",\n" +
            "\t\t\t\t\"lng\": \"-47.0653\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"1-463-123-4447\",\n" +
            "\t\t\"website\": \"ramiro.info\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Romaguera-Jacobson\",\n" +
            "\t\t\t\"catchPhrase\": \"Face to face bifurcated interface\",\n" +
            "\t\t\t\"bs\": \"e-enable strategic applications\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 4,\n" +
            "\t\t\"name\": \"Patricia Lebsack\",\n" +
            "\t\t\"username\": \"Karianne\",\n" +
            "\t\t\"email\": \"Julianne.OConner@kory.org\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Hoeger Mall\",\n" +
            "\t\t\t\"suite\": \"Apt. 692\",\n" +
            "\t\t\t\"city\": \"South Elvis\",\n" +
            "\t\t\t\"zipcode\": \"53919-4257\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"29.4572\",\n" +
            "\t\t\t\t\"lng\": \"-164.2990\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"493-170-9623 x156\",\n" +
            "\t\t\"website\": \"kale.biz\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Robel-Corkery\",\n" +
            "\t\t\t\"catchPhrase\": \"Multi-tiered zero tolerance productivity\",\n" +
            "\t\t\t\"bs\": \"transition cutting-edge web services\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 5,\n" +
            "\t\t\"name\": \"Chelsey Dietrich\",\n" +
            "\t\t\"username\": \"Kamren\",\n" +
            "\t\t\"email\": \"Lucio_Hettinger@annie.ca\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Skiles Walks\",\n" +
            "\t\t\t\"suite\": \"Suite 351\",\n" +
            "\t\t\t\"city\": \"Roscoeview\",\n" +
            "\t\t\t\"zipcode\": \"33263\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-31.8129\",\n" +
            "\t\t\t\t\"lng\": \"62.5342\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"(254)954-1289\",\n" +
            "\t\t\"website\": \"demarco.info\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Keebler LLC\",\n" +
            "\t\t\t\"catchPhrase\": \"User-centric fault-tolerant solution\",\n" +
            "\t\t\t\"bs\": \"revolutionize end-to-end systems\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 6,\n" +
            "\t\t\"name\": \"Mrs. Dennis Schulist\",\n" +
            "\t\t\"username\": \"Leopoldo_Corkery\",\n" +
            "\t\t\"email\": \"Karley_Dach@jasper.info\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Norberto Crossing\",\n" +
            "\t\t\t\"suite\": \"Apt. 950\",\n" +
            "\t\t\t\"city\": \"South Christy\",\n" +
            "\t\t\t\"zipcode\": \"23505-1337\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-71.4197\",\n" +
            "\t\t\t\t\"lng\": \"71.7478\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"1-477-935-8478 x6430\",\n" +
            "\t\t\"website\": \"ola.org\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Considine-Lockman\",\n" +
            "\t\t\t\"catchPhrase\": \"Synchronised bottom-line interface\",\n" +
            "\t\t\t\"bs\": \"e-enable innovative applications\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 7,\n" +
            "\t\t\"name\": \"Kurtis Weissnat\",\n" +
            "\t\t\"username\": \"Elwyn.Skiles\",\n" +
            "\t\t\"email\": \"Telly.Hoeger@billy.biz\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Rex Trail\",\n" +
            "\t\t\t\"suite\": \"Suite 280\",\n" +
            "\t\t\t\"city\": \"Howemouth\",\n" +
            "\t\t\t\"zipcode\": \"58804-1099\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"24.8918\",\n" +
            "\t\t\t\t\"lng\": \"21.8984\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"210.067.6132\",\n" +
            "\t\t\"website\": \"elvis.io\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Johns Group\",\n" +
            "\t\t\t\"catchPhrase\": \"Configurable multimedia task-force\",\n" +
            "\t\t\t\"bs\": \"generate enterprise e-tailers\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 8,\n" +
            "\t\t\"name\": \"Nicholas Runolfsdottir V\",\n" +
            "\t\t\"username\": \"Maxime_Nienow\",\n" +
            "\t\t\"email\": \"Sherwood@rosamond.me\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Ellsworth Summit\",\n" +
            "\t\t\t\"suite\": \"Suite 729\",\n" +
            "\t\t\t\"city\": \"Aliyaview\",\n" +
            "\t\t\t\"zipcode\": \"45169\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-14.3990\",\n" +
            "\t\t\t\t\"lng\": \"-120.7677\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"586.493.6943 x140\",\n" +
            "\t\t\"website\": \"jacynthe.com\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Abernathy Group\",\n" +
            "\t\t\t\"catchPhrase\": \"Implemented secondary concept\",\n" +
            "\t\t\t\"bs\": \"e-enable extensible e-tailers\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 9,\n" +
            "\t\t\"name\": \"Glenna Reichert\",\n" +
            "\t\t\"username\": \"Delphine\",\n" +
            "\t\t\"email\": \"Chaim_McDermott@dana.io\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Dayna Park\",\n" +
            "\t\t\t\"suite\": \"Suite 449\",\n" +
            "\t\t\t\"city\": \"Bartholomebury\",\n" +
            "\t\t\t\"zipcode\": \"76495-3109\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"24.6463\",\n" +
            "\t\t\t\t\"lng\": \"-168.8889\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"(775)976-6794 x41206\",\n" +
            "\t\t\"website\": \"conrad.com\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Yost and Sons\",\n" +
            "\t\t\t\"catchPhrase\": \"Switchable contextually-based project\",\n" +
            "\t\t\t\"bs\": \"aggregate real-time technologies\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"id\": 10,\n" +
            "\t\t\"name\": \"Clementina DuBuque\",\n" +
            "\t\t\"username\": \"Moriah.Stanton\",\n" +
            "\t\t\"email\": \"Rey.Padberg@karina.biz\",\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"street\": \"Kattie Turnpike\",\n" +
            "\t\t\t\"suite\": \"Suite 198\",\n" +
            "\t\t\t\"city\": \"Lebsackbury\",\n" +
            "\t\t\t\"zipcode\": \"31428-2261\",\n" +
            "\t\t\t\"geo\": {\n" +
            "\t\t\t\t\"lat\": \"-38.2386\",\n" +
            "\t\t\t\t\"lng\": \"57.2232\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"phone\": \"024-648-3804\",\n" +
            "\t\t\"website\": \"ambrose.net\",\n" +
            "\t\t\"company\": {\n" +
            "\t\t\t\"name\": \"Hoeger LLC\",\n" +
            "\t\t\t\"catchPhrase\": \"Centralized empowering task-force\",\n" +
            "\t\t\t\"bs\": \"target end-to-end models\"\n" +
            "\t\t}\n" +
            "\t}\n" +
            "]"

    @Test
    fun views() {
        val mockResponse = MockResponse()
        mockResponse.setBody(json)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario =
            launchFragmentInContainer<UserListFragment>(themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar)

        var idlingResource: TestIdlingResource? = null

        scenario.onFragment { fragment ->
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()

        }

        onView(withId(R.id.loadingIndicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

    }


}
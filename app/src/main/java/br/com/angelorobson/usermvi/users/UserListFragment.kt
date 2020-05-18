package br.com.angelorobson.usermvi.users

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.getViewModel
import br.com.angelorobson.usermvi.users.widget.UserListAdapter
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.user_fragment.*

class UserListFragment : Fragment(R.layout.user_fragment) {

    lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = UserListAdapter()
        recyclerView.adapter = adapter


        disposable = Observable.mergeArray(
            adapter.userClicks.map { UserClicked(it.id) }
        ).compose(getViewModel(UserListViewModel::class).init(event = Initial))
            .subscribe { model ->
                if (model.usersResult is UsersResult.UserLoaded) {
                    loadingIndicator.isVisible = model.usersResult.isLoading
                    tvError.isVisible = false
                    val users = model.usersResult.users
                    adapter.submitList(users)
                }
                if (model.usersResult is UsersResult.Error) {
                    loadingIndicator.isVisible = false
                    tvError.isVisible = true
                }
                if (model.usersResult is UsersResult.Empty) {
                    loadingIndicator.isVisible = false
                    tvError.isVisible = false
                }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}


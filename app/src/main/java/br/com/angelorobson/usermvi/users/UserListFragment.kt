package br.com.angelorobson.usermvi.users

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.getViewModel
import br.com.angelorobson.usermvi.users.widget.UserListAdapter
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

        disposable = Observable.empty<UserListEvent>()
            .compose(getViewModel(UserListViewModel::class).init(event = Initial))
            .subscribe { model ->
                if (model.usersResult is UsersResult.UserLoaded) {
                    loadingIndicator.isVisible = model.usersResult.isLoading
                    val users = model.usersResult.users
                    adapter.submitList(users)
                }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}


package br.com.angelorobson.usermvi.userdetails

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.getViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.user_detail_fragment.*
import kotlinx.android.synthetic.main.user_fragment.loadingIndicator
import kotlinx.android.synthetic.main.user_fragment.tvError

class UserDetailFragment : Fragment(R.layout.user_detail_fragment) {

    private val args: UserDetailFragmentArgs by navArgs()

    private lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()

        disposable = Observable.empty<UserDetailEvent>()
            .compose(getViewModel(UserDetailViewModel::class).init(Initial(args.id.toInt())))
            .subscribe {
                val user = it.user
                loadingIndicator.isVisible = it.isLoading
                tvError.isVisible = it.error
                tvName.text = user?.name
                tvUserName.text = user?.username
            }
    }

}
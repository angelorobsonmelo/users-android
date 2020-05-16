package br.com.angelorobson.usermvi.users.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.model.User
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_item.*

class UserListAdapter : ListAdapter<User, UserListViewHolder>(DiffUtilCallback()) {

    private val userClicksSubject = PublishSubject.create<Int>()
    val userClicks: Observable<User> = userClicksSubject
        .map { position ->
            getItem(position)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    viewType,
                    parent,
                    false
                ),
            userClicksSubject
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.user_item
    }


}

private class DiffUtilCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}

class UserListViewHolder(
    override val containerView: View,
    private val userClicksSubject: PublishSubject<Int>
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(user: User) {
        val context = containerView.context
        tvName.text = user.name
        tvUserName.text = user.username

        userCard.clicks().map { adapterPosition }.subscribe(userClicksSubject)
    }
}
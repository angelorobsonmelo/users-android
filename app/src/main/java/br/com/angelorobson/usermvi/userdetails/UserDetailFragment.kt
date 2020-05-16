package br.com.angelorobson.usermvi.userdetails

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.angelorobson.usermvi.R

class UserDetailFragment : Fragment(R.layout.user_detail_fragment) {

    private val args: UserDetailFragmentArgs by navArgs()

    override fun onStart() {
        super.onStart()
        println(args.id)
    }

}
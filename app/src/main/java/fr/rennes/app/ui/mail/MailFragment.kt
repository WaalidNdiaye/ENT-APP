package fr.rennes.app.ui.mail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.rennes.app.R

class MailFragment : Fragment() {

    companion object {
        fun newInstance() = MailFragment()
    }

    private lateinit var viewModel: MailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
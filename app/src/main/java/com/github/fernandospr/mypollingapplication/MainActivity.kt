package com.github.fernandospr.mypollingapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.fernandospr.mypollingapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory((application as MyApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            viewModel.postPerson(Person(binding.editText.text.toString()))
        }
        viewModel.state.observe(this, ::handleState)
    }

    private fun handleState(state: MainState) {
        when (state) {
            is MainState.Loading -> showLoading()
            is MainState.Approved -> showApproved(state.person)
            is MainState.Pending -> showPending()
            is MainState.Rejected -> showRejected()
            is MainState.Error -> showError()
        }
    }

    private fun showError() {
        binding.textView.text = "Error"
        binding.button.isEnabled = true
    }

    private fun showRejected() {
        binding.textView.text = "Rejected"
        binding.button.isEnabled = true
    }

    private fun showPending() {
        binding.textView.text = "Pending"
        binding.button.isEnabled = true
    }

    private fun showApproved(person: Person) {
        binding.textView.text = "Approved - Payload: $person"
        binding.button.isEnabled = true
    }

    private fun showLoading() {
        binding.textView.text = "Loading..."
        binding.button.isEnabled = false
    }
}

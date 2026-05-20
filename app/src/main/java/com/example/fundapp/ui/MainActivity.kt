package com.example.fundapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.databinding.ActivityMainBinding
import com.example.fundapp.databinding.DialogAddFundBinding
import com.example.fundapp.viewmodel.FundViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fundAdapter: FundAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FundViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[FundViewModel::class.java]

        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        setupToolbar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        fundAdapter = FundAdapter(
            onDeleteClick = { code -> showDeleteConfirm(code) },
            onClearAllClick = { showClearAllConfirm() }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = fundAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshFunds()
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddFundDialog()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun observeViewModel() {
        viewModel.funds.observe(this) { funds ->
            fundAdapter.submitList(funds)
            binding.emptyView.visibility = if (funds.isEmpty()) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.isRefreshing.observe(this) { isRefreshing ->
            binding.swipeRefresh.isRefreshing = isRefreshing
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun showAddFundDialog() {
        val dialogBinding = DialogAddFundBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.add_fund)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.confirm) { _, _ ->
                val code = dialogBinding.etFundCode.text.toString().trim()
                val amountStr = dialogBinding.etFundAmount.text.toString().trim()
                val amount = if (amountStr.isEmpty()) 0.0 else amountStr.toDoubleOrNull() ?: 0.0

                if (code.length != 6) {
                    Toast.makeText(this, R.string.invalid_code, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.addFund(code, amount)
                Toast.makeText(this, getString(R.string.add_fund), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirm(code: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirm)
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.deleteFund(code)
                Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    private fun showClearAllConfirm() {
        AlertDialog.Builder(this)
            .setTitle(R.string.clear_all)
            .setMessage(R.string.clear_confirm)
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.deleteAllFunds()
                Toast.makeText(this, R.string.clear_all, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }
}
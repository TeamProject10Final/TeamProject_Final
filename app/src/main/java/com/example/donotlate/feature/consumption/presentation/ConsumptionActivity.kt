package com.example.donotlate.feature.consumption.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.ConsumptionContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityConsumptionBinding
import kotlinx.coroutines.launch

class ConsumptionActivity : AppCompatActivity() {



    private lateinit var binding: ActivityConsumptionBinding

    private lateinit var finishedAdapter: ConsumptionAdapter
    private lateinit var unfinishedAdapter: ConsumptionAdapter

    private lateinit var consumptionViewModel: ConsumptionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        binding = ActivityConsumptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consumption)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCalculate.setOnClickListener {
            val intent = Intent(this, CalculationActivity::class.java)
            startActivity(intent)
        }


        val appContainer = (application as DoNotLateApplication).appContainer

        appContainer.consumptionContainer = ConsumptionContainer(
            appContainer.getFinishedConsumptionUseCase,
            appContainer.getUnfinishedConsumptionUseCase,
            appContainer.insertConsumptionUseCase,
            appContainer.deleteConsumptionUseCase,
            appContainer.getConsumptionByCategoryUseCase,
            appContainer.getConsumptionByIdUseCase,
            appContainer.getConsumptionDataUseCase,
            appContainer.getTotalPriceUseCase,
            appContainer.getDataCountUseCase,
            appContainer.getLiveDataCountUseCase,
            appContainer.toggleIsFinishedUseCase
        )

        appContainer.consumptionContainer?.let {
            consumptionViewModel = ViewModelProvider(
                this,
                it.consumptionViewModelFactory
            )[ConsumptionViewModel::class.java]
        }

        setupRecyclerViews()
        observeViewModel()

        binding.btnBackActivity.setOnClickListener {
            onBackPressed()
        }

//        //지각 여부를 채팅창에서 받아와야 할듯... livedata로... 그거로 tvNickname 바꾸기!
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            launch {
                consumptionViewModel.finishedConsumptions.collect { finishedList ->
                    if (finishedList.isNotEmpty()) {
                        binding.tv04.visibility = View.VISIBLE
                        finishedAdapter.submitList(finishedList)
                    } else {
                        binding.tv04.visibility = View.INVISIBLE
                    }
                }
            }

            launch {
                consumptionViewModel.unfinishedConsumptions.collect { unfinishedList ->
                    if (unfinishedList.isNotEmpty()) {
                        binding.tv02.visibility = View.VISIBLE
                        unfinishedAdapter.submitList(unfinishedList)
                    } else {
                        binding.tv02.visibility = View.INVISIBLE
                    }
                }
            }

            launch {
                consumptionViewModel.errorState.collect { error ->
                    Toast.makeText(
                        this@ConsumptionActivity,
                        "Error: $error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            launch {
                consumptionViewModel.totalPrice.collect { totalPrice ->
                    binding.tvExpenseNum.text = "${totalPrice.addCommas()} 원"
                }
            }

            launch {
                consumptionViewModel.liveDataCount.collect { count ->
                    binding.tvVisitNum.text = "총 $count 건"
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        finishedAdapter = ConsumptionAdapter { model ->
            showConfirmationDialog(model)
        }
        unfinishedAdapter = ConsumptionAdapter { model ->
            showConfirmationDialog(model)
        }

        binding.rvCalculateFinish.apply {
            adapter = finishedAdapter
            layoutManager = LinearLayoutManager(this@ConsumptionActivity)
        }

        binding.rvCalculateProgress.apply {
            adapter = unfinishedAdapter
            layoutManager = LinearLayoutManager(this@ConsumptionActivity)
        }
    }

    private fun showConfirmationDialog(item: ConsumptionModel) {
        AlertDialog.Builder(this)
            .setMessage("정산 상태를 변경할까요?\n\n${item.isFinished} -> ${!item.isFinished}")
            .setPositiveButton("Yes") { _, _ ->
                consumptionViewModel.toggleIsFinished(item)
            }
            .setNegativeButton("No", null)
            .show()
    }

    //utils로 빼내기!!!!
    companion object {

        fun Int.addCommas(): String {
            return String.format("%,d", this)
        }

        fun Long.addCommas(): String {
            return String.format("%,d", this)
        }


        fun String.removeCommasToInt(): Int {
            return try {
                this.replace(",", "").toInt()
            } catch (e: NumberFormatException) {
                0 // 혹은 예외처리에 따라 다른 값 반환 가능
            }
        }

        fun hideKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}




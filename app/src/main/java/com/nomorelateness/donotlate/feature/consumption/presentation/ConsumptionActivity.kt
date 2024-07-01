package com.nomorelateness.donotlate.feature.consumption.presentation


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.ActivityConsumptionBinding
import com.nomorelateness.donotlate.feature.consumption.presentation.dialog.ConfirmDialogInterface
import com.nomorelateness.donotlate.feature.consumption.presentation.dialog.ConsumptionConfirmDialog
import com.nomorelateness.donotlate.feature.consumption.presentation.dialog.DeleteDialogInterface
import kotlinx.coroutines.launch

class ConsumptionActivity : AppCompatActivity(), ConfirmDialogInterface, DeleteDialogInterface {


    private lateinit var binding: ActivityConsumptionBinding

    private lateinit var finishedAdapter: ConsumptionAdapter
    private lateinit var unfinishedAdapter: ConsumptionAdapter

    private lateinit var consumptionViewModel: ConsumptionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
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


        val appContainer =
            (application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer

        appContainer.consumptionContainer = com.nomorelateness.donotlate.ConsumptionContainer(
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
            appContainer.toggleIsFinishedUseCase,
            appContainer.getCurrentUserDataUseCase
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

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeViewModel() {
        lifecycleScope.launch {
            launch {
                consumptionViewModel.finishedConsumptions.collect { finishedList ->
                    if (finishedList.isNotEmpty()) {
                        binding.tv04.visibility = View.VISIBLE
                        finishedAdapter.submitList(finishedList)
                    } else {
                        binding.tv04.visibility = View.INVISIBLE
                        finishedAdapter.submitList(emptyList())
                        finishedAdapter.notifyDataSetChanged()
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
                        unfinishedAdapter.submitList(emptyList())
                        unfinishedAdapter.notifyDataSetChanged()
                    }
                }
            }

            launch {
                consumptionViewModel.errorState.collect { error ->
                    if (error.isNotEmpty() && error != null) {
                        Toast.makeText(
                            this@ConsumptionActivity,
                            "Error 123: $error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.d("확인 에러", error)
                }
            }

            launch {
                consumptionViewModel.totalPrice.collect { totalPrice ->
                    if (totalPrice == 0L) {
                        Log.d("확인 long2? 0l", "$totalPrice")
                        binding.tvExpenseNum.text =
                            "${totalPrice} ${resources.getString(R.string.cal_activity_text1)}"
                    } else {
                        Log.d("확인 long2?", "$totalPrice")
                        binding.tvExpenseNum.text =
                            "${totalPrice.addCommas()} ${resources.getString(R.string.cal_activity_text1)}"
                    }
                }
            }

            launch {
                consumptionViewModel.liveDataCount.collect { count ->
                    binding.tvVisitNum.text =
                        "$count ${resources.getString(R.string.cal_activity_text2)}"
                }
            }
            launch {
                consumptionViewModel.currentUserData.collect { userData ->
                    userData?.let { binding.tvUserName.text = userData.name }
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
        val dialog = ConsumptionConfirmDialog(this, this, item)
        dialog.show(this.supportFragmentManager, "tag")
    }

    //utils로 빼내기!!!!
    companion object {

        fun Int.addCommas(): String {
            return String.format("%,d", this)
        }

        fun Long.addCommas(): String {
            Log.d("확인 long error?", "여긴가...")
            return String.format("%,d", this)
        }


        fun String.removeCommasToInt(): Int {
            return try {
                this.replace(",", "").toInt()
            } catch (e: NumberFormatException) {
                0 // 혹은 예외처리에 따라 다른 값 반환 가능
            }
        }
//
//        fun hideKeyboard(view: View) {
//            val imm =
//                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
    }

    override fun onPositiveButtonClicked(model: ConsumptionModel) {
        consumptionViewModel.toggleIsFinished(model)
    }

    override fun onDeleteButtonClicked(model: ConsumptionModel) {
        Log.d("확인 delete clicked", "$model")
        consumptionViewModel.deleteConsumption(model)
    }

}




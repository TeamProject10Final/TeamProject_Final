package com.example.donotlate.consumption

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityConsumptionBinding

class ConsumptionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityConsumptionBinding.inflate(layoutInflater) }

    private lateinit var finishedAdapter: ConsumptionAdapter
    private lateinit var unfinishedAdapter: ConsumptionAdapter


    private val viewModel: SharedViewModel by viewModels {
        val appContainer = (application as DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.consumptionRepository
//            appContainer.getFinishedConsumptionUseCase,
//            appContainer.getUnfinishedConsumptionUseCase,
//            appContainer.insertConsumptionUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

        setupRecyclerViews()
        observeViewModel()

//        viewModel.error.observe(this, Observer { error ->
//            error?.let {
//                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        // 데이터베이스에서 데이터의 총 가격을 실시간으로 관찰하고 업데이트... 인데 , 때문인지 계산 이상함
//        viewModel.totalPrice.observe(this, Observer { totalPrice ->
////            binding.tvExpenseNum.text = "${String.format("%.2f", totalPrice)} 원"
//            if (totalPrice != null) {
//                binding.tvExpenseNum.text = "${totalPrice.addCommas()} 원"
//            }else{
//                binding.tvExpenseNum.text = "0 원"
//            }
//        })
//
//        // 데이터베이스에서 데이터의 개수를 실시간으로 관찰하고 업데이트
//        viewModel.dataCount.observe(this, Observer { count ->
//            if(count != null) {
//                binding.tvVisitNum.text = "총 $count 건"
//            }else{
//                binding.tvVisitNum.text = "총 0 건"
//            }})
//
//        //지각 여부를 채팅창에서 받아와야 할듯... livedata로... 그거로 tvNickname 바꾸기!
    }

    private fun observeViewModel() {
        // 완료된 소비 데이터 관찰 (날짜기준 최근 순 5개)
        viewModel.finishedConsumption.observe(this) { finishedList ->
            if (finishedList != null) {
                binding.tv04.visibility = View.VISIBLE
                finishedAdapter.submitList(finishedList)
            } else{
                binding.tv04.visibility = View.INVISIBLE
            }
        }

        // 미완료된 소비 데이터 관찰
        viewModel.unfinishedConsumption.observe(this) { unfinishedList ->
            if (unfinishedList != null) {
                binding.tv02.visibility = View.VISIBLE
                unfinishedAdapter.submitList(unfinishedList)
            }else{
                binding.tv02.visibility = View.INVISIBLE
            }
        }

        viewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })

        // 데이터베이스에서 데이터의 총 가격을 실시간으로 관찰하고 업데이트... addCommas 부분 확인하기
        viewModel.totalPrice.observe(this, Observer { totalPrice ->
            if (totalPrice != null) {
                binding.tvExpenseNum.text = "${totalPrice.addCommas()} 원"
            }else{
                binding.tvExpenseNum.text = "0 원"
            }
        })

        // 데이터베이스에서 데이터의 개수를 실시간으로 관찰하고 업데이트
        viewModel.dataCount.observe(this, Observer { count ->
            if(count != null) {
                binding.tvVisitNum.text = "총 $count 건"
            }else{
                binding.tvVisitNum.text = "총 0 건"
            }})

        //지각 여부는 채팅창에서 받아와야 할듯... livedata로... 그거로 tvNickname 바꾸기!
    }

    private fun setupRecyclerViews() {

        //아이템 클릭 시 isFinished 변경할 수 있는 거 띄우기

        // RecyclerView 초기화 및 어댑터 설정
        finishedAdapter = ConsumptionAdapter()
        binding.rvCalculateFinish.apply {
            adapter = finishedAdapter
            layoutManager = LinearLayoutManager(this@ConsumptionActivity)
        }

        unfinishedAdapter = ConsumptionAdapter()
        binding.rvCalculateProgress.apply {
            adapter = unfinishedAdapter
            layoutManager = LinearLayoutManager(this@ConsumptionActivity)
        }
    }

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
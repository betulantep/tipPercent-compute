package com.betulantep.tippercentcalc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.betulantep.tippercentcalc.databinding.ActivityMainBinding

private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekBarPercent.progress = INITIAL_TIP_PERCENT
        binding.tvPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        binding.seekBarPercent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }

        })
    }

    @SuppressLint("RestrictedApi")
    private fun updateTipDescription(tipPercent : Int) {
        val tipDescription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        binding.tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/binding.seekBarPercent.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        binding.tvTipDescription.setTextColor(color)
    }

    @SuppressLint("SetTextI18n")
    private fun computeTipAndTotal() {
        if(binding.etBase.text.isEmpty()){
            binding.apply {
                tvTip.text = ""
                tvTotal.text = ""
            }
            return
        }
        val baseAmount = binding.etBase.text.toString().toDouble()
        val tipPercent = binding.seekBarPercent.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        binding.tvTip.text = "%.2f".format(tipAmount)
        binding.tvTotal.text = "%.2f".format(totalAmount)
    }
}
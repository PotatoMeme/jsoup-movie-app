package com.potatomeme.jsoupmovieapp.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.potatomeme.jsoupmovieapp.databinding.DialogDatepickerBinding

class DateDialog(private val context :AppCompatActivity,private val date: String = "") {
    private lateinit var binding : DialogDatepickerBinding
    private val dlg = Dialog(context)

    private lateinit var listener : DateDialogOKClickedListener

    fun show(){
        binding = DialogDatepickerBinding.inflate(context.layoutInflater)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(binding.root)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!date.isNullOrEmpty()){

            binding.datePicker.updateDate(date.substring(0,4).toInt(),date.substring(4,6).toInt()-1,date.substring(6).toInt())
        }

        binding.tvOk.setOnClickListener {
            listener.onOKClicked(
                (binding.datePicker.year*10000+(binding.datePicker.month+1)*100+binding.datePicker.dayOfMonth).toString()
            )
            dlg.dismiss()
        }

        binding.tvCancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: DateDialogOKClickedListener {
            override fun onOKClicked(dateStr: String) {
                listener(dateStr)
            }

        }
    }


    interface DateDialogOKClickedListener {
        fun onOKClicked(dateStr: String)
    }
}